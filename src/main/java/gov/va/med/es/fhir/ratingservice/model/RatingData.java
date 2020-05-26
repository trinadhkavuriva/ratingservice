package gov.va.med.es.fhir.ratingservice.model;

import java.util.ArrayList;
import java.util.List;

public class RatingData {

	List<String> messages = new ArrayList<>();
	List<String> devMessages = new ArrayList<>();
	
	
	
	// Combined Evaluation Service Connected
	private String combinedEvalPercent;

	private String combinedEvalEffectiveDt;

	private List<RatedDisability> ratedDisabilities;

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

	public List<RatedDisability> getRatedDisabilities() {
		return ratedDisabilities;
	}

	public void setRatedDisabilities(List<RatedDisability> ratedDisabilities) {
		this.ratedDisabilities = ratedDisabilities;
	}
	
	
	public void addRatedDisability(RatedDisability ratedDisability) {
		if ( ratedDisabilities == null ) {
			ratedDisabilities = new ArrayList<RatedDisability>();
		}
		ratedDisabilities.add(ratedDisability);
	}

	
	public void addMessage(String message) {
		if ( this.messages == null ) {
			this.messages = new ArrayList<String>();
		}
		this.messages.add(message);
	}
	
	
	public void addDevMessage(String message) {
		if ( this.devMessages == null ) {
			this.devMessages = new ArrayList<String>();
		}
		this.devMessages.add(message);
	}
}
