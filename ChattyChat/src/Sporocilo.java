import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Sporocilo {
	@JsonProperty("global")
	private Boolean global;
	@JsonProperty("recipient")
	private String recipient;
	@JsonProperty("sender")
	private String sender;
	@JsonProperty("text")
	private String text;
	@JsonProperty("sent_at")
	private Date sent_at;
	
	public Sporocilo() {};
	
	//PREJETA
	public Sporocilo(Boolean javno, String prejemnik, String posiljatelj, 
			String besedilo, Date cas) {
		this.setGlobal(javno);
		this.setRecipient(prejemnik);
		this.setSender(posiljatelj);
		this.setText(besedilo);
		this.setSent_at(cas);
	}

	public static void main(String[] args) {
		
		// TODO Auto-generated method stub

	}
	
	public Boolean getGlobal() {
		return global;
	}

	public void setGlobal(Boolean global) {
		this.global = global;
	}

	
	public String getRecipient() {
		return recipient;
	}

	public void setRecipient(String recipient) {
		this.recipient = recipient;
	}

	public String getSender() {
		return sender;
	}

	public void setSender(String sender) {
		this.sender = sender;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public Date getSent_at() {
		return sent_at;
	}

	public void setSent_at(Date sent_at) {
		this.sent_at = sent_at;
	}

}
