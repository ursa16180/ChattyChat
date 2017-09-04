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
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class ChatFrame extends JFrame implements ActionListener, KeyListener {

	private static final long serialVersionUID = 1L;
	private JTextArea output; // Izpisana sporočila
	private JTextField input; // Za vnos novih sporočil
	private JTextField poljeVzdevek;
	private JButton gumbPrijava;
	private JButton gumbOdjava;
	private JButton gumbPoslji;
	public JPanel uporabnikiOkno; // Izpisuje uporabnike
	public String jaz; // Moj vzdevek
	public Map<String, ZasebniPogovor> slovarZasebni; // Slovar odprtih zasebnih pogovorov
	public Robotek robot;
	

	public ChatFrame() {
		super();
		setTitle("Chatty chat");
		Container pane = this.getContentPane();
		pane.setLayout(new GridBagLayout());
		this.robot = new Robotek(this);
		this.slovarZasebni = new HashMap<String, ZasebniPogovor>();

		// Zgornje okno za vpis vzdevka, prijavo in odjavo
		JPanel zgornjeOkno = new JPanel();
		FlowLayout lajoutmenedzer = new FlowLayout(FlowLayout.LEFT);
		zgornjeOkno.setLayout(lajoutmenedzer);
		GridBagConstraints zgornjeOknoConstraint = new GridBagConstraints();
		zgornjeOknoConstraint.gridx = 0;
		zgornjeOknoConstraint.gridy = 0;
		zgornjeOknoConstraint.gridwidth = 2;
		zgornjeOknoConstraint.fill = GridBagConstraints.BOTH;
		pane.add(zgornjeOkno, zgornjeOknoConstraint);

		// Elementi zgornjega okna
		JLabel vnesiVzdevek = new JLabel("Vzdevek:");
		// jaz = new String(System.getProperty("user.name"));
		poljeVzdevek = new JTextField(System.getProperty("user.name"), 15);
		// jaz = poljeVzdevek.getText()
		this.gumbPrijava = new JButton("Prijava");
		this.gumbPrijava.addActionListener(this);
		this.gumbOdjava = new JButton("Odjava");
		this.gumbOdjava.addActionListener(this);
		zgornjeOkno.add(vnesiVzdevek);
		zgornjeOkno.add(poljeVzdevek);
		zgornjeOkno.add(gumbPrijava);
		zgornjeOkno.add(gumbOdjava);

		// Seznam prijavljenih uporabnikov
		this.uporabnikiOkno = new JPanel();
		this.uporabnikiOkno.setBackground(Color.blue);
		// this.uporabnikiOkno.setPrefferedSize(new Dimesnion))
		JLabel trenutnoPrijavljeni = new JLabel("Trenutno prijavljeni:");
		this.uporabnikiOkno.add(trenutnoPrijavljeni);
		this.uporabnikiOkno.setLayout(new BoxLayout(this.uporabnikiOkno, BoxLayout.Y_AXIS)); // Izpisuje uporabnike
																								// navpično
		/*
		 * GridBagConstraints uporabnikiOknoConstraint = new GridBagConstraints();
		 * uporabnikiOknoConstraint.gridx = 1; uporabnikiOknoConstraint.gridy = 1;
		 * uporabnikiOknoConstraint.fill = GridBagConstraints.BOTH;
		 * pane.add(uporabnikiOkno, uporabnikiOknoConstraint);
		 */
		JScrollPane drsnikUporabniki = new JScrollPane(uporabnikiOkno);
		GridBagConstraints drsnikUporabnikiConstraint = new GridBagConstraints();
		drsnikUporabnikiConstraint.gridx = 1;
		drsnikUporabnikiConstraint.gridy = 1;
		drsnikUporabnikiConstraint.fill = GridBagConstraints.BOTH;
		drsnikUporabnikiConstraint.weightx = 1;
		drsnikUporabnikiConstraint.weighty = 1;
		pane.add(drsnikUporabniki, drsnikUporabnikiConstraint);

		// Prostor za izpisovanje sporočil
		this.output = new JTextArea(20, 40);
		this.output.setEditable(false);

		JScrollPane drsnik = new JScrollPane(output);
		GridBagConstraints drsnikConstraint = new GridBagConstraints();
		drsnikConstraint.gridx = 0;
		drsnikConstraint.gridy = 1;
		drsnikConstraint.fill = GridBagConstraints.BOTH;
		drsnikConstraint.weightx = 5;
		drsnikConstraint.weighty = 1;
		pane.add(drsnik, drsnikConstraint);
		// this.output.setMinimumSize(new Dimension(100,100));//TODO

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

			@Override
			public void windowClosing(WindowEvent e) {
				robot.deaktiviraj();// TODO če aktiven!!!
				App.izpisiMe(jaz);// TODO če vpisana!!!
			}
		});
	}

	public void addMessage(String person, String message) {
		String chat = this.output.getText();
		this.output.setText(chat + person + ": " + message + "\n");
	}

	public void prikaziUporabnike(List<String> seznamUporabnikov) { // Naredi gumbe uporabnikov
		uporabnikiOkno.removeAll();
		JLabel trenutnoPrijavljeni = new JLabel("Trenutno prijavljeni:");
		uporabnikiOkno.add(trenutnoPrijavljeni);
		

		Collections.sort(seznamUporabnikov); //Uredi po abecedi
		for (String uporabnik : seznamUporabnikov) {
			JButton gumbUporabnik = new JButton(uporabnik);
			gumbUporabnik.setOpaque(false);
			gumbUporabnik.setContentAreaFilled(false);
			gumbUporabnik.setBorder(BorderFactory.createLineBorder(Color.BLACK, 10));
			gumbUporabnik.setBorderPainted(false);
			uporabnikiOkno.add(gumbUporabnik);
			gumbUporabnik.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					if (e.getSource() == gumbUporabnik) {
					if (slovarZasebni.containsKey(uporabnik)) {
						slovarZasebni.get(uporabnik).requestFocus();
						// slovarZasebni.get(uporabnik).toFront();
						// slovarZasebni.get(uporabnik).repaint(); //A to res rabmo? //TODO

					}
					{
						ZasebniPogovor pogovor = new ZasebniPogovor(uporabnik, jaz);
						pogovor.pack();
						pogovor.setVisible(true);
						slovarZasebni.put(uporabnik, pogovor);
					}}
				}
			});

			// JLabel labelUporabnik = new JLabel(uporabnik);
			// uporabnikiOkno.add(filer);
			// uporabnikiOkno.add(labelUporabnik);
			// labelUporabnik.addMouseListener(this);
			// labelUporabnik.addMouseListener(new MouseAdapter() {
			// @Override
			// public void mouseClicked(MouseEvent dogodek) {
			// if (dogodek.getClickCount() == 2) {
			// System.out.println("bu");
			// //TODO Odpri, fokusiraj okno
			// }
			// }
			// });

		}

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == this.gumbPrijava) {
			jaz = poljeVzdevek.getText();
			App.vpisiMe(jaz);
			robot.aktiviraj();
		}
		if (e.getSource() == this.gumbOdjava) {
			App.izpisiMe(jaz);
			robot.deaktiviraj();
		}
		if (e.getSource() == this.gumbPoslji) {
			App.poslji(jaz, this.input.getText());
			this.addMessage(jaz, this.input.getText());
			this.input.setText("");
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