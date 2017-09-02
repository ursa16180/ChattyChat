


//Ta robot nam bo preverjal, �e je kaj novih sporo�il na stre�niku.
//To mora sporo�it chatframu? ali Chit chat?

import java.util.Timer;
import java.util.TimerTask;


public class Robotek extends TimerTask {
private ChatFrame chat;
private Timer timer;



public Robotek(ChatFrame chat) {
	this.chat = chat;

}

/**
 * Activate the robot!
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
	String novaSporocila = App.preberi(chat.jaz);
	prikazi_sporocila(novaSporocila);
	
}

private void prikazi_sporocila(String novaSporocila) {
	// TODO Auto-generated method stub
	
}


}
