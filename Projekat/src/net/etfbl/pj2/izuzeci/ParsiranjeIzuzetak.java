package net.etfbl.pj2.izuzeci;

/**
 * Izuzetak koji se baci ukoliko se dogodi bilo koja greska pri parsiranju
 * podataka iz fajla za Iznajmljivanja i Vozila Npr. ukoliko su koordinate van
 * opsega(dodatne napomene za projekat), ukoliko nije pravilan format
 * unosa(koordinata, datuma...), nepravilan broj polja...
 *
 * @author Slaviša Čovakušić
 */

public class ParsiranjeIzuzetak extends Exception {

	private static final long serialVersionUID = 5638539551750044863L;

	public ParsiranjeIzuzetak() {
	}

	/**
	 * Konstruktor koji prima poruku o gresci
	 *
	 * @param msg Dodatne informacije o gresci
	 */
	public ParsiranjeIzuzetak(String msg) {
		super("Greska pri parsiranju " + msg);
	}
}
