package net.etfbl.pj2.parser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.etfbl.pj2.izuzeci.DuplikatIzuzetak;
import net.etfbl.pj2.izuzeci.NepoznazoVoziloIzuzetak;
import net.etfbl.pj2.izuzeci.ParsiranjeIzuzetak;
import net.etfbl.pj2.vozila.ElektricniAutomobil;
import net.etfbl.pj2.vozila.ElektricniBicikl;
import net.etfbl.pj2.vozila.ElektricniTrotinet;
import net.etfbl.pj2.vozila.Vozilo;

/**
 * Klasa VoziloPraser (pogresno otkucano, trebalo biti VoziloParser, ali sam
 * kasno primijetio) sluzi da parsuje vozila iz fajla za vozila Provjerava sve
 * moguce greske tokom parsiranja i baca odgovarajuci izuzetak
 *
 * @author Slaviša Čovakušić
 */
public class VoziloPraser {

	private Set<String> idSet = new HashSet<>();
	private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("d.M.yyyy.");

	private static final String imeFajla = "Ulazni podaci" +File.separator +"PJ2 - projektni zadatak 2024 - Prevozna sredstva.csv";

	/**
	 * Parsira CSV fajl koji sadrži podatke o vozilima i vraća listu validnih
	 * vozila. Proverava ispravnost podataka kao što su ID, proizvodjac, model,
	 * datum nabavke i tip vozila. Takođe detektuje duplikate vozila i nepoznate
	 * vrste vozila.
	 *
	 * @return Lista vozila koja su uspešno parsirana.
	 * @throws Throwable U slučaju greške prilikom čitanja ili parsiranja podataka.
	 */
	public List<Vozilo> parsujVozila() throws Throwable {
		List<Vozilo> vozila = new ArrayList<>();

		try (BufferedReader br = new BufferedReader(new FileReader(imeFajla))) {
			String line;
			br.readLine();
			while ((line = br.readLine()) != null) {
				try {
					// U fajlovima mogu biti pogrešni podaci i program treba da ih ignoriše, tj.
					// zbog loših podataka (redova) ne smije se prekinuti izvršavanje programa. Npr.
					// mogu postojati dupli identifikatori prevoznog sredstva u fajlu sa prevoznim
					// sredstvima i tada se kreira samo prvo prevozno sredstvo, a svako sljedeće
					// duplo se ignoriše uz ispis poruke na konzolu.
					String[] col = line.split(",");
					if (col.length != 9) {
						throw new ParsiranjeIzuzetak("Neispravan broj polja u redu: " + line);
					}

					String id = col[0].trim();
					if (idSet.contains(id)) {
						throw new DuplikatIzuzetak("Dupli ID vozila: " + id);
					}

					String proizvodjac = col[1].trim();
					String model = col[2].trim();
					LocalDate datumNabavke = col[3].trim().isEmpty() ? null
							: LocalDate.parse(col[3].trim(), DATE_FORMATTER);
					double cijenaNabavke = col[4].isEmpty() ? 0.0 : Double.parseDouble(col[4].trim());
					if (col[4].isEmpty() || proizvodjac == "" || model == "" || id == "") {
						throw new ParsiranjeIzuzetak("Nedostaje obavezno polje!");
					}
					Integer domet = col[5].isEmpty() ? 0 : Integer.parseInt(col[5].trim());
					Integer maxBrzina = col[6].isEmpty() ? 0 : Integer.parseInt(col[6].trim());
					String opis = col[7].trim();
					String vrsta = col[8].trim().toLowerCase();
					if (col[8].isEmpty()) {
						throw new ParsiranjeIzuzetak("Vrsta je obavezno polje!");
					}

					Vozilo vozilo;
					switch (vrsta) {
					case "automobil":
						if (opis.isEmpty() || maxBrzina != 0.0 || domet!=0.0 || datumNabavke == null) {
							throw new ParsiranjeIzuzetak("Automobil mora imati opis i datum, a ne moze imati maksimalnu brzinu ili domet");
						}
						vozilo = new ElektricniAutomobil(id, cijenaNabavke, proizvodjac, model, datumNabavke, opis);
						break;
					case "bicikl":
						if (!opis.isEmpty() || maxBrzina != 0.0 || datumNabavke != null) {
							throw new ParsiranjeIzuzetak("Bicikl ne može imati opis,datum ili maksimalnu brzinu.");
						}
						vozilo = new ElektricniBicikl(id, cijenaNabavke, proizvodjac, model, domet);
						break;
					case "trotinet":
						if (!opis.isEmpty() || domet != 0.0 || datumNabavke != null) {
							throw new ParsiranjeIzuzetak("Trotinet ne može imati opis,datum ili domet.");
						}
						vozilo = new ElektricniTrotinet(id, cijenaNabavke, proizvodjac, model, maxBrzina);
						break;
					default:
						throw new NepoznazoVoziloIzuzetak("Nepoznata vrsta vozila: " + vrsta);
					}

					vozila.add(vozilo);
					idSet.add(id);
					System.out.println("Dodato vozilo: " + vozilo);
				} catch (DuplikatIzuzetak | NepoznazoVoziloIzuzetak | ParsiranjeIzuzetak e) {
					System.err.println(e.getMessage());
				} catch (NumberFormatException e) {
					System.err.println("Pogresan format broja" + e.getMessage());
				} catch (Exception e) {
					System.err.println(e.getMessage());
				}

			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return vozila;
	}

	/*
	 * public static void main(String args[]) throws Throwable { VoziloPraser vp=new
	 * VoziloPraser(); List<Vozilo> vozila= vp.parsujVozila(); for(Vozilo vo:vozila)
	 * { System.out.println(vo.getId()+ " "+ vo.getProizvodjac() + vo.getModel()+
	 * vo.getCijenaNabavke()+vo.getTrenutniNivoBaterije()); } }
	 */
}
