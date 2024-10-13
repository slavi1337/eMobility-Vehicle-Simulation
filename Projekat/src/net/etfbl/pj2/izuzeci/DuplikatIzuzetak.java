package net.etfbl.pj2.izuzeci;

/**
 * Izuzetak koji se baci ukoliko se naidje na vozilo sa istim IDom u fajlu za
 * Vozila
 * 
 * @author Slaviša Čovakušić
 */

public class DuplikatIzuzetak extends Exception {

	private static final long serialVersionUID = 8190208668075793526L;

	public DuplikatIzuzetak() {
	}

	/**
	 * Konstruktor koji prima poruku o gresci
	 * 
	 * @param msg Dodatne informacije o gresci
	 */
	public DuplikatIzuzetak(String msg) {
		super("Greska, duplikat: " + msg);
	}
}
