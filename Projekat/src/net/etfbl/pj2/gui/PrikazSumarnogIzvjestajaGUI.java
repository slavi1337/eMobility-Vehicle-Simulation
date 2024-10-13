package net.etfbl.pj2.gui;

import java.awt.BorderLayout;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.WindowConstants;
import javax.swing.table.DefaultTableModel;

import net.etfbl.pj2.racun.Racun;

/**
 * Klasa PrikazSumarnogIzvjestajaGUI prikazuje sumarni izvještaj računa u
 * tabelarnom formatu. Prikazani podaci uključuju ukupne prihode, popuste,
 * promocije, troškove održavanja i porez.
 * 
 * @author Slaviša Čovakušić
 */
public class PrikazSumarnogIzvjestajaGUI extends JFrame {

	private static final long serialVersionUID = -6497552715324809370L;

	/**
	 * Konstruktor koji kreira GUI za prikaz sumarnog izvještaja na osnovu liste
	 * računa.
	 *
	 * @param racuni lista računa iz koje se izvode podaci za sumarni izvještaj
	 */
	public PrikazSumarnogIzvjestajaGUI(List<Racun> racuni) {
		setTitle("Sumarni Izvještaj");
		setSize(600, 300);
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

		// Inicijalizacija sumarnog izvještaja
		Double ukupanPrihod = 0.0;
		Double ukupanPopust = 0.0;
		Double ukupnoPromocije = 0.0;
		Double ukupanIznosUskiDeo = 0.0;
		Double ukupanIznosSirokiDeo = 0.0;
		Double ukupanIznosZaPopravke = 0.0;

		for (Racun racun : racuni) {
			ukupanPrihod += racun.getUkupniIznos();
			ukupanPopust += racun.getPopust();
			ukupnoPromocije += racun.getPromocija();
			if (racun.getJelUCentru()) {
				ukupanIznosUskiDeo += racun.getUkupniIznos();
			} else {
				ukupanIznosSirokiDeo += racun.getUkupniIznos();
			}
			ukupanIznosZaPopravke += racun.getIznosZaPopravku();
		}

		Double ukupanIznosOdrzavanje = ukupanPrihod * 0.2;
		Double ukupniTroskoviKompanije = ukupanIznosOdrzavanje;
		Double ukupanPorez = (ukupanPrihod - ukupanIznosOdrzavanje - ukupanIznosZaPopravke - ukupniTroskoviKompanije)
				* 0.1;

		Object[][] podaci = { { "Ukupan prihod", String.format("%.2f", ukupanPrihod) },
				{ "Ukupan popust", String.format("%.2f", ukupanPopust) },
				{ "Ukupno promocije", String.format("%.2f", ukupnoPromocije) },
				{ "Ukupan iznos vožnji u užem dijelu grada", String.format("%.2f", ukupanIznosUskiDeo) },
				{ "Ukupan iznos vožnji u širem dijelu grada", String.format("%.2f", ukupanIznosSirokiDeo) },
				{ "Ukupan iznos za održavanje", String.format("%.2f", ukupanIznosOdrzavanje) },
				{ "Ukupan iznos za popravke kvarova", String.format("%.2f", ukupanIznosZaPopravke) },
				{ "Ukupni troškovi kompanije", String.format("%.2f", ukupniTroskoviKompanije) },
				{ "Ukupan porez", String.format("%.2f", ukupanPorez) } };

		String[] kolone = { "Stavka", "Iznos" };

		// Kreiranje tabele
		JTable tabela = new JTable(new DefaultTableModel(podaci, kolone));
		JScrollPane scrollPane = new JScrollPane(tabela);
		add(scrollPane, BorderLayout.CENTER);

		setVisible(true);
	}
}
