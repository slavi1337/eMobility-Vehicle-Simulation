package net.etfbl.pj2.racun;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.time.format.DateTimeFormatter;
import java.util.Properties;

import net.etfbl.pj2.izuzeci.NepoznazoVoziloIzuzetak;
import net.etfbl.pj2.model.Iznajmljivanje;
import net.etfbl.pj2.model.KoordinatnoPolje;
import net.etfbl.pj2.model.Kvar;
import net.etfbl.pj2.vozila.ElektricniAutomobil;
import net.etfbl.pj2.vozila.ElektricniBicikl;
import net.etfbl.pj2.vozila.ElektricniTrotinet;
import net.etfbl.pj2.vozila.Vozilo;

/**
 * Klasa koja predstavlja račun kreiran za svako iznajmljivanje vozila Račun
 * sadrži informacije o osnovnoj cijeni, udaljenosti, popustima, promocijama i
 * kvarovima... Generide tekstualni fajl sa detaljima o iznajmljivanjuw
 *
 * @author Slaviša Čovakušić
 */
public class Racun {

	private Double ukupniIznos;
	private Double osnovnaCijena = 0.0;
	private Double udaljenost = 0.0;
	private Double udaljenostUska = 0.0;
	private Double udaljenostSiroka = 0.0;
	private Double iznos;
	private Double popust = 0.0;
	private Double promocija = 0.0;
	private Iznajmljivanje iznajmljivanje;
	private Vozilo vozilo;
	private Double krajnjiNivoBaterije;
	private Double pocetniNivoBaterije;
	private Kvar kvar;
	private static final DateTimeFormatter FORMAT_DATUM_VRIJEME = DateTimeFormatter.ofPattern("d.M.yyyy HH.mm");
	private static final DateTimeFormatter FORMAT_DATUM_VRIJEME_SEKUNDE = DateTimeFormatter
			.ofPattern("d.M.yyyy HH:mm:ss");
	private Boolean jelUCentru = true;

	private Double iznosZaPopravku = 0.0;
	
	private static final double koeficijentAuto=0.07;
	private static final double koeficijentBicikl=0.04;
	private static final double koeficijentTrotinet=0.02;
	
	/**
	 * Konstruktor klase Racun Inicijalizuje račun sa osnovnim parametrima
	 * iznajmljivanja i vozila Racuna ukupan iznos, popuste i promocije na osnovu
	 * podataka iz fajla `config.properties`
	 *
	 * @param iznajmljivanje - objekat koji predstavlja iznajmljivanje vozila
	 * @param vozilo         - vozilo koje se iznajmljuje
	 */
	public Racun(Iznajmljivanje iznajmljivanje, Vozilo vozilo) {
		this.iznajmljivanje = iznajmljivanje;
		this.vozilo = vozilo;

		Properties properties = new Properties();
		try (InputStream inp = getClass().getClassLoader().getResourceAsStream("config.properties")) {
			properties.load(inp);
			try {
				if (this.iznajmljivanje.getJelPokvaren()) {
					osnovnaCijena = 0.0;
				} else {
					if (vozilo instanceof ElektricniAutomobil) {
						osnovnaCijena = Double.parseDouble(properties.getProperty("CAR_UNIT_PRICE"));
					} else if (vozilo instanceof ElektricniBicikl) {
						osnovnaCijena = Double.parseDouble(properties.getProperty("BIKE_UNIT_PRICE"));
					} else if (vozilo instanceof ElektricniTrotinet) {
						osnovnaCijena = Double.parseDouble(properties.getProperty("SCOOTER_UNIT_PRICE"));
					} else {
						osnovnaCijena = 0.0;
						throw new NepoznazoVoziloIzuzetak("Vozilo nepoznatog tipa");
					}
					// System.out.println(osnovnaCijena);
					osnovnaCijena *= this.iznajmljivanje.getTrajanje();
				}

			} catch (NepoznazoVoziloIzuzetak ex1) {
				System.err.println("Nepoznato vozilo: " + ex1.getMessage());
			}
			udaljenostUska = Double.parseDouble(properties.getProperty("DISTANCE_NARROW"));
			udaljenostSiroka = Double.parseDouble(properties.getProperty("DISTANCE_WIDE"));
			for (KoordinatnoPolje polje : this.iznajmljivanje.getPutanja()) {
				if (polje.getX() < 5 || polje.getX() > 14 || polje.getY() < 5 || polje.getY() > 14) {
					jelUCentru = false;
				}
			}
			if (jelUCentru) {
				udaljenost = osnovnaCijena * udaljenostUska;
			} else {
				udaljenost = osnovnaCijena * udaljenostSiroka;
			}

			if (this.iznajmljivanje.getJelNaPopustu()) {
				popust = osnovnaCijena * Double.parseDouble(properties.getProperty("DISCOUNT"));
			}
			if (this.iznajmljivanje.getJelNaPromociji()) {
				promocija = osnovnaCijena * Double.parseDouble(properties.getProperty("DISCOUNT_PROM"));
			}
			// na primjeru pogresno (dvosmisleno) napisano, ovako cu raditi
			iznos = udaljenost;
			this.ukupniIznos = iznos - popust - promocija;

			// za izvjestaje
			if (this.iznajmljivanje.getJelPokvaren()) {
				if (this.vozilo instanceof ElektricniAutomobil) {
					iznosZaPopravku = vozilo.getCijenaNabavke() * koeficijentAuto;
				} else if (this.vozilo instanceof ElektricniBicikl) {
					iznosZaPopravku = vozilo.getCijenaNabavke() * koeficijentBicikl;
				} else if (this.vozilo instanceof ElektricniTrotinet) {
					iznosZaPopravku = vozilo.getCijenaNabavke() * koeficijentTrotinet;
				}
			}
		} catch (IOException ex) {
			ex.printStackTrace();
		}

	}

	/**
	 * Pravi racun i ime fajla
	 */
	public void napraviRacun() {
		Properties properties = new Properties();
		try (InputStream inp = getClass().getClassLoader().getResourceAsStream("config.properties")) {
			properties.load(inp);
			String imeFajla = properties.getProperty("RACUNI_PUTANJA") + File.separator + iznajmljivanje.getVoziloId() + " "
					+ iznajmljivanje.getKorisnik().getIme() + "-"
					+ iznajmljivanje.getVrijemeKraja().format(FORMAT_DATUM_VRIJEME) + ".txt";
			upisiTekstURacun(imeFajla);
		} catch (IOException ex) {
			System.err.println("Greska sa config fajlom " + ex.getMessage());
		}
	}

	/**
	 * Upis sadržaja računa u .txt fajl na lokaciji Racuni/
	 *
	 * @param imeFajla - naziv fajla u koji se piše
	 */
	private void upisiTekstURacun(String imeFajla) {
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(imeFajla))) {
			// Korisnik
			writer.write(iznajmljivanje.getKorisnik() + "\n");
			writer.write("====================================\n");

			// ID vozila i nivoi baterije
			writer.write("ID vozila: " + vozilo.getId() + "\n");
			writer.write("Pocetni % baterije: " + pocetniNivoBaterije.intValue() + "%" + "\n");
			writer.write("Krajnji % baterije: " + krajnjiNivoBaterije.intValue() + "%" + "\n");

			// Vreme početka i kraja sa sekundama
			writer.write(
					"Početno vreme: " + iznajmljivanje.getVrijemePocetka().format(FORMAT_DATUM_VRIJEME_SEKUNDE) + "\n");
			writer.write(
					"Krajnje vreme: " + iznajmljivanje.getVrijemeKraja().format(FORMAT_DATUM_VRIJEME_SEKUNDE) + "\n");
			writer.write("====================================\n");

			// Ostale informacije
			writer.write("Početna lokacija: " + iznajmljivanje.getPocetak() + "\n");
			writer.write("Krajnja lokacija: " + iznajmljivanje.getKraj() + "\n");
			writer.write("Dužina: " + iznajmljivanje.getPutanja().size() + "\n");

			writer.write("Trajanje: " + iznajmljivanje.getTrajanje() + "\n");
			writer.write("Prolazak kroz siri dio grada?" + (jelUCentru ? "Ne\n" : "Da\n"));

			writer.write(
					"Osnovna cijena (UNIT_PRICE * VRIJEME_TRAJANJA): " + String.format("%.2f", osnovnaCijena) + "\n");
			writer.write("Iznos (Osnovna_cijena * DIST): " + iznos + "\n");
			writer.write("Popust: " + String.format("%.2f", popust) + "\n");
			writer.write("Promocija: " + String.format("%.2f", promocija) + "\n");

			writer.write("====================================\n");
			writer.write("Ukupni iznos: " + ukupniIznos + "\n");

			// Informacije o kvaru
			if (iznajmljivanje.getJelPokvaren()) {
				kvar = new Kvar("Pokvarena baterija", iznajmljivanje.getVrijemePocetka());
				writer.write(kvar.toString() + "\n");
			} else {
				writer.write("Kvar: Nema" + "\n");
			}

			System.out.println("Racun uspjesno napravljen: " + imeFajla);
		} catch (IOException e) {
			System.err.println("Greska sa fajlom za upis racuna " + e.getMessage());
		}

	}

	/**
	 * @return the ukupniIznos
	 */
	public Double getUkupniIznos() {
		return ukupniIznos;
	}

	/**
	 * @param ukupniIznos the ukupniIznos to set
	 */
	public void setUkupniIznos(Double ukupniIznos) {
		this.ukupniIznos = ukupniIznos;
	}

	/**
	 * @return the osnovnaCijena
	 */
	public Double getOsnovnaCijena() {
		return osnovnaCijena;
	}

	/**
	 * @param osnovnaCijena the osnovnaCijena to set
	 */
	public void setOsnovnaCijena(Double osnovnaCijena) {
		this.osnovnaCijena = osnovnaCijena;
	}

	/**
	 * @return the udaljenost
	 */
	public Double getUdaljenost() {
		return udaljenost;
	}

	/**
	 * @param udaljenost the udaljenost to set
	 */
	public void setUdaljenost(Double udaljenost) {
		this.udaljenost = udaljenost;
	}

	/**
	 * @return the udaljenostUska
	 */
	public Double getUdaljenostUska() {
		return udaljenostUska;
	}

	/**
	 * @param udaljenostUska the udaljenostUska to set
	 */
	public void setUdaljenostUska(Double udaljenostUska) {
		this.udaljenostUska = udaljenostUska;
	}

	/**
	 * @return the udaljenostSiroka
	 */
	public Double getUdaljenostSiroka() {
		return udaljenostSiroka;
	}

	/**
	 * @param udaljenostSiroka the udaljenostSiroka to set
	 */
	public void setUdaljenostSiroka(Double udaljenostSiroka) {
		this.udaljenostSiroka = udaljenostSiroka;
	}

	/**
	 * @return the iznos
	 */
	public Double getIznos() {
		return iznos;
	}

	/**
	 * @param iznos the iznos to set
	 */
	public void setIznos(Double iznos) {
		this.iznos = iznos;
	}

	/**
	 * @return the popust
	 */
	public Double getPopust() {
		return popust;
	}

	/**
	 * @param popust the popust to set
	 */
	public void setPopust(Double popust) {
		this.popust = popust;
	}

	/**
	 * @return the promocija
	 */
	public Double getPromocija() {
		return promocija;
	}

	/**
	 * @param promocija the promocija to set
	 */
	public void setPromocija(Double promocija) {
		this.promocija = promocija;
	}

	/**
	 * @return the iznajmljivanje
	 */
	public Iznajmljivanje getIznajmljivanje() {
		return iznajmljivanje;
	}

	/**
	 * @param iznajmljivanje the iznajmljivanje to set
	 */
	public void setIznajmljivanje(Iznajmljivanje iznajmljivanje) {
		this.iznajmljivanje = iznajmljivanje;
	}

	/**
	 * @return the vozilo
	 */
	public Vozilo getVozilo() {
		return vozilo;
	}

	/**
	 * @param vozilo the vozilo to set
	 */
	public void setVozilo(Vozilo vozilo) {
		this.vozilo = vozilo;
	}

	/**
	 * @return the krajnjiNivoBaterije
	 */
	public Double getKrajnjiNivoBaterije() {
		return krajnjiNivoBaterije;
	}

	/**
	 * @param krajnjiNivoBaterije the krajnjiNivoBaterije to set
	 */
	public void setKrajnjiNivoBaterije(Double krajnjiNivoBaterije) {
		this.krajnjiNivoBaterije = krajnjiNivoBaterije;
	}

	/**
	 * @return the pocetniNivoBaterije
	 */
	public Double getPocetniNivoBaterije() {
		return pocetniNivoBaterije;
	}

	/**
	 * @param pocetniNivoBaterije the pocetniNivoBaterije to set
	 */
	public void setPocetniNivoBaterije(Double pocetniNivoBaterije) {
		this.pocetniNivoBaterije = pocetniNivoBaterije;
	}

	/**
	 * @return the kvar
	 */
	public Kvar getKvar() {
		return kvar;
	}

	/**
	 * @param kvar the kvar to set
	 */
	public void setKvar(Kvar kvar) {
		this.kvar = kvar;
	}

	/**
	 * @return the formatDatumVrijeme
	 */
	public static DateTimeFormatter getFormatDatumVrijeme() {
		return FORMAT_DATUM_VRIJEME;
	}

	/**
	 * @return the formatDatumVrijemeSekunde
	 */
	public static DateTimeFormatter getFormatDatumVrijemeSekunde() {
		return FORMAT_DATUM_VRIJEME_SEKUNDE;
	}

	/**
	 * @return the jelUCentru
	 */
	public Boolean getJelUCentru() {
		return jelUCentru;
	}

	/**
	 * @param jelUCentru the jelUCentru to set
	 */
	public void setJelUCentru(Boolean jelUCentru) {
		this.jelUCentru = jelUCentru;
	}

	public Double getIznosZaPopravku() {
		return iznosZaPopravku;
	}

}
