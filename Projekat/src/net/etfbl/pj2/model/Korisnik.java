package net.etfbl.pj2.model;

/**
 * Klasa Korisnik predstavlja korisnika sistema, koji ima ime, ID dokumenta i
 * broj vozačke dozvole (slucajno generisani podaci)
 * 
 * @author Slaviša Čovakušić
 */
public class Korisnik {

	private String ime;
	private String idDokumenta;
	private String brojVozacke;

	public Korisnik() {

	}

	/**
	 * Konstruktor koji prima ime korisnika i automatski generiše ID dokumenta i
	 * broj vozačke.
	 *
	 * @param ime Ime korisnika.
	 */
	public Korisnik(String ime) {
		this.setIme(ime);

		// Vrijednosti za dokumenta korisnika mogu imati proizvoljne vrijednosti. Voditi
		// računa da polja postoje u modelu. Nije obavezno praviti posebno listu
		// korisnika (kao što ima za vozila), i objekti korisnika se mogu ponavljati
		setIdDokumenta(HelperFunkcije.generisiIdDokumenta());
		setBrojVozacke(HelperFunkcije.generisiIdDokumenta());
	}

	public void generisiIdKorisnika() {
		setIdDokumenta(HelperFunkcije.generisiIdDokumenta());
		setBrojVozacke(HelperFunkcije.generisiIdDokumenta());
	}

	/**
	 * @return the ime
	 */
	public String getIme() {
		return ime;
	}

	/**
	 * @param ime the ime to set
	 */
	public void setIme(String ime) {
		this.ime = ime;
	}

	/**
	 * @return the idDokumenta
	 */
	public String getIdDokumenta() {
		return idDokumenta;
	}

	/**
	 * @param idDokumenta the idDokumenta to set
	 */
	public void setIdDokumenta(String idDokumenta) {
		this.idDokumenta = idDokumenta;
	}

	/**
	 * @return the brojVozacke
	 */
	public String getBrojVozacke() {
		return brojVozacke;
	}

	/**
	 * @param brojVozacke the brojVozacke to set
	 */
	public void setBrojVozacke(String brojVozacke) {
		this.brojVozacke = brojVozacke;
	}

	@Override
	public String toString() {
		return "Korisnik [ime=" + ime + ", idDokumenta=" + idDokumenta + ", brojVozacke=" + brojVozacke + "]";
	}

}
