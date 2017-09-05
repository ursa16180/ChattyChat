import java.awt.Dimension;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
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

	// Aktivacija robotka
	public void aktiviraj() {
		timer = new Timer();
		timer.scheduleAtFixedRate(this, 0, 1000);
	}

	// Deaktivacija robotka
	public void deaktiviraj() {
		timer.cancel();
	}

	@Override
	public void run() {
		try {
			novaSporocila = KomunikacijaServer.sprejemSporocil(chat.jaz);
			prikazi_sporocila(novaSporocila);
			uporabniki = KomunikacijaServer.vrniVpisane();
			List<String> seznam = toList(uporabniki); // Da ne pretvarja večkrat in mene odstrani
			sporociNedosegljive(seznam); // Sporoča v zasebnih pogovorih, če se je dopisovalec (ne)dosegljiv
			chat.prikaziUporabnike(seznam);// Prikaze uporabnike v chatu
		} catch (ClientProtocolException e) {
			// Auto-generated catch block
			e.printStackTrace();
		} catch (URISyntaxException e) {
			// Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void sporociNedosegljive(List<String> seznamDosegljivih) {
		Set<String> mnozicaZasebnih = chat.slovarZasebni.keySet();
		for (String posameznik : mnozicaZasebnih) {
			if (seznamDosegljivih.contains(posameznik) && !chat.slovarZasebni.get(posameznik).getAktiven()) {
				// Vpiše se nekdo s katerim imamo odprt neaktiven zasebni pogovor
				chat.slovarZasebni.get(posameznik).addMessage("Sistem", "Oseba " + posameznik + " je dosegljiva.");
				chat.slovarZasebni.get(posameznik).setAktiven(true);
				chat.slovarZasebni.get(posameznik).aktivirajPogovor();
			}
			if (!seznamDosegljivih.contains(posameznik) && chat.slovarZasebni.get(posameznik).getAktiven()) {
				// Izpiše se nekdo s katerim imamo odprt aktiven zasebni pogovor
				chat.slovarZasebni.get(posameznik).addMessage("Sistem", "Oseba " + posameznik + " je nedosegljiva.");
				chat.slovarZasebni.get(posameznik).setAktiven(false);
				chat.slovarZasebni.get(posameznik).deaktivirajPogovor();
			}
		}
	}

	private List<String> toList(List<Uporabnik> uporabniki) { // Pretvori v seznam nizov in odstrani mene
		List<String> seznamUporabnikov = new ArrayList<String>();
		for (Uporabnik uporabnik : uporabniki) {
			String imeUporabnika = uporabnik.getUsername();
			seznamUporabnikov.add(imeUporabnika);
		}
		seznamUporabnikov.remove(chat.jaz);
		return seznamUporabnikov;
	}

	private void prikazi_sporocila(List<Sporocilo> novaSporocila) {
		for (Sporocilo posamezno : novaSporocila) {
			Boolean javno = posamezno.getGlobal();
			String posiljatelj = posamezno.getSender();
			String besedilo = posamezno.getText();
			if (javno) {
				chat.addMessage(posiljatelj, besedilo);
			} else {
				if (chat.slovarZasebni.containsKey(posiljatelj)) {
					chat.slovarZasebni.get(posiljatelj).addMessage(posiljatelj, besedilo);
					chat.slovarZasebni.get(posiljatelj).toFront();
				} else {
					ZasebniPogovor pogovor = new ZasebniPogovor(posiljatelj, chat.jaz);
					pogovor.setMinimumSize(new Dimension(100, 100));
					pogovor.pack();
					pogovor.setVisible(true);
					chat.slovarZasebni.put(posiljatelj, pogovor);
					pogovor.addMessage(posiljatelj, besedilo);
				}

			}
		}

	}

}
