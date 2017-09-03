import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.http.client.ClientProtocolException;


public class Robotek extends TimerTask {
	private ChatFrame chat;
	private Timer timer;
	private List<Sporocilo> novaSporocila;
	private List<Uporabnik> uporabniki;



public Robotek(ChatFrame chat) {
	this.chat = chat;

}

/**
 * Aktiviraj robotka:
 */
public void aktiviraj() {
	timer = new Timer();
	timer.scheduleAtFixedRate(this, 0, 1000);
}

public void deaktiviraj() {
	timer.cancel();	
}

@Override
public void run() {
	try {
		novaSporocila = App.preberi(chat.jaz);
		uporabniki = App.vrniVpisane();
		chat.prikaziUporabnike(uporabniki);
		//uporabniki = App.vrniVpisane();
		prikazi_sporocila(novaSporocila);
		//prikazi_uporabnike(uporabniki);
	} catch (ClientProtocolException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (URISyntaxException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}

	
}

private void prikazi_sporocila(List<Sporocilo> novaSporocila) {
	//Iz seznama dobi posamezna sporocila
	for (Sporocilo posamezno : novaSporocila) {
		String posiljatelj = posamezno.getSender();
		String besedilo = posamezno.getText();	
		
		chat.addMessage(posiljatelj, besedilo); //Pošlje sporočilo
		
	}
	
}

}
