import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collections;
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
		timer.scheduleAtFixedRate(this, 2000, 1000);
	}

	// Deaktiviraj robotka
	public void deaktiviraj() {
		timer.cancel();
	}

	@Override
	public void run() {
		try {
			novaSporocila = App.preberi(chat.jaz);
			prikazi_sporocila(novaSporocila);
			uporabniki = App.vrniVpisane();
			List<String> seznam = toList(uporabniki);
			sporociNedosegljive(seznam);
			// pretvori v seznam in v zasebnih pogovorih pove da se je izpisal
			chat.prikaziUporabnike(seznam);// prikaze uporabnike v chatu

			// uporabniki = App.vrniVpisane();
			// prikazi_uporabnike(uporabniki);
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

	private void sporociNedosegljive(List<String> seznamDosegljivih) {
		Set<String> mnozicaZasebnih = chat.slovarZasebni.keySet();
		for (String posameznik : mnozicaZasebnih) {
			if (seznamDosegljivih.contains(posameznik) && !chat.slovarZasebni.get(posameznik).getAktiven()) {
				chat.slovarZasebni.get(posameznik).addMessage("Sistem", "Oseba " + posameznik + " je dosegljiva.");
				chat.slovarZasebni.get(posameznik).setAktiven(true);
				chat.slovarZasebni.get(posameznik).setEnabled(true);
			}
			if (!seznamDosegljivih.contains(posameznik) && chat.slovarZasebni.get(posameznik).getAktiven()) {
				chat.slovarZasebni.get(posameznik).addMessage("Sistem", "Oseba " + posameznik + " je nedosegljiva.");
				chat.slovarZasebni.get(posameznik).setAktiven(false);
				chat.slovarZasebni.get(posameznik).setEnabled(false);
			}

		}

	}

	private List<String> toList(List<Uporabnik> uporabniki) {
		List<String> seznamUporabnikov = new ArrayList<String>();
		for (Uporabnik uporabnik : uporabniki) {
			String imeUporabnika = uporabnik.getUsername();
			seznamUporabnikov.add(imeUporabnika);// TODO if stavek da me ne doda na seznam ali da me na koncu izbriše
		}
		return seznamUporabnikov;
	}

	private void prikazi_sporocila(List<Sporocilo> novaSporocila) {
		// Iz seznama dobi posamezna sporocila
		for (Sporocilo posamezno : novaSporocila) {
			Boolean javno = posamezno.getGlobal();
			String posiljatelj = posamezno.getSender();
			String besedilo = posamezno.getText();
			if (javno) {
				chat.addMessage(posiljatelj, besedilo);
				System.out.println(javno);
			} else {
				System.out.println("Global je False");// TODO nikol ne doseže tega elsa in ne izpisuje zasebnih
				if (chat.slovarZasebni.containsKey(posiljatelj)) {
					chat.slovarZasebni.get(posiljatelj).addMessage(posiljatelj, besedilo);
					chat.slovarZasebni.get(posiljatelj).toFront();
					System.out.println(javno);
					System.out.println("else-if");

				} else {
					ZasebniPogovor pogovor = new ZasebniPogovor(posiljatelj, chat.jaz);
					pogovor.pack();
					pogovor.setVisible(true);
					chat.slovarZasebni.put(posiljatelj, pogovor);
					pogovor.addMessage(posiljatelj, besedilo);
					System.out.println(javno);
					System.out.println("else-else");
				}

			}
		}

	}

}
