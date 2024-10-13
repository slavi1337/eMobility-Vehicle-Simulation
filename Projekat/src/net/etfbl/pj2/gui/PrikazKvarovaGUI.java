package net.etfbl.pj2.gui;

import java.awt.BorderLayout;
import java.time.format.DateTimeFormatter;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.WindowConstants;
import javax.swing.table.DefaultTableModel;

import net.etfbl.pj2.model.Iznajmljivanje;
import net.etfbl.pj2.model.Kvar;
import net.etfbl.pj2.vozila.ElektricniAutomobil;
import net.etfbl.pj2.vozila.ElektricniBicikl;
import net.etfbl.pj2.vozila.ElektricniTrotinet;
import net.etfbl.pj2.vozila.Vozilo;

/**
 * Klasa za GUI prikaz kvarova Prikaz se bazira na listi iznajmljivanja i listi
 * vozila, gdje se za svako prevozno sredstvo prikazuje ID, vrijeme kvara i opis
 * kvara.
 * 
 * @author Slaviša Čovakušić
 */
public class PrikazKvarovaGUI extends JFrame {

	private static final long serialVersionUID = 2626952550324819294L;

	private static final DateTimeFormatter FORMAT_DATUM_VRIJEME = DateTimeFormatter.ofPattern("d.M.yyyy HH:mm");

	/**
	 * Konstruktor klase PrikazKvarovaGUI koji inicijalizuje prozor i prikazuje
	 * kvarove.
	 *
	 * @param iznajmljivanja Lista iznajmljivanja koja sadrži informacije o
	 *                       kvarovima.
	 * @param listaVozila    Lista vozila koja sadrži prevozna sredstva.
	 */
	public PrikazKvarovaGUI(List<Iznajmljivanje> iznajmljivanja, List<Vozilo> listaVozila) {
		setTitle("Prikaz Kvarova");
		setSize(800, 600);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

		DefaultTableModel model = new DefaultTableModel(
				new Object[] { "Vrsta Prevoznog Sredstva", "ID", "Vrijeme", "Opis Kvara" }, 0);

		for (Iznajmljivanje iznajmljivanje : iznajmljivanja) {
			if (Boolean.TRUE.equals(iznajmljivanje.getJelPokvaren())) {
				Vozilo vozilo = listaVozila.stream().filter(v -> v.getId().equals(iznajmljivanje.getVoziloId()))
						.findFirst().orElse(null);

				if (vozilo != null) {
					String vrstaPrevoznogSredstva = vozilo.getClass().getSimpleName();
					String id = iznajmljivanje.getVoziloId();
					Kvar kvar = new Kvar("Pokvarena baterija", iznajmljivanje.getVrijemePocetka());

					if (vozilo instanceof ElektricniBicikl) {
						kvar.setOpis("Pukla guma");
					}
					if (vozilo instanceof ElektricniAutomobil) {
						kvar.setOpis("Kvar na ekranu");
					}

					if (vozilo instanceof ElektricniTrotinet) {
						kvar.setOpis("Pokvarena baterija");
					}

					model.addRow(new Object[] { vrstaPrevoznogSredstva, id,
							kvar.getDatumVremeKvara().format(FORMAT_DATUM_VRIJEME).toString(), kvar.getOpis() });
				}
			}
		}

		JTable kvaroviTable = new JTable(model);
		JScrollPane scrollPane = new JScrollPane(kvaroviTable);

		add(scrollPane, BorderLayout.CENTER);

		setVisible(true);
	}
}
