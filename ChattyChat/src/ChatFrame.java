import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class ChatFrame extends JFrame implements ActionListener, KeyListener, MouseListener {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JTextArea output;
	private JTextField input;
	private JTextField poljeVzdevek;
	private JButton gumbPrijava;
	private JButton gumbOdjava;
	private Box.Filler filer;
	public JPanel uporabnikiOkno;
	public String jaz;
	private Robotek robot;

	public ChatFrame() {
		super();
		setTitle("Chatty chat");
		Container pane = this.getContentPane();
		pane.setLayout(new GridBagLayout());
		
		JPanel zgornjeOkno = new JPanel();
		JLabel vnesiVzdevek = new JLabel("Vzdevek:");
		jaz = new String(System.getProperty("user.name"));
		poljeVzdevek = new JTextField(System.getProperty("user.name"), 15);
		//jaz = poljeVzdevek.getText()
		this.gumbPrijava = new JButton("Prijava");
		this.gumbPrijava.addActionListener(this);
		
		this.gumbOdjava = new JButton("Odjava");
		this.gumbOdjava.addActionListener(this);
		this.robot = new Robotek(this);
		
		//Zgornja vrstica za prijavo in odjavo
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
		
		//Seznam prijavljenih uporabnikov
		this.uporabnikiOkno = new JPanel();
		this.uporabnikiOkno.setBackground(Color.blue);
		JLabel trenutnoPrijavljeni = new JLabel("Trenutno prijavljeni:");
		this.uporabnikiOkno.setLayout(new BoxLayout(this.uporabnikiOkno, BoxLayout.Y_AXIS));
		this.uporabnikiOkno.add(trenutnoPrijavljeni);
		//Filer da niso uporabniki preblizu
		Dimension minSize = new Dimension(20, 5);
		Dimension prefSize = new Dimension(30, 10);
		Dimension maxSize = new Dimension(40, 15);
		this.filer= new Box.Filler(minSize, prefSize, maxSize);
		
		GridBagConstraints uporabnikiOknoConstraint = new GridBagConstraints();
		uporabnikiOknoConstraint.gridx = 1;
		uporabnikiOknoConstraint.gridy = 1;
		uporabnikiOknoConstraint.fill = GridBagConstraints.BOTH;
		pane.add(uporabnikiOkno, uporabnikiOknoConstraint);

		
		
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
			@Override
			public void windowClosed(WindowEvent e) {
				robot.deaktiviraj();
				App.izpisiMe(jaz);
			}
		});
	}

	public void addMessage(String person, String message) {
		String chat = this.output.getText();
		this.output.setText(chat + person + ": " + message + "\n");
	}
	
	public void prikaziUporabnike(List<Uporabnik> uporabniki) { //Naredi gumbe uporabnikov
		List<String> seznamUporabnikov = new ArrayList<String>();
		for (Uporabnik uporabnik : uporabniki) {
			String imeUporabnika = uporabnik.getUsername();
			seznamUporabnikov.add(imeUporabnika);
		}
		Collections.sort(seznamUporabnikov);
		uporabnikiOkno.removeAll();
		JLabel trenutnoPrijavljeni = new JLabel("Trenutno prijavljeni:");
		uporabnikiOkno.add(trenutnoPrijavljeni);
		
		for (String uporabnik : seznamUporabnikov) {
			JButton gumbUporabnik = new JButton(uporabnik);
			gumbUporabnik.setOpaque(false);
			gumbUporabnik.setContentAreaFilled(false);
			gumbUporabnik.setBorderPainted(false);
			uporabnikiOkno.add(filer);
			uporabnikiOkno.add(gumbUporabnik);
			gumbUporabnik.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					System.out.println("bu");
				}
			});

//			JLabel labelUporabnik = new JLabel(uporabnik);
//			uporabnikiOkno.add(filer);
//			uporabnikiOkno.add(labelUporabnik);
//			labelUporabnik.addMouseListener(this);
//			labelUporabnik.addMouseListener(new MouseAdapter() {
//				@Override
//				public void mouseClicked(MouseEvent dogodek) {
//					if (dogodek.getClickCount() == 2) {
//						System.out.println("bu");
//						//TODO Odpri, fokusiraj okno
//					}
//				}
//			});
			
		}	
		
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource()==this.gumbPrijava){
			jaz = poljeVzdevek.getText();
			App.vpisiMe(jaz);
			robot.aktiviraj();
		}
		if (e.getSource()==this.gumbOdjava){
			App.izpisiMe(jaz);
			robot.deaktiviraj();
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

	@Override
	public void mouseClicked(MouseEvent e) {
		if (e.getClickCount() == 2) {
			System.out.println("bu");
		}
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}


}
