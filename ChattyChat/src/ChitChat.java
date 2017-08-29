public class ChitChat {

	public static void main(String[] args) {
		ChatFrame chatFrame = new ChatFrame();
		PrimeRobot robot = new PrimeRobot(chatFrame);
		robot.activate();
		chatFrame.pack();
		chatFrame.setVisible(true);
	}

}
