import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class ZasebniPogovor extends JFrame implements KeyListener, ActionListener {
	private static final long serialVersionUID = 1L;
	public JTextArea output; // Izpisana sporočila
	public JTextField input; // Prostor za vpis
	private JButton gumbPoslji;
	private String jaz;
	private String dopisovalec;
	public Boolean aktiven; //Ali je pogovor aktiven ali se je človeč izpisal

	public ZasebniPogovor(String dopisovalec, String jaz) {
		super();
		this.jaz = jaz;
		this.dopisovalec = dopisovalec;
		this.aktiven = true; //ko se pogovor ustvari je aktiven
		setTitle(dopisovalec);
		Container pane = this.getContentPane();
		pane.setLayout(new GridBagLayout());
		
		//Prostor za izpisana sporočila
		this.output = new JTextArea(20, 40);
		this.output.setEditable(false);

		JScrollPane drsnik = new JScrollPane(output);
		GridBagConstraints drsnikConstraint = new GridBagConstraints();
		drsnikConstraint.gridx = 0;
		drsnikConstraint.gridy = 0;
		drsnikConstraint.fill = GridBagConstraints.BOTH;
		drsnikConstraint.weightx = 1;
		drsnikConstraint.weighty = 1;
		pane.add(drsnik, drsnikConstraint);
		
		this.input = new JTextField(40);
		GridBagConstraints inputConstraint = new GridBagConstraints();
		inputConstraint.gridx = 0;
		inputConstraint.gridy = 2;
		inputConstraint.weightx = 5;
		inputConstraint.weighty = 0;

		inputConstraint.fill = GridBagConstraints.BOTH;
		pane.add(input, inputConstraint);
		input.addKeyListener(this);

		this.gumbPoslji = new JButton("Pošlji");
		GridBagConstraints posljiConstraint = new GridBagConstraints();
		posljiConstraint.gridx = 1;
		posljiConstraint.gridy = 2;
		pane.add(gumbPoslji, posljiConstraint);
		gumbPoslji.addActionListener(this);

		addWindowListener(new WindowAdapter() {
			public void windowOpened(WindowEvent e) {
				input.requestFocusInWindow();
			}
			public void windowClosing(WindowEvent e) {
				ChitChat.chatFrame.slovarZasebni.remove(dopisovalec);
				//Zbriši iz slovarja
			}
		});
	}
	
	public Boolean getAktiven() {
		return aktiven;
	}

	public void setAktiven(Boolean aktiven) {
		this.aktiven = aktiven;
	}

	public void addMessage(String person, String message) {
		String chat = this.output.getText();
		this.output.setText(chat + person + ": " + message + "\n");
	}
	
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == this.gumbPoslji) {
			App.posljiZasebno(jaz, this.dopisovalec, this.input.getText());
			this.addMessage(jaz, this.input.getText());
			this.input.setText("");
		}
	}
	
	public void keyTyped(KeyEvent e) {
		if (e.getSource() == this.input) {
			if (e.getKeyChar() == '\n') {
				App.posljiZasebno(jaz,this.dopisovalec, this.input.getText());
				this.addMessage(jaz, this.input.getText());
				this.input.setText("");
			}
		}
	}
	

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

}
