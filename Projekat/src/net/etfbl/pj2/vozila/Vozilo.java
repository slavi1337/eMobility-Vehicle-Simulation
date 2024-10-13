package net.etfbl.pj2.vozila;

import java.io.Serializable;

/**
 * Apstraktna klasa koja predstavlja osnovnu za sve vrste vozila Sva vozila se
 * mogu napuniti i isprazniti i zbog toga Vozilo implementira Napunjivo
 * interfejs Najprofitabilnija vozila se trebaju serijalizovati u binarni
 * format, pa zato klasa implementira i Serializable interfejs
 *
 * @author Slaviša Čovakušić
 */
public abstract class Vozilo implements Napunjivo, Serializable {

	private static final long serialVersionUID = 8989057120938279759L;
	private String id;
	private double cijenaNabavke;
	private String proizvodjac;
	private String model;
	private double trenutniNivoBaterije;

	/**
	 * Podrazumijevani konstruktor klase Vozilo.
	 */
	public Vozilo() {
	}

	/**
	 * Konstruktor koji inicijalizuje vozilo sa zadatim parametrima
	 *
	 * @param id                   Jedinstveni identifikator vozila
	 * @param cijenaNabavke        Cijena po kojoj je vozilo nabavljeno
	 * @param proizvodjac          Proizvodjac vozila
	 * @param model                Model vozila
	 * @param trenutniNivoBaterije Trenutni nivo napunjenosti baterije
	 */
	public Vozilo(String id, double cijenaNabavke, String proizvodjac, String model, double trenutniNivoBaterije) {
		this.setId(id);
		this.setCijenaNabavke(cijenaNabavke);
		this.setProizvodjac(proizvodjac);
		this.setModel(model);
		this.setTrenutniNivoBaterije(trenutniNivoBaterije);
	}

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return the cijenaNabavke
	 */
	public double getCijenaNabavke() {
		return cijenaNabavke;
	}

	/**
	 * @param cijenaNabavke the cijenaNabavke to set
	 */
	public void setCijenaNabavke(double cijenaNabavke) {
		this.cijenaNabavke = cijenaNabavke;
	}

	/**
	 * @return the proizvodjac
	 */
	public String getProizvodjac() {
		return proizvodjac;
	}

	/**
	 * @param proizvodjac the proizvodjac to set
	 */
	public void setProizvodjac(String proizvodjac) {
		this.proizvodjac = proizvodjac;
	}

	/**
	 * @return the model
	 */
	public String getModel() {
		return model;
	}

	/**
	 * @param model the model to set
	 */
	public void setModel(String model) {
		this.model = model;
	}

	/**
	 * @return the trenutniNivoBaterije
	 */
	public double getTrenutniNivoBaterije() {
		return trenutniNivoBaterije;
	}

	/**
	 * @param trenutniNivoBaterije the trenutniNivoBaterije to set
	 */
	public void setTrenutniNivoBaterije(double trenutniNivoBaterije) {
		this.trenutniNivoBaterije = trenutniNivoBaterije;
	}

	/**
	 * Vraca string koji predstavlja sve relevantne informacije o vozilu koje ce se
	 * ispisivati pri printanju vozila
	 *
	 * @return String sa podacima o vozilu
	 */
	@Override
	public String toString() {
		return "Vozilo [id=" + id + ", cijenaNabavke=" + cijenaNabavke + ", proizvodjac=" + proizvodjac + ", model="
				+ model + ", trenutniNivoBaterije=" + trenutniNivoBaterije + "]";
	}

}
