import java.awt.Container;
import java.awt.FlowLayout;
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
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class ChatFrame extends JFrame implements ActionListener, KeyListener {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JTextArea output;
	private JTextField input;
	private JTextField poljeVzdevek;
	private JButton gumbPrijava;
	private JButton gumbOdjava;
	private String jaz;

	public ChatFrame() {
		super();
		setTitle("Chatty chat");
		Container pane = this.getContentPane();
		pane.setLayout(new GridBagLayout());
		
		JPanel zgornjeOkno = new JPanel();
		JLabel vnesiVzdevek = new JLabel("Vzdevek:");
		this.jaz = new String(System.getProperty("user.name"));
		this.poljeVzdevek = new JTextField(System.getProperty("user.name"), 15);
		this.gumbPrijava = new JButton("Prijava");
		this.gumbPrijava.addActionListener(this);
		
		this.gumbOdjava = new JButton("Odjava");
		this.gumbOdjava.addActionListener(this);
		
		FlowLayout lajoutmenedzer = new FlowLayout(FlowLayout.LEFT);
		zgornjeOkno.setLayout(lajoutmenedzer);
		zgornjeOkno.add(vnesiVzdevek);
		zgornjeOkno.add(poljeVzdevek);
		zgornjeOkno.add(gumbPrijava);
		zgornjeOkno.add(gumbOdjava);
		
		GridBagConstraints zgornjeOknoConstraint = new GridBagConstraints();
		zgornjeOknoConstraint.gridx = 0;
		zgornjeOknoConstraint.gridy = 0;
		zgornjeOknoConstraint.fill = GridBagConstraints.HORIZONTAL;
		pane.add(zgornjeOkno, zgornjeOknoConstraint);

		
		
		this.output = new JTextArea(20, 40);
		this.output.setEditable(false);
		GridBagConstraints drsnikConstraint = new GridBagConstraints();
		drsnikConstraint.gridx = 0;
		drsnikConstraint.gridy = 1;
		drsnikConstraint.fill = GridBagConstraints.BOTH;
		drsnikConstraint.weightx = 1;
		drsnikConstraint.weighty = 1;
		JScrollPane drsnik = new JScrollPane(output);
		pane.add(drsnik, drsnikConstraint);
		
		this.input = new JTextField(40);
		GridBagConstraints inputConstraint = new GridBagConstraints();
		inputConstraint.gridx = 0;
		inputConstraint.gridy = 2;
		inputConstraint.fill=GridBagConstraints.VERTICAL;
		pane.add(input, inputConstraint);
		input.addKeyListener(this);
		
		addWindowListener(new WindowAdapter(){
			public void windowOpened(WindowEvent e){
				input.requestFocusInWindow();
			}
		});
	}

	/**
	 * @param person - the person sending the message
	 * @param message - the message content
	 */
	public void addMessage(String person, String message) {
		String chat = this.output.getText();
		this.output.setText(chat + person + ": " + message + "\n");
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource()==this.gumbPrijava){
			this.jaz = poljeVzdevek.getText();
			App.vpisiMe(this.jaz);
		}
		if (e.getSource()==this.gumbOdjava){
			App.izpisiMe(this.jaz);
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {
		if (e.getSource() == this.input) {
			if (e.getKeyChar() == '\n') {
				App.poslji(jaz, this.input.getText());
				this.addMessage(jaz, this.input.getText());
				this.input.setText("");
			}
		}		
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
