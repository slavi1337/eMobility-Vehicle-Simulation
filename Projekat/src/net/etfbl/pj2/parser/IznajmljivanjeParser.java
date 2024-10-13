package net.etfbl.pj2.parser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import net.etfbl.pj2.izuzeci.NepoznazoVoziloIzuzetak;
import net.etfbl.pj2.izuzeci.ParsiranjeIzuzetak;
import net.etfbl.pj2.model.Iznajmljivanje;
import net.etfbl.pj2.model.KoordinatnoPolje;
import net.etfbl.pj2.vozila.Vozilo;

/**
 * Klasa zaduzena za parsiranje fajla za Iznajmljivanje vozila Provjerava sve
 * moguce greske tokom parsitanja i baca odgovarajuci izuzetak
 *
 * @author Slaviša Čovakušić
 */
public class IznajmljivanjeParser {

	private static final String imeFajla = "Ulazni podaci" + File.separator+ "PJ2 - projektni zadatak 2024 - Iznajmljivanja.csv";
	private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("d.M.yyyy HH:mm");
	private List<Vozilo> vozila; // Lista vozila iz VoziloParsera
	private List<Iznajmljivanje> iznajmljivanja = new ArrayList<>();

	public IznajmljivanjeParser(List<Vozilo> vozila) {
		this.vozila = vozila;
	}

	/**
	 * Parsira CSV fajl i kreira listu validnih iznajmljivanja. Provjerava
	 * ispravnost formata podataka i potencijalne konflikte kod iznajmljivanja istog
	 * vozila u isto vreme.
	 *
	 * @return Lista iznajmljivanja sortirana po datumu i vremenu
	 * @throws Throwable U slučaju greške prilikom čitanja ili parsiranja podataka
	 */
	public List<Iznajmljivanje> parsujIznajmljivanja() throws Throwable {
		System.out.println("\n\n");
		// cilj napraviti listu sortiranu po datumu, onako kako ce se izvrsavati
		// simulacija, ali da lista sadrzi samo validne
		// unose, i da se provjeri da li postoje ista vozila u isto vrijeme u
		// iznajmljivanjima

		try (BufferedReader br = new BufferedReader(new FileReader(imeFajla))) {
			br.readLine();
			String line;
			while ((line = br.readLine()) != null) {
				try {
					// U fajlu sa iznajmljivanjima voditi računa da se u istom terminu (vrijeme i
					// datum) isto prevozno sredstvo ne smije koristiti više od jednom. Ako ima više
					// iznajmljivanja za isti termin, simulirati i obraditi prvi iz fajla, a ostale
					// ignorisati uz poruke na konzoli. Na isti način obraditi linije koje nemaju
					// dovoljno podataka, pogrešne koordinate (van opsega), ....
					String[] values = line.split(",");
					if (values.length != 10) {
						throw new ParsiranjeIzuzetak("Neispravan broj polja u redu: " + line);
					}

					LocalDateTime vrijemePocetka;
					try {
						vrijemePocetka = LocalDateTime.parse(values[0].trim(), DATE_FORMATTER);
					} catch (Exception e) {
						throw new ParsiranjeIzuzetak("Pogresan format datuma: " + values[0]);
					}
					String korisnikIme = values[1].trim();
					String voziloId = values[2].trim();
					//Svako prevozno sredstvo iz liste iznajmljivanja mora postojati u fajlu prevoznih sredstava.
					vozila.stream().filter(v -> v.getId().equals(voziloId)).findFirst().orElseThrow(
							() -> new NepoznazoVoziloIzuzetak("Vozilo ID: " + voziloId + " ne postoji u listi vozila"));

					String pocetakXStr = values[3].trim();
					String pocetakYStr = values[4].trim();

					// Provjera da li su navodnici pravilno postavljeni
					if (!(pocetakXStr.startsWith("\"") && pocetakYStr.endsWith("\""))) {
						throw new ParsiranjeIzuzetak(
								"Nepravilno formatirane koordinate: " + pocetakXStr + ", " + pocetakYStr);
					}

					pocetakXStr = pocetakXStr.substring(1); // Uklanja prvi navodnik
					pocetakYStr = pocetakYStr.substring(0, pocetakYStr.length() - 1); // Uklanja poslednji navodnik

					int pocetakX = Integer.parseInt(pocetakXStr);
					int pocetakY = Integer.parseInt(pocetakYStr);

					if (pocetakX < 0 || pocetakX > 19 || pocetakY < 0 || pocetakY > 19) {
						throw new ParsiranjeIzuzetak("Koordinate su van opsega: " + pocetakX + ", " + pocetakY);
					}

					KoordinatnoPolje pocetak = new KoordinatnoPolje(pocetakX, pocetakY);

					String krajXStr = values[5].trim();
					String krajYStr = values[6].trim();

					if (!(krajXStr.startsWith("\"") && krajYStr.endsWith("\""))) {
						throw new ParsiranjeIzuzetak(
								"Nepravilno formatirane koordinate: " + krajXStr + ", " + krajYStr);
					}

					krajXStr = krajXStr.substring(1); // Uklanja prvi navodnik
					krajYStr = krajYStr.substring(0, krajYStr.length() - 1); // Uklanja poslednji navodnik

					int krajX = Integer.parseInt(krajXStr);
					int krajY = Integer.parseInt(krajYStr);

					if (krajX < 0 || krajX > 19 || krajY < 0 || krajY > 19) {
						throw new ParsiranjeIzuzetak("Koordinate su van opsega: " + krajX + ", " + krajY);
					}

					KoordinatnoPolje kraj = new KoordinatnoPolje(krajX, krajY);

					int trajanje = Integer.parseInt(values[7].trim());
					if (trajanje <= 0) {
						throw new ParsiranjeIzuzetak("Trajanje mora biti vece od nule: " + trajanje);
					}

					boolean jelPokvaren = parseBoolean(values[8].trim(), "kvar");
					boolean jelNaPromociji = parseBoolean(values[9].trim(), "promocija");

					// pretpostavka da nece biti iznajmljivanja bas precizna u sekundu, tako da
					// nisam u obriz uzeo trajanje posto u fajlovima nema nijedno da je blizu
					boolean postojiKonflikt = iznajmljivanja.stream().anyMatch(
							i -> i.getVoziloId().equals(voziloId) && i.getVrijemePocetka().equals(vrijemePocetka));

					if (postojiKonflikt) {
						throw new ParsiranjeIzuzetak("Konflikt za vozilo ID: " + voziloId + " u vrijeme: " + vrijemePocetka);
					}
					
					// Ukoliko korisnik sa istim imenom (id) iznajmljuje vec drugo vozilo u isto vrijeme
					// u slucaju da je moguce iznajmljivati vise vozila od strane istog korisnika staviti do 154. linije pod komentar
					boolean korisnikVecIznajmljuje = iznajmljivanja.stream()
	                        .anyMatch(i -> i.getKorisnik().getIme().equals(korisnikIme) && i.getVrijemePocetka().equals(vrijemePocetka));
	                
		            if (korisnikVecIznajmljuje) {
		            	throw new ParsiranjeIzuzetak("Konflikt za korisnika: " + korisnikIme + " vec iznajmljuje vozilo u vrijeme: " + vrijemePocetka);
		            }

					Iznajmljivanje iznajmljivanje = new Iznajmljivanje(korisnikIme, voziloId, pocetak, kraj,
							vrijemePocetka, vrijemePocetka.plusSeconds(trajanje), trajanje, jelNaPromociji,
							jelPokvaren);

					// System.out.println(iznajmljivanje);
					iznajmljivanja.add(iznajmljivanje);

				} catch (ParsiranjeIzuzetak | NepoznazoVoziloIzuzetak e) {
					System.err.println(e.getMessage());
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		iznajmljivanja = iznajmljivanja.stream()
				.sorted((i1, i2) -> i1.getVrijemePocetka().compareTo(i2.getVrijemePocetka()))
				.collect(Collectors.toList());

		System.out.println("\n\n");
		for (Iznajmljivanje i : iznajmljivanja) {
			System.out.println(i);
		}

		return iznajmljivanja;
	}

	private boolean parseBoolean(String value, String fieldName) throws ParsiranjeIzuzetak {
		if (value.equalsIgnoreCase("da")) {
			return true;
		} else if (value.equalsIgnoreCase("ne")) {
			return false;
		} else {
			throw new ParsiranjeIzuzetak("Pogresna vrijednost za " + fieldName + ": " + value);
		}
	}

	/*
	 * public static void main (String args[]) throws Throwable { VoziloPraser
	 * vp=new VoziloPraser(); List<Vozilo> lv=vp.parsujVozila();
	 * IznajmljivanjeParser ip=new IznajmljivanjeParser(lv); List<Iznajmljivanje>
	 * lz=ip.parsujIznajmljivanja();
	 * 
	 * System.out.println("\n\n"); for(Iznajmljivanje i:lz) System.out.println(i); }
	 */

}
