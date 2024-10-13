package net.etfbl.pj2.gui;

import java.awt.BorderLayout;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.WindowConstants;
import javax.swing.table.DefaultTableModel;

import net.etfbl.pj2.racun.Racun;

/**
 * Klasa PrikazDnevnogIzvjestajaGUI prikazuje dnevni izvještaj računa u
 * tabelarnom formatu. Prikazani podaci uključuju ukupne prihode, popuste,
 * promocije i troškove vožnji u užem i širem dijelu grada.
 * 
 * @author Slaviša Čovakušić
 */
public class PrikazDnevnogIzvjestajaGUI extends JFrame {

	private static final long serialVersionUID = 8431821187135520249L;

	private static final DateTimeFormatter FORMAT_DATUM = DateTimeFormatter.ofPattern("d.M.yyyy");

	/**
	 * Konstruktor koji kreira GUI za prikaz dnevnog izvještaja na osnovu liste
	 * računa.
	 *
	 * @param racuni lista računa iz koje se izvode podaci za dnevni izvještaj
	 */
	public PrikazDnevnogIzvjestajaGUI(List<Racun> racuni) {
		setTitle("Dnevni Izvještaj");
		setSize(800, 400);
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

		// Grupisanje računa po datumu
		Map<LocalDate, Double[]> dnevniIzvjestaji = new HashMap<>();

		for (Racun racun : racuni) {
			LocalDate datum = racun.getIznajmljivanje().getVrijemePocetka().toLocalDate();
			Double[] podaci = dnevniIzvjestaji.getOrDefault(datum, new Double[6]);

			Double iznosUskiDeo = 0.0;
			Double iznosSirokiDeo = 0.0;

			if (racun.getJelUCentru()) {
				iznosUskiDeo += racun.getUkupniIznos();
			} else {
				iznosSirokiDeo += racun.getUkupniIznos();
			}

			podaci[0] = podaci[0] != null ? podaci[0] + racun.getUkupniIznos() : racun.getUkupniIznos();
			podaci[1] = podaci[1] != null ? podaci[1] + racun.getPopust() : racun.getPopust();
			podaci[2] = podaci[2] != null ? podaci[2] + racun.getPromocija() : racun.getPromocija();
			podaci[3] = podaci[3] != null ? podaci[3] + iznosUskiDeo : iznosUskiDeo;
			podaci[4] = podaci[4] != null ? podaci[4] + iznosSirokiDeo : iznosSirokiDeo;
			podaci[5] = podaci[5] != null ? podaci[5] + racun.getIznosZaPopravku() : racun.getIznosZaPopravku();

			dnevniIzvjestaji.put(datum, podaci);
		}

		// Podaci za tabelu
		Object[][] podaci = new Object[dnevniIzvjestaji.size()][7];
		int i = 0;
		for (Map.Entry<LocalDate, Double[]> entry : dnevniIzvjestaji.entrySet()) {
			LocalDate datum = entry.getKey();
			Double[] vrijednosti = entry.getValue();
			podaci[i][0] = datum.format(FORMAT_DATUM);
			podaci[i][1] = String.format("%.2f", vrijednosti[0]);
			podaci[i][2] = String.format("%.2f", vrijednosti[1]);
			podaci[i][3] = String.format("%.2f", vrijednosti[2]);
			podaci[i][4] = String.format("%.2f", vrijednosti[3]);
			podaci[i][5] = String.format("%.2f", vrijednosti[4]);
			podaci[i][6] = String.format("%.2f", vrijednosti[5]);
			i++;
		}

		String[] kolone = { "Datum", "Ukupan prihod", "Ukupan popust", "Ukupno promocije",
				"Ukupan iznos vožnji u užem dijelu", "Ukupan iznos vožnji u širem dijelu", "Ukupan iznos za popravke" };

		JTable tabela = new JTable(new DefaultTableModel(podaci, kolone));
		JScrollPane scrollPane = new JScrollPane(tabela);
		add(scrollPane, BorderLayout.CENTER);

		setVisible(true);
	}
}
