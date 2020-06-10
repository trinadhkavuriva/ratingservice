package gov.va.med.es.fhir.ratingservice.model;

import java.util.ArrayList;
import java.util.List;

public class RatingData {

	// Combined Evaluation Service Connected
	private String combinedEvalPercent;
	
	//this is imprecise date in ES
	private String combinedEvalEffectiveDt;
	private List<RatedDisabilityData> ratedDisabilities;

	List<String> messages = new ArrayList<>();
	List<String> devMessages = new ArrayList<>();

	String serviceTitle;
	String disabilitiesHeader;
	
	
	public String getCombinedEvalPercent() {
		return combinedEvalPercent;
	}

	public void setCombinedEvalPercent(String combinedEvalPercent) {
		this.combinedEvalPercent = combinedEvalPercent;
	}

	public String getCombinedEvalEffectiveDt() {
		return combinedEvalEffectiveDt;
	}

	public void setCombinedEvalEffectiveDt(String combinedEvalEffectiveDt) {
		this.combinedEvalEffectiveDt = combinedEvalEffectiveDt;
	}

	public List<RatedDisabilityData> getRatedDisabilities() {
		return ratedDisabilities;
	}

	public void setRatedDisabilities(List<RatedDisabilityData> ratedDisabilities) {
		this.ratedDisabilities = ratedDisabilities;
	}

	public void addRatedDisability(RatedDisabilityData ratedDisability) {
		if (ratedDisabilities == null) {
			ratedDisabilities = new ArrayList<RatedDisabilityData>();
		}
		ratedDisabilities.add(ratedDisability);
	}

	public List<String> getMessages() {
		return messages;
	}

	public void setMessages(List<String> messages) {
		this.messages = messages;
	}

	public List<String> getDevMessages() {
		return devMessages;
	}

	public void setDevMessages(List<String> devMessages) {
		this.devMessages = devMessages;
	}

	public String getServiceTitle() {
		return serviceTitle;
	}

	public void setServiceTitle(String serviceTitle) {
		this.serviceTitle = serviceTitle;
	}

	public String getDisabilitiesHeader() {
		return disabilitiesHeader;
	}

	public void setDisabilitiesHeader(String disabilitiesHeader) {
		this.disabilitiesHeader = disabilitiesHeader;
	}

}
