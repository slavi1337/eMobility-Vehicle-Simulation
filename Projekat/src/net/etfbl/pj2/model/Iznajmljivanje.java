package net.etfbl.pj2.model;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Predstavlja jedno iznajmljivanje vozila Koristi se prilikom parsiranja
 * iznajmljivanja iz fajla lista iznajmljivanja...
 *
 *@author Slaviša Čovakušić
 */
public class Iznajmljivanje {
	private Korisnik korisnik;
	private String voziloId;
	private KoordinatnoPolje pocetak;
	private KoordinatnoPolje kraj;
	private List<KoordinatnoPolje> putanja;
	private LocalDateTime vrijemePocetka;
	private LocalDateTime vrijemeKraja;
	private Integer trajanje; // u sekundama
	private Boolean jelNaPromociji;
	private Boolean jelNaPopustu;
	private Boolean jelPokvaren;
	private static Integer redniBrIznajmljivanja = 0;

	/**
	 * Konstruktor koji inicijalizuje iznajmljivanje sa svim potrebnim
	 * informacijama.
	 *
	 * @param korisnikIme    ime korisnika koji iznajmljuje vozilo
	 * @param voziloId       ID vozila koje je iznajmljeno
	 * @param pocetak        početna koordinata
	 * @param kraj           krajnja koordinata
	 * @param vrijemePocetka vreme početka iznajmljivanja
	 * @param vrijemeKraja   vreme završetka iznajmljivanja
	 * @param trajanje       trajanje iznajmljivanja u sekundama
	 * @param jelNaPromociji da li je vozilo na promociji
	 * @param jelPokvaren    da li je vozilo pokvareno
	 */
	public Iznajmljivanje(String korisnikIme, String voziloId, KoordinatnoPolje pocetak, KoordinatnoPolje kraj,
			LocalDateTime vrijemePocetka, LocalDateTime vrijemeKraja, Integer trajanje, Boolean jelNaPromociji,
			Boolean jelPokvaren) {

		this.korisnik = new Korisnik(korisnikIme);
		this.voziloId = voziloId;
		this.pocetak = pocetak;
		this.kraj = kraj;
		this.putanja = HelperFunkcije.napraviPutanju(pocetak, kraj);
		this.vrijemePocetka = vrijemePocetka;
		this.vrijemeKraja = vrijemeKraja;
		this.trajanje = trajanje;
		this.jelNaPromociji = jelNaPromociji;
		this.jelPokvaren = jelPokvaren;
		Iznajmljivanje.redniBrIznajmljivanja++;
		if (Iznajmljivanje.redniBrIznajmljivanja % 10 == 0) {
			jelNaPopustu = true;
		} else {
			jelNaPopustu = false;
		}

	}

	/**
	 * @return the korisnik
	 */
	public Korisnik getKorisnik() {
		return korisnik;
	}

	/**
	 * @param korisnik the korisnik to set
	 */
	public void setKorisnik(Korisnik korisnik) {
		this.korisnik = korisnik;
	}

	/**
	 * @return the voziloId
	 */
	public String getVoziloId() {
		return voziloId;
	}

	/**
	 * @param voziloId the voziloId to set
	 */
	public void setVoziloId(String voziloId) {
		this.voziloId = voziloId;
	}

	/**
	 * @return the pocetak
	 */
	public KoordinatnoPolje getPocetak() {
		return pocetak;
	}

	/**
	 * @param pocetak the pocetak to set
	 */
	public void setPocetak(KoordinatnoPolje pocetak) {
		this.pocetak = pocetak;
	}

	/**
	 * @return the kraj
	 */
	public KoordinatnoPolje getKraj() {
		return kraj;
	}

	/**
	 * @param kraj the kraj to set
	 */
	public void setKraj(KoordinatnoPolje kraj) {
		this.kraj = kraj;
	}

	/**
	 * @return the putanja
	 */
	public List<KoordinatnoPolje> getPutanja() {
		return putanja;
	}

	/**
	 * Postavlja listu koordinata koje čine putanju vozila na iznajmljivanju.
	 *
	 * @param putanja lista koordinata putanje
	 */
	public void setPutanja(List<KoordinatnoPolje> putanja) {
		this.putanja = putanja;
	}

	/**
	 * @return the vrijemePocetka
	 */
	public LocalDateTime getVrijemePocetka() {
		return vrijemePocetka;
	}

	/**
	 * @param vrijemePocetka the vrijemePocetka to set
	 */
	public void setVrijemePocetka(LocalDateTime vrijemePocetka) {
		this.vrijemePocetka = vrijemePocetka;
	}

	/**
	 * @return the vrijemeKraja
	 */
	public LocalDateTime getVrijemeKraja() {
		return vrijemeKraja;
	}

	/**
	 * @param vrijemeKraja the vrijemeKraja to set
	 */
	public void setVrijemeKraja(LocalDateTime vrijemeKraja) {
		this.vrijemeKraja = vrijemeKraja;
	}

	/**
	 * @return the trajanje
	 */
	public Integer getTrajanje() {
		return trajanje;
	}

	/**
	 * @param trajanje the trajanje to set
	 */
	public void setTrajanje(Integer trajanje) {
		this.trajanje = trajanje;
	}

	/**
	 * @return the jelNaPromociji
	 */
	public Boolean getJelNaPromociji() {
		return jelNaPromociji;
	}

	/**
	 * @param jelNaPromociji the jelNaPromociji to set
	 */
	public void setJelNaPromociji(Boolean jelNaPromociji) {
		this.jelNaPromociji = jelNaPromociji;
	}

	/**
	 * @return the jelNaPopustu
	 */
	public Boolean getJelNaPopustu() {
		return jelNaPopustu;
	}

	/**
	 * @param jelNaPopustu the jelNaPopustu to set
	 */
	public void setJelNaPopustu(Boolean jelNaPopustu) {
		this.jelNaPopustu = jelNaPopustu;
	}

	/**
	 * @return the jelPokvaren
	 */
	public Boolean getJelPokvaren() {
		return jelPokvaren;
	}

	/**
	 * @param jelPokvaren the jelPokvaren to set
	 */
	public void setJelPokvaren(Boolean jelPokvaren) {
		this.jelPokvaren = jelPokvaren;
	}

	@Override
	public String toString() {
		return "Iznajmljivanje [korisnik=" + korisnik + ", voziloId=" + voziloId + ", pocetak=" + pocetak + ", kraj="
				+ kraj + ", vrijemePocetka=" + vrijemePocetka + ", vrijemeKraja=" + vrijemeKraja + ", putanja="
				+ putanja + ", trajanje=" + trajanje + ", jelNaPromociji=" + jelNaPromociji + ", jelNaPopustu="
				+ jelNaPopustu + ", jelPokvaren=" + jelPokvaren + "]";
	}

}
