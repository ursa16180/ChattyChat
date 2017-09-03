import java.util.Date;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Uporabnik {
	@JsonProperty("username")
	private String username;
	@JsonProperty("last_active")
	private Date last_active;
	public Uporabnik() {}; //potreben prazen konstruktor
	
	public Uporabnik (String ime, Date aktivnost) {
		this.setUsername(ime);
		this.setLast_active(aktivnost);
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

	
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	
	public Date getLast_active() {
		return last_active;
	}

	public void setLast_active(Date last_active) {
		this.last_active = last_active;
	}

}
