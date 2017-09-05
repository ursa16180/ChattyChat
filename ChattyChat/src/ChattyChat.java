import java.awt.Dimension;

public class ChattyChat {

	public static ChatFrame chatFrame = new ChatFrame();

	public static void main(String[] args) {
		chatFrame.pack();
		chatFrame.setMinimumSize(new Dimension(450, 450));
		chatFrame.setVisible(true);
	}

}
