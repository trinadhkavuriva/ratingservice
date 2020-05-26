package gov.va.med.es.fhir.ratingservice.model;

public class Message {

	int code;
	String error;
	String description;

	
	public Message() {
	}
	
	public Message(int code, String error, String description) {
		this.code = code;
		this.error = error;
		this.description = description;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
