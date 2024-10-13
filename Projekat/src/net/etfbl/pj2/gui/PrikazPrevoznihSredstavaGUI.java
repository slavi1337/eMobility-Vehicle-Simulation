package net.etfbl.pj2.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.time.format.DateTimeFormatter;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import net.etfbl.pj2.vozila.ElektricniAutomobil;
import net.etfbl.pj2.vozila.ElektricniBicikl;
import net.etfbl.pj2.vozila.ElektricniTrotinet;
import net.etfbl.pj2.vozila.Vozilo;

/**
 * Klasa PrikazPrevoznihSredstavaGUI prikazuje prevozna sredstva (automobile,
 * trotinete, bicikle) u tabelama sa relevantnim podacima. GUI omogućava pregled
 * različitih vozila i njihovih atributa.
 * 
 * @author Slaviša Čovakušić
 */
public class PrikazPrevoznihSredstavaGUI extends JFrame {

	private static final long serialVersionUID = 8660012497153668642L;

	private static final DateTimeFormatter FORMAT_DATUM = DateTimeFormatter.ofPattern("d.M.yyyy");

	/**
	 * Konstruktor koji kreira novi prozor za prikaz prevoznih sredstava. Prikazuju
	 * se automobili, trotineti i bicikli u tabelama.
	 *
	 * @param vozila lista vozila koja će biti prikazana u tabelama
	 */
	public PrikazPrevoznihSredstavaGUI(List<Vozilo> vozila) {
		setTitle("Prikaz Prevoznih Sredstava");
		setLayout(new BorderLayout());
		setSize(800, 600);
		setLocationRelativeTo(null);

		JPanel tablePanel = new JPanel();
		tablePanel.setLayout(new BoxLayout(tablePanel, BoxLayout.Y_AXIS));

		DefaultTableModel carModel = new DefaultTableModel(new Object[] { "ID", "Proizvođač", "Model", "Cijena nabavke",
				"Trenutni Nivo Baterije", "Datum Nabavke", "Opis" }, 0);
		DefaultTableModel scooterModel = new DefaultTableModel(new Object[] { "ID", "Proizvođač", "Model",
				"Cijena nabavke", "Trenutni Nivo Baterije", "Maksimalna Brzina" }, 0);
		DefaultTableModel bicycleModel = new DefaultTableModel(
				new Object[] { "ID", "Proizvođač", "Model", "Cijena nabavke", "Trenutni Nivo Baterije", "Domet" }, 0);

		for (Vozilo vozilo : vozila) {
			if (vozilo instanceof ElektricniAutomobil) {
				ElektricniAutomobil auto = (ElektricniAutomobil) vozilo;
				carModel.addRow(new Object[] { auto.getId(), auto.getProizvodjac(), auto.getModel(),
						auto.getCijenaNabavke(), auto.getTrenutniNivoBaterije(),
						auto.getDatumNabavke().format(FORMAT_DATUM), auto.getOpis() });
			} else if (vozilo instanceof ElektricniTrotinet) {
				ElektricniTrotinet trotinet = (ElektricniTrotinet) vozilo;
				scooterModel.addRow(new Object[] { trotinet.getId(), trotinet.getProizvodjac(), trotinet.getModel(),
						trotinet.getCijenaNabavke(), trotinet.getTrenutniNivoBaterije(),
						trotinet.getMaksimalnaBrzina() });
			} else if (vozilo instanceof ElektricniBicikl) {
				ElektricniBicikl bicikl = (ElektricniBicikl) vozilo;
				bicycleModel.addRow(new Object[] { bicikl.getId(), bicikl.getProizvodjac(), bicikl.getModel(),
						bicikl.getCijenaNabavke(), bicikl.getTrenutniNivoBaterije(),
						bicikl.getDometSaJednimPunjenjem() });
			}
		}

		JTable carTable = new JTable(carModel);
		carTable.setBackground(Color.RED);
		JTable scooterTable = new JTable(scooterModel);
		scooterTable.setBackground(Color.BLUE);
		JTable bicycleTable = new JTable(bicycleModel);
		bicycleTable.setBackground(Color.GREEN);

		adjustColumnWidths(carTable);
		adjustColumnWidths(scooterTable);
		adjustColumnWidths(bicycleTable);

		JLabel carLabel = new JLabel("Automobili", SwingConstants.CENTER);
		JLabel scooterLabel = new JLabel("Trotineti", SwingConstants.CENTER);
		JLabel bicycleLabel = new JLabel("Bicikli", SwingConstants.CENTER);

		tablePanel.add(carLabel, BorderLayout.CENTER);
		tablePanel.add(new JScrollPane(carTable));
		tablePanel.add(Box.createRigidArea(new Dimension(0, 10)));
		tablePanel.add(scooterLabel, BorderLayout.CENTER);
		tablePanel.add(new JScrollPane(scooterTable));
		tablePanel.add(Box.createRigidArea(new Dimension(0, 10)));
		tablePanel.add(bicycleLabel, BorderLayout.CENTER);
		tablePanel.add(new JScrollPane(bicycleTable));

		add(tablePanel, BorderLayout.CENTER);

		setVisible(true);
	}

	private void adjustColumnWidths(JTable table) {
		TableColumn column;
		for (int i = 0; i < table.getColumnCount(); i++) {
			column = table.getColumnModel().getColumn(i);
			column.setPreferredWidth(500);
		}
	}
}
