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

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.BorderFactory;
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
	private Boolean mojStatus;
	public JPanel uporabnikiOkno; // Prikaže gumbe dosegljivih uporabnikov
	public String jaz; // Moj vzdevek
	public Map<String, ZasebniPogovor> slovarZasebni; // Slovar odprtih zasebnih pogovorov
	public Robotek robot;
	private Color barvaAktivna;
	private Color barvaNeaktivna;

	public ChatFrame() {
		super();
		setTitle("Chatty chat");
		Container pane = this.getContentPane();
		pane.setLayout(new GridBagLayout());

		this.slovarZasebni = new HashMap<String, ZasebniPogovor>();
		this.setMojStatus(false);
		this.barvaNeaktivna = new Color(220, 220, 220); // Svetlo siva
		Color barvaOkna = new Color(82, 153, 188); // Modra
		this.barvaAktivna = new Color(162, 207, 229); // Svetlo modra
		pane.setBackground(barvaOkna); // Določi barvo okna

		// Zgornje okno za vpis vzdevka, prijavo in odjavo
		JPanel zgornjeOkno = new JPanel();
		zgornjeOkno.setBackground(barvaOkna);
		FlowLayout lajoutmenedzer = new FlowLayout(FlowLayout.LEFT);
		zgornjeOkno.setLayout(lajoutmenedzer);
		GridBagConstraints zgornjeOknoConstraint = new GridBagConstraints();
		zgornjeOknoConstraint.gridx = 0;
		zgornjeOknoConstraint.gridy = 0;
		zgornjeOknoConstraint.gridwidth = 2; // Razteza se čez 2 stolpca
		zgornjeOknoConstraint.fill = GridBagConstraints.BOTH;
		pane.add(zgornjeOkno, zgornjeOknoConstraint);

		// Elementi zgornjega okna
		JLabel vnesiVzdevek = new JLabel("Vzdevek:");
		this.poljeVzdevek = new JTextField(System.getProperty("user.name"), 15);
		this.gumbPrijava = new JButton("Prijava");
		this.gumbPrijava.addActionListener(this);
		this.gumbOdjava = new JButton("Odjava");
		this.gumbOdjava.addActionListener(this);
		zgornjeOkno.add(vnesiVzdevek);
		zgornjeOkno.add(poljeVzdevek);
		zgornjeOkno.add(gumbPrijava);
		zgornjeOkno.add(gumbOdjava);

		// Okno dosegljivih uporabnikov
		this.uporabnikiOkno = new JPanel();
		this.uporabnikiOkno.setBackground(new Color(242, 179, 86)); // Oranžno ozadje
		this.uporabnikiOkno.setMinimumSize(new Dimension(140, 100));
		JLabel trenutnoDosegljivi = new JLabel("Trenutno dosegljivi:");
		this.uporabnikiOkno.add(trenutnoDosegljivi);
		this.uporabnikiOkno.setLayout(new BoxLayout(this.uporabnikiOkno, BoxLayout.Y_AXIS)); // Izpisuje uporabnike
																								// navpično
		JScrollPane drsnikUporabniki = new JScrollPane(uporabnikiOkno);
		GridBagConstraints drsnikUporabnikiConstraint = new GridBagConstraints();
		drsnikUporabnikiConstraint.gridx = 1;
		drsnikUporabnikiConstraint.gridy = 1;
		drsnikUporabnikiConstraint.fill = GridBagConstraints.BOTH;
		drsnikUporabnikiConstraint.weightx = 1;
		drsnikUporabnikiConstraint.weighty = 1;
		pane.add(drsnikUporabniki, drsnikUporabnikiConstraint);

		// Prostor za izpisovanje sporočil
		this.output = new JTextArea(20, 40); // Število vrstic in stolpcev
		this.output.setPreferredSize(new Dimension(500, 100));
		this.output.setEditable(false);
		this.output.setLineWrap(true);
		this.output.setWrapStyleWord(true);
		this.output.setBackground(barvaNeaktivna);
		JScrollPane drsnik = new JScrollPane(output);
		GridBagConstraints drsnikConstraint = new GridBagConstraints();
		drsnikConstraint.gridx = 0;
		drsnikConstraint.gridy = 1;
		drsnikConstraint.fill = GridBagConstraints.BOTH;
		drsnikConstraint.weightx = 5;
		drsnikConstraint.weighty = 1;
		pane.add(drsnik, drsnikConstraint);

		// Prostor za vpisovanje sporočil
		this.input = new JTextField(40);
		GridBagConstraints inputConstraint = new GridBagConstraints();
		inputConstraint.gridx = 0;
		inputConstraint.gridy = 2;
		inputConstraint.weightx = 5;
		inputConstraint.weighty = 0;
		inputConstraint.fill = GridBagConstraints.BOTH;
		pane.add(input, inputConstraint);
		input.addKeyListener(this);

		// Gumb za pošiljanje sporočil
		this.gumbPoslji = new JButton("Pošlji");
		GridBagConstraints posljiConstraint = new GridBagConstraints();
		posljiConstraint.gridx = 1;
		posljiConstraint.gridy = 2;
		pane.add(gumbPoslji, posljiConstraint);
		gumbPoslji.addActionListener(this);

		// Na začetku je vse deaktivirano, dokler se uporabnik ne prijavi
		gumbOdjava.setEnabled(false);
		gumbPrijava.setEnabled(true);
		input.setEnabled(false);
		gumbPoslji.setEnabled(false);

		// Obnašanje okna (fokus in zapiranje)
		addWindowListener(new WindowAdapter() {
			public void windowOpened(WindowEvent e) {
				input.requestFocusInWindow();
			}

			@Override
			public void windowClosing(WindowEvent e) {
				if (mojStatus) {
					robot.deaktiviraj();
					KomunikacijaServer.izpisiMe(jaz);
				}
				for (Map.Entry<String, ZasebniPogovor> element : slovarZasebni.entrySet()) {
					element.getValue().dispose(); // Zapre vse zasebne pogovore
				}
			}
		});
	}

	public void addMessage(String person, String message) {
		String chat = this.output.getText();
		this.output.setText(chat + person + ": " + message + "\n");
	}

	public Boolean getMojStatus() {
		return mojStatus;
	}

	public void setMojStatus(Boolean mojStatus) {
		this.mojStatus = mojStatus;
	}

	public void prikaziUporabnike(List<String> seznamUporabnikov) { // Naredi gumbe uporabnikov
		uporabnikiOkno.removeAll();
		JLabel trenutnoDosegljivi = new JLabel("Trenutno dosegljivi:");
		uporabnikiOkno.add(trenutnoDosegljivi);

		Collections.sort(seznamUporabnikov); // Uredi po abecedi
		for (String uporabnik : seznamUporabnikov) {
			JButton gumbUporabnik = new JButton(uporabnik);
			gumbUporabnik.setOpaque(false);
			gumbUporabnik.setContentAreaFilled(false);
			gumbUporabnik.setBorder(BorderFactory.createLineBorder(Color.BLACK, 10));
			gumbUporabnik.setBorderPainted(false);
			uporabnikiOkno.add(gumbUporabnik);
			gumbUporabnik.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if (slovarZasebni.containsKey(uporabnik)) {
						slovarZasebni.get(uporabnik).requestFocus();
					} else {
						ZasebniPogovor pogovor = new ZasebniPogovor(uporabnik, jaz);
						pogovor.pack();
						pogovor.setMinimumSize(new Dimension(100, 100));
						pogovor.setVisible(true);
						slovarZasebni.put(uporabnik, pogovor);
					}
				}
			});
			uporabnikiOkno.revalidate(); // to invoke the layout manager
			uporabnikiOkno.repaint();
		}
	}

	private void aktivirajPogovore() {
		this.robot = new Robotek(this); // Potrebno narediti nov timer vsakič ko se vpišeš
		robot.aktiviraj();
		gumbOdjava.setEnabled(true);
		gumbPrijava.setEnabled(false);
		input.setEnabled(true);
		gumbPoslji.setEnabled(true);
		output.setBackground(barvaAktivna);
		Set<String> mnozicaZasebnih = slovarZasebni.keySet();
		for (String posameznik : mnozicaZasebnih) {
			slovarZasebni.get(posameznik).aktivirajPogovor();
		}
	}

	private void deaktivirajPogovore() {
		robot.deaktiviraj();
		gumbOdjava.setEnabled(false);
		gumbPrijava.setEnabled(true);
		input.setEnabled(false);
		gumbPoslji.setEnabled(false);
		output.setBackground(barvaNeaktivna);
		Set<String> mnozicaZasebnih = slovarZasebni.keySet();
		for (String posameznik : mnozicaZasebnih) {
			slovarZasebni.get(posameznik).deaktivirajPogovor();
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == this.gumbPrijava) {
			jaz = poljeVzdevek.getText();
			poljeVzdevek.setEditable(false);
			KomunikacijaServer.vpisiMe(jaz);
			aktivirajPogovore();
		}

		if (e.getSource() == this.gumbOdjava) {
			KomunikacijaServer.izpisiMe(jaz);
			poljeVzdevek.setEditable(true);
			deaktivirajPogovore();
		}

		if (e.getSource() == this.gumbPoslji) {
			KomunikacijaServer.posljiJavno(jaz, this.input.getText());
			this.addMessage(jaz, this.input.getText());
			this.input.setText("");
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {
		if (e.getSource() == this.input) {
			if (e.getKeyChar() == '\n') {
				KomunikacijaServer.posljiJavno(jaz, this.input.getText());
				this.addMessage(jaz, this.input.getText());
				this.input.setText("");
			}
		}
	}

	@Override
	public void keyPressed(KeyEvent e) {
		// Auto-generated method stub
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// Auto-generated method stub
	}

}