package gov.va.med.es.fhir.ratingservice.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.hl7.fhir.r4.model.CodeableConcept;
import org.hl7.fhir.r4.model.HumanName;
import org.hl7.fhir.r4.model.Identifier;
import org.hl7.fhir.r4.model.Patient;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.parser.IParser;
import gov.va.med.es.fhir.ratingservice.model.Message;
import gov.va.med.es.fhir.ratingservice.model.RatedDisability;
import gov.va.med.es.fhir.ratingservice.model.RatingData;
import gov.va.med.es.fhir.ratingservice.model.RatingResponse;

@RestController
@RequestMapping("/rating-service")
public class RatingServiceApiController {

	private final Log logger = LogFactory.getLog(getClass());

	final String[] DISALLOWED_FIELDS = new String[] {};

	@InitBinder
	public void initBinder(WebDataBinder binder) {
		binder.setDisallowedFields(DISALLOWED_FIELDS);
	}

	@RequestMapping(value = "/{patientId}/read", produces = { "application/json" }, method = RequestMethod.GET)
	public ResponseEntity<RatingResponse> readUsingGET(
			//@PathVariable String patientId,
			@PathVariable(value = "patientId", required=true) String patientId,
			@RequestHeader(value = "token", required = true) String token) {

		RatingResponse response = new RatingResponse();

		// call FHIR DTSU2 API to get the patient data using patient id and
		// oauth2 token. If the token expired or invalid we will populate the
		// response head with the appropriate Http status code.
		getPatientDataFromFHIR(patientId, token, response);

		if (response.hasErrors()) {
			return new ResponseEntity<RatingResponse>(response, response.getHttpCode());
		}
		// call ES with edipi to pull the rest of the rating for this patient.

		RatingData ratingData = new RatingData();
		ratingData.setCombinedEvalEffectiveDt((new Date()).toString());
		ratingData.setCombinedEvalPercent("70%");

		RatedDisability ratedDisability = new RatedDisability();
		ratedDisability.setDisabilityLine1("line1");
		ratedDisability.setServiceConnectedLine2("line 2");
		ratedDisability.setDisabilityHistLine3("line 3");
		ratingData.addRatedDisability(ratedDisability);
		response.setRatingData(ratingData);
		System.out.println("token is :" + token);

		ResponseEntity<RatingResponse> responseEntity = new ResponseEntity<RatingResponse>(response, HttpStatus.OK);
		return responseEntity;
	}

	private void getPatientDataFromFHIR(String patientId, String token, RatingResponse response) {

		String url = "https://fhir-ehr.sandboxcerner.com/dstu2/0b8a0111-e8e6-4c26-a91c-5069cbc6b1ca/Patient/"
				+ patientId;

		CloseableHttpClient httpclient = HttpClientBuilder.create().build();
		HttpGet get = new HttpGet(url);
		get.setHeader("accept", "application/json");
		get.setHeader("Content-Type", "application/json+fhir; fhirVersion=4.0; charset=utf-8");
		get.setHeader("authorization", "Bearer " + token);
		HttpResponse fhirResponse = null;
		try {
			fhirResponse = httpclient.execute(get);

			// if this is not a successful http response then populate the error message
			// and return.

			int httpStatusCode = fhirResponse.getStatusLine().getStatusCode();
			if (HttpStatus.OK.equals(HttpStatus.valueOf(httpStatusCode)) == false) {
				Message errorMessage = new Message();
				errorMessage.setCode(fhirResponse.getStatusLine().getStatusCode());
				Header[] headers = fhirResponse.getHeaders("WWW-Authenticate");
				for (Header header : headers) {
					String value = header.getValue();
					errorMessage.setDescription(value);
					response.addMessage(errorMessage);
				}
				
				if ( headers.length <= 0 ) {
					errorMessage.setCode(httpStatusCode);
					response.addMessage(errorMessage);
				}
				
				return;
			}

			HttpEntity entity = fhirResponse.getEntity();

			if (entity != null) {
				InputStream instream = entity.getContent();
				String result = convertStreamToString(instream);
				instream.close();

				// ObjectMapper mapper = new ObjectMapper();
				// mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

				// mapper.addMixIn(Reference.class, ReferenceMixIn.class);
				// Patient patient = mapper.readValue(result, Patient.class);

				FhirContext ctx = FhirContext.forR4();
				IParser parser = ctx.newJsonParser();
				Patient patient = parser.parseResource(Patient.class, result);
				populatePatient(patient, response);
			}
		} catch (Exception eX) {
			logger.error("Unable to pull the patient for " + patientId, eX);
		} finally {
			try {
				if (httpclient != null) {
					httpclient.close();
				}
			} catch (IOException e) {
				logger.error(e);
			}
		}

	}

	private void populatePatient(Patient patient, RatingResponse response) {
		List<HumanName> names = patient.getName();
		for (HumanName name : names) {
			if (HumanName.NameUse.OFFICIAL.equals(name.getUse())) {
				response.setName(name.getText());
			}
		}
		response.setSex(patient.getGender().getDisplay());
		Date dob = patient.getBirthDate();
		SimpleDateFormat format = new SimpleDateFormat("MM-dd-yyyy");

		String dobString = format.format(dob);
		response.setDob(dobString);

		List<Identifier> identifiers = patient.getIdentifier();
		for (Identifier identifier : identifiers) {

			CodeableConcept codeableConcept = identifier.getType();

			if ("Military Id".equals(codeableConcept.getText())) {
				response.setEdipi(identifier.getValue());
			} else if ("MRN".equals(codeableConcept.getText())) {
				response.setMrn(identifier.getValue());
			}

		}
	}

	private static String convertStreamToString(InputStream is) {

		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		StringBuilder sb = new StringBuilder();

		String line = null;
		try {
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return sb.toString();
	}

}
