package net.etfbl.pj2.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;

import net.etfbl.pj2.model.Iznajmljivanje;
import net.etfbl.pj2.model.KoordinatnoPolje;
import net.etfbl.pj2.racun.Racun;
import net.etfbl.pj2.vozila.ElektricniAutomobil;
import net.etfbl.pj2.vozila.ElektricniBicikl;
import net.etfbl.pj2.vozila.ElektricniTrotinet;
import net.etfbl.pj2.vozila.Vozilo;

/**
 * Klasa MapaGUI predstavlja grafički korisnicki interfejs za prikaz mape i
 * simulacije u njoj imate mogucnost pristupa razlicitim izvjestajima
 *
 * @author Slaviša Čovakušić
 */
public class MapaGUI extends JFrame {
	private static final long serialVersionUID = 5067407768920534468L;
	private static final int SIZE = 20; // za 20x20 mapu
	private JLabel[][] grid = new JLabel[SIZE][SIZE]; // grid za mapu
	private JLabel dateTimeLabel;
	private static final DateTimeFormatter FORMAT_DATUM_VRIJEME = DateTimeFormatter.ofPattern("d.M.yyyy HH:mm");
	private List<Vozilo> listaVozila;
	private List<Iznajmljivanje> iznajmljivanja;
	private List<Racun> racuni = new ArrayList<>();

	@SuppressWarnings("unchecked")
	private List<Vozilo>[][] vozilaNaPolju = new ArrayList[SIZE][SIZE];

	private static final String autoFajl = "Serijalizacija" + File.separator + "Automobil.ser";
	private static final String biciklFajl = "Serijalizacija" + File.separator + "Bicikl.ser";
	private static final String trotinetFajl = "Serijalizacija" + File.separator + "Trotinet.ser";

	/**
	 * Konstruktor za kreiranje GUI interfejsa mape sa zadatim vozilima i
	 * iznajmljivanjima
	 *
	 * @param listaVozila    lista vozila koja će se prikazivati na mapi
	 * @param iznajmljivanja lista iznajmljivanja koja se simuliraju na mapi
	 */
	public MapaGUI(List<Vozilo> listaVozila, List<Iznajmljivanje> iznajmljivanja) {
		this.listaVozila = listaVozila;
		this.iznajmljivanja = iznajmljivanja;

		// racunanje svih racuna zbog ostalih formi
		for (Iznajmljivanje i : this.iznajmljivanja) {
			racuni.add(new Racun(i, getVoziloById(i.getVoziloId())));
		}

		setTitle("ePJ2 mapa grada");
		setLayout(new BorderLayout());
		setSize(Toolkit.getDefaultToolkit().getScreenSize()); // fullscreen
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

		JPanel gridPanel = new JPanel(new GridLayout(SIZE, SIZE));
		gridPanel.setPreferredSize(new Dimension(800, 800));
		add(gridPanel, BorderLayout.CENTER);

		for (int i = 0; i < SIZE; i++) {
			for (int j = 0; j < SIZE; j++) {
				grid[i][j] = new JLabel();
				grid[i][j].setOpaque(true);
				vozilaNaPolju[i][j] = new ArrayList<>();
				// bojenje mape
				this.resetujPolje(new KoordinatnoPolje(i, j));

				grid[i][j].setBorder(BorderFactory.createLineBorder(Color.GRAY));
				gridPanel.add(grid[i][j]);
			}
		}

		dateTimeLabel = new JLabel("Datum i vrijeme: ");
		dateTimeLabel.setHorizontalAlignment(SwingConstants.CENTER);
		dateTimeLabel.setFont(new Font("Calibri", Font.BOLD, 20));

		JButton prikazPrevoznihSredstavaDugme = new JButton("Prevozna Sredstva");
		prikazPrevoznihSredstavaDugme.addActionListener(e -> new PrikazPrevoznihSredstavaGUI(listaVozila));

		JButton prikazKvarovaDugme = new JButton("Kvarovi");
		prikazKvarovaDugme.addActionListener(e -> new PrikazKvarovaGUI(iznajmljivanja, listaVozila));

		JButton sumarniIzvjestajDugme = new JButton("Sumarni izvjestaj");
		sumarniIzvjestajDugme.addActionListener(e -> new PrikazSumarnogIzvjestajaGUI(racuni));

		JButton dnevniIzvjestajDugme = new JButton("Dnevni izvjestaj");
		dnevniIzvjestajDugme.addActionListener(e -> new PrikazDnevnogIzvjestajaGUI(racuni));

		JButton deserijalizacijaDugme = new JButton("Deserijalizacija");
		deserijalizacijaDugme.addActionListener(e -> {
			Vozilo automobil = deserijalizujVozilo(autoFajl);
			Vozilo bicikl = deserijalizujVozilo(biciklFajl);
			Vozilo trotinet = deserijalizujVozilo(trotinetFajl);

			double zaradaAutomobil = deserijalizujZaradu(autoFajl);
			double zaradaBicikl = deserijalizujZaradu(biciklFajl);
			double zaradaTrotinet = deserijalizujZaradu(trotinetFajl);

			new PrikazDeserijalizacijeGUI(automobil, zaradaAutomobil, bicikl, zaradaBicikl, trotinet, zaradaTrotinet);
		});

		JPanel gornjiPanel = new JPanel(new BorderLayout());

		gornjiPanel.add(dateTimeLabel, BorderLayout.EAST);

		JPanel dugmadPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		dugmadPanel.add(prikazPrevoznihSredstavaDugme);
		dugmadPanel.add(prikazKvarovaDugme);
		dugmadPanel.add(sumarniIzvjestajDugme);
		dugmadPanel.add(dnevniIzvjestajDugme);

		dugmadPanel.add(deserijalizacijaDugme);
		gornjiPanel.add(dugmadPanel, BorderLayout.WEST);

		add(gornjiPanel, BorderLayout.NORTH);

		setVisible(true);
	}

	/**
	 * Pronalazi vozilo na osnovu njegovog jedinstvenog ID-a.
	 *
	 * @param voziloId jedinstveni identifikator vozila
	 * @return objekat tipa Vozilo koji odgovara zadatom ID-u
	 * @throws IllegalArgumentException ako vozilo sa zadatim ID-om nije pronadjeno
	 */
	public Vozilo getVoziloById(String voziloId) {
		for (Vozilo vozilo : listaVozila) {
			if (vozilo.getId().equals(voziloId)) {
				return vozilo;
			}
		}
		throw new IllegalArgumentException("Vozilo sa ID: " + voziloId + " nije pronadjeno");
	}

	public void updatujPolje(KoordinatnoPolje polje, List<Vozilo> vehicleList) {
		String text = vehicleList.stream()
				.map(v -> v.getId() + String.format(" %.0f", v.getTrenutniNivoBaterije()) + "%")
				.collect(Collectors.joining(" "));

		JLabel label = getFieldLabel(polje);
		label.setText(text);
		label.setForeground(new Color(0, 255, 13));
		label.setBackground(Color.BLACK);
	}

	/**
	 * Vraća listu vozila koja se nalaze na zadatom koordinatnom polju.
	 *
	 * @param polje Koordinatno polje za koje treba dobiti vozila
	 * @return Lista vozila na zadatom polju
	 */
	public List<Vozilo> getVozilaNaPolju(KoordinatnoPolje polje) {
		return vozilaNaPolju[polje.getX()][polje.getY()];
	}

	/**
	 * Vraca labelu sa polja
	 * 
	 * @param polje
	 * @return labelu na polju
	 */
	public JLabel getFieldLabel(KoordinatnoPolje polje) {
		int x = polje.getX();
		int y = polje.getY();
		return grid[x][y];
	}

	/**
	 * Resetuje boju polja na pocetnu
	 *
	 * @param polje polje za koje se resetuje boja
	 */
	public synchronized void resetujPolje(KoordinatnoPolje polje) {
		int x = polje.getX();
		int y = polje.getY();
		if (x >= 5 && x < 15 && y >= 5 && y < 15) {
			grid[x][y].setBackground(new Color(173, 216, 230));
		} else {
			grid[x][y].setBackground(new Color(250, 200, 125));
		}
	}

	/**
	 * Updejtuje vrijeme na labeli
	 *
	 * @param dateTime vrijeme na koje se labela updejtuje
	 */
	public void updateDateTimeLabel(LocalDateTime dateTime) {
		dateTimeLabel.setText("Datum i vrijeme: " + dateTime.format(FORMAT_DATUM_VRIJEME));
	}

	/**
	 * Prikaz "KRAJ" animacije na kraju simulacije
	 */
	public void prikaziKraj() {
		for (int i = 0; i < SIZE; i++) {
			for (int j = 0; j < SIZE; j++) {
				grid[i][j].setText("");
			}
		}

		String endMessage = "KRAJ";
		int length = endMessage.length();

		for (int step = 0; step < length; step++) {
			int row = SIZE / 2;
			int col = (SIZE - length) / 2 + step;

			grid[row][col].setText(Character.toString(endMessage.charAt(step)));
			grid[row][col].setForeground(Color.RED);
			grid[row][col].setFont(new Font("Calibri", Font.BOLD, 40));

			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		for (int i = 0; i < 15; i++) {
			for (int step = 0; step < length; step++) {
				int row = SIZE / 2;
				int col = (SIZE - length) / 2 + step;
				grid[row][col].setForeground(Color.BLACK);
			}

			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			for (int step = 0; step < length; step++) {
				int row = SIZE / 2;
				int col = (SIZE - length) / 2 + step;
				grid[row][col].setForeground(Color.RED);
			}

			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Serijalizuje najprofitabilnija vozila u fajlove
	 */
	public void serijalizujNajprofitabilnijaVozila() {
		Map<String, Double> zaradaPoVozilu = new HashMap<>();

		for (Racun racun : racuni) {
			String voziloId = racun.getIznajmljivanje().getVoziloId();
			zaradaPoVozilu.put(voziloId, zaradaPoVozilu.getOrDefault(voziloId, 0.0) + racun.getUkupniIznos());
		}

		Vozilo najprofitabilnijiAutomobil = null;
		Vozilo najprofitabilnijiBicikl = null;
		Vozilo najprofitabilnijiTrotinet = null;

		double maxZaradaAutomobil = 0;
		double maxZaradaBicikl = 0;
		double maxZaradaTrotinet = 0;

		for (Vozilo vozilo : listaVozila) {
			double zarada = zaradaPoVozilu.getOrDefault(vozilo.getId(), 0.0);

			if (vozilo instanceof ElektricniAutomobil && zarada > maxZaradaAutomobil) {
				maxZaradaAutomobil = zarada;
				najprofitabilnijiAutomobil = vozilo;
			} else if (vozilo instanceof ElektricniBicikl && zarada > maxZaradaBicikl) {
				maxZaradaBicikl = zarada;
				najprofitabilnijiBicikl = vozilo;
			} else if (vozilo instanceof ElektricniTrotinet && zarada > maxZaradaTrotinet) {
				maxZaradaTrotinet = zarada;
				najprofitabilnijiTrotinet = vozilo;
			}
		}

		// Serijalizacija najprofitabilnijih vozila u binarne fajlove
		serijalizujVozilo(autoFajl, najprofitabilnijiAutomobil, maxZaradaAutomobil);
		serijalizujVozilo(biciklFajl, najprofitabilnijiBicikl, maxZaradaBicikl);
		serijalizujVozilo(trotinetFajl, najprofitabilnijiTrotinet, maxZaradaTrotinet);

	}

	/**
	 * Serijalizuje vozilo sa zadatom zaradom u datoteku.
	 *
	 * @param imeFajla putanja datoteke u koju će se serijalizovati vozilo
	 * @param vozilo   vozilo koje se serijalizuje
	 * @param zarada   zarada vozila koja se takođe serijalizuje
	 */
	private void serijalizujVozilo(String imeFajla, Vozilo vozilo, double zarada) {
		try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(imeFajla))) {
			oos.writeObject(vozilo);
			oos.writeDouble(zarada);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Deserijalizuje vozilo iz datoteke.
	 *
	 * @param imeFajla putanja datoteke iz koje se deserijalizuje vozilo
	 * @return deserijalizovano vozilo
	 */
	private Vozilo deserijalizujVozilo(String imeFajla) {
		try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(imeFajla))) {
			return (Vozilo) ois.readObject();
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Deserijalizuje zaradu vozila iz datoteke.
	 *
	 * @param imeFajla putanja datoteke iz koje se deserijalizuje zarada
	 * @return deserijalizovana zarada
	 */
	private synchronized double deserijalizujZaradu(String imeFajla) {
		try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(imeFajla))) {
			// prvo preskok vozila., pa citanje zarade
			ois.readObject();
			return ois.readDouble();
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
			return 0.0;
		}
	}

//	/**
//	 * Ažurira mapu sa novim vozilom na zadatoj lokaciji i prikazuje nivo baterije.
//	 *
//	 * @param polje        koordinatno polje gde se vozilo nalazi
//	 * @param voziloId     ID vozila
//	 * @param nivoBaterije nivo baterije vozila
//	 */
//	public synchronized void updateMap(KoordinatnoPolje polje, String voziloId, double nivoBaterije) {
//		// Dobavlja ili kreira listu vozila za to polje
//		List<String> vozila = vozilaNaPolju.getOrDefault(polje, new ArrayList<>());
//
//		// Provjeri da li vozilo već postoji na tom polju (da izbjegneš duplikate)
//		if (!vozila.stream().anyMatch(v -> v.startsWith(voziloId))) {
//			vozila.add(voziloId + " (" + (int) nivoBaterije + "%)");
//		}
//
//		// Ažuriraj mapu sa vozilima za to polje
//		vozilaNaPolju.put(polje, vozila);
//
//		// Ažuriraj GUI na sigurnom threadu
//		SwingUtilities.invokeLater(() -> updateGridLabel(polje));
//	}

//	/**
//	 * Briše prikaz vozila sa zadatogproslog polja na mapi.
//	 *
//	 * @param polje    koordinatno polje gde se vozilo nalazi
//	 * @param voziloId ID vozila
//	 */
//	public synchronized void clearField(KoordinatnoPolje polje, String voziloId) {
//		List<String> vozila = vozilaNaPolju.get(polje);
//
//		if (vozila != null) {
//			// Ukloni vozilo sa polja
//			vozila.removeIf(v -> v.startsWith(voziloId));
//
//			// Ako nema više vozila na tom polju, ukloni listu
//			if (vozila.isEmpty()) {
//				vozilaNaPolju.remove(polje);
//				SwingUtilities.invokeLater(() -> resetujPolje(polje));
//			} else {
//				// Ako ima još vozila, ažuriraj GUI
//				SwingUtilities.invokeLater(() -> updateGridLabel(polje));
//			}
//		}
//	}

//	public void updateGridLabel(KoordinatnoPolje polje) {
//		int x = polje.getX();
//		int y = polje.getY();
//		grid[x][y].setForeground(new Color(0, 255, 13)); // Zeleni tekst za vozila
//		grid[x][y].setBackground(Color.BLACK); // Pozadina polja
//	}

}
