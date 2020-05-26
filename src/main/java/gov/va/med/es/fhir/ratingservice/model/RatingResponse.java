package gov.va.med.es.fhir.ratingservice.model;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;

public class RatingResponse {
	List<Message> messages = new ArrayList<>();
	List<String> devMessages = new ArrayList<>();

	private String edipi;

	private String icn;

	private String name;

	private String age;

	private String dob;

	private String sex;

	private String mrn;

	private String fin;

	private RatingData ratingData;

	public List<Message> getMessages() {
		return messages;
	}

	public void setMessages(List<Message> messages) {
		this.messages = messages;
	}

	public List<String> getDevMessages() {
		return devMessages;
	}

	public void setDevMessages(List<String> devMessages) {
		this.devMessages = devMessages;
	}

	public RatingData getRatingData() {
		return ratingData;
	}

	public void setRatingData(RatingData ratingData) {
		this.ratingData = ratingData;
	}

	public String getEdipi() {
		return edipi;
	}

	public void setEdipi(String edipi) {
		this.edipi = edipi;
	}

	public String getIcn() {
		return icn;
	}

	public void setIcn(String icn) {
		this.icn = icn;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAge() {
		return age;
	}

	public void setAge(String age) {
		this.age = age;
	}

	public String getDob() {
		return dob;
	}

	public void setDob(String dob) {
		this.dob = dob;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getMrn() {
		return mrn;
	}

	public void setMrn(String mrn) {
		this.mrn = mrn;
	}

	public String getFin() {
		return fin;
	}

	public void setFin(String fin) {
		this.fin = fin;
	}

	
	
	public void addMessage(int code, String error, String description) {
		if ( this.messages == null ) {
			this.messages = new ArrayList<Message>();
		}
		
		Message message = new Message(code, error, description);
		this.messages.add(message);
	}
	
	public void addMessage(Message message) {
		if ( this.messages == null ) {
			this.messages = new ArrayList<Message>();
		}
		this.messages.add(message);
	}
	
	
	public void addDevMessage(String message) {
		if ( this.devMessages == null ) {
			this.devMessages = new ArrayList<String>();
		}
		this.devMessages.add(message);
	}

	public boolean hasErrors() {
		if ( messages != null && messages.size() > 0 ) {
			return true;
		}
		return false;
	}

	public HttpStatus getHttpCode() {
		if ( messages != null && messages.size() > 0 ) {
			for ( Message message : messages) {
				return HttpStatus.resolve(message.getCode());
			}
		}
		return HttpStatus.OK;
	}
}
