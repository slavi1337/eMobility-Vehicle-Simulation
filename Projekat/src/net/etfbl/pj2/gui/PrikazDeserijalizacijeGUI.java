package net.etfbl.pj2.gui;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;
import javax.swing.table.DefaultTableModel;

import net.etfbl.pj2.vozila.ElektricniAutomobil;
import net.etfbl.pj2.vozila.ElektricniBicikl;
import net.etfbl.pj2.vozila.ElektricniTrotinet;
import net.etfbl.pj2.vozila.Vozilo;

/**
 * Klasa PrikazDeserijalizacijeGUI prikazuje najprofitabilnija vozila u tabeli
 * sa relevantnim podacima o svakom vozilu. Prikaz sadrži tri tabele za
 * automobile, bicikle i trotinete, sa podacima o njihovoj zaradi.
 * 
 * @author Slaviša Čovakušić
 */
public class PrikazDeserijalizacijeGUI extends JFrame {

	private static final long serialVersionUID = 1L;

	/**
	 * Konstruktor klase PrikazDeserijalizacijeGUI koji inicijalizuje GUI sa
	 * tabelama za prikaz najprofitabilnijih vozila.
	 *
	 * @param automobil       Najprofitabilniji automobil.
	 * @param zaradaAutomobil Ukupna zarada od automobila.
	 * @param bicikl          Najprofitabilniji bicikl.
	 * @param zaradaBicikl    Ukupna zarada od bicikla.
	 * @param trotinet        Najprofitabilniji trotinet.
	 * @param zaradaTrotinet  Ukupna zarada od trotineta.
	 */
	public PrikazDeserijalizacijeGUI(Vozilo automobil, double zaradaAutomobil, Vozilo bicikl, double zaradaBicikl,
			Vozilo trotinet, double zaradaTrotinet) {
		setTitle("Prikaz Najprofitabilnijih Vozila");
		setLayout(new GridLayout(3, 1)); // Tri tabele, jedna ispod druge
		setSize(600, 600);
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

		// Kreiraj i dodaj tabele
		add(createTablePanel("Najprofitabilniji Automobil", automobil, zaradaAutomobil));
		add(createTablePanel("Najprofitabilniji Bicikl", bicikl, zaradaBicikl));
		add(createTablePanel("Najprofitabilniji Trotinet", trotinet, zaradaTrotinet));

		setVisible(true);
	}

	private JPanel createTablePanel(String title, Vozilo vozilo, double zarada) {
		JPanel panel = new JPanel(new BorderLayout());
		JLabel label = new JLabel(title + " (Ukupan Iznos: " + zarada + ")", SwingConstants.CENTER);
		label.setFont(new Font("Serif", Font.BOLD, 16));

		String[] columnNames = getColumnNamesForVozilo(vozilo);
		Object[][] data = getDataForVozilo(vozilo);

		JTable table = new JTable(new DefaultTableModel(data, columnNames));
		JScrollPane scrollPane = new JScrollPane(table);

		panel.add(label, BorderLayout.NORTH);
		panel.add(scrollPane, BorderLayout.CENTER);

		return panel;
	}

	private String[] getColumnNamesForVozilo(Vozilo vozilo) {
		if (vozilo instanceof ElektricniAutomobil) {
			return new String[] { "ID", "Cijena Nabavke", "Proizvođač", "Model", "Trenutni Nivo Baterije",
					"Datum Nabavke", "Opis" };
		} else if (vozilo instanceof ElektricniBicikl) {
			return new String[] { "ID", "Cijena Nabavke", "Proizvođač", "Model", "Trenutni Nivo Baterije",
					"Datum Nabavke", "Domet" };
		} else if (vozilo instanceof ElektricniTrotinet) {
			return new String[] { "ID", "Cijena Nabavke", "Proizvođač", "Model", "Trenutni Nivo Baterije",
					"Datum Nabavke", "Maksimalna Brzina" };
		} else {
			return new String[] {};
		}
	}

	private Object[][] getDataForVozilo(Vozilo vozilo) {
		if (vozilo instanceof ElektricniAutomobil) {
			ElektricniAutomobil auto = (ElektricniAutomobil) vozilo;
			return new Object[][] { { auto.getId(), auto.getCijenaNabavke(), auto.getProizvodjac(), auto.getModel(),
					auto.getTrenutniNivoBaterije(), auto.getDatumNabavke(), auto.getOpis() } };
		} else if (vozilo instanceof ElektricniBicikl) {
			ElektricniBicikl bicikl = (ElektricniBicikl) vozilo;
			return new Object[][] { { bicikl.getId(), bicikl.getCijenaNabavke(), bicikl.getProizvodjac(),
					bicikl.getModel(), bicikl.getTrenutniNivoBaterije(), null, bicikl.getDometSaJednimPunjenjem() } };
		} else if (vozilo instanceof ElektricniTrotinet) {
			ElektricniTrotinet trotinet = (ElektricniTrotinet) vozilo;
			return new Object[][] { { trotinet.getId(), trotinet.getCijenaNabavke(), trotinet.getProizvodjac(),
					trotinet.getModel(), trotinet.getTrenutniNivoBaterije(), null, trotinet.getMaksimalnaBrzina() } };
		} else {
			return new Object[][] {};
		}
	}
}
