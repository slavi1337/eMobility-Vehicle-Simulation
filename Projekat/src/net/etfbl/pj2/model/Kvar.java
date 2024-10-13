package net.etfbl.pj2.model;

import java.time.LocalDateTime;

/**
 * Klasa Kvar predstavlja kvar koji se desio na vozilu, sadrži opis kvara i
 * vrijeme kada se kvar dogodio.
 * 
 * @author Slaviša Čovakušić
 */
public class Kvar {
	private String opis;
	private LocalDateTime datumVremeKvara;

	/**
	 * Konstruktor koji prima opis kvara i datum/vrijeme kvara.
	 *
	 * @param opis            Opis kvara.
	 * @param datumVremeKvara Datum i vrijeme kada se kvar dogodio.
	 */
	public Kvar(String opis, LocalDateTime datumVremeKvara) {
		this.setOpis(opis);
		this.setDatumVremeKvara(datumVremeKvara);
	}

	/**
	 * @return the opis
	 */
	public String getOpis() {
		return opis;
	}

	/**
	 * @param opis the opis to set
	 */
	public void setOpis(String opis) {
		this.opis = opis;
	}

	/**
	 * @return the datumVremeKvara
	 */
	public LocalDateTime getDatumVremeKvara() {
		return datumVremeKvara;
	}

	/**
	 * @param datumVremeKvara the datumVremeKvara to set
	 */
	public void setDatumVremeKvara(LocalDateTime datumVremeKvara) {
		this.datumVremeKvara = datumVremeKvara;
	}

	@Override
	public String toString() {
		return "Kvar [opis=" + opis + ", datumVremeKvara=" + datumVremeKvara + "]";
	}

}