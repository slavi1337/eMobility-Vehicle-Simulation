package net.etfbl.pj2.izuzeci;

/**
 * Izuzetak koji se baci ukoliko se u fajlu za vozila naidje na nepoznatu vrstu
 * vozila
 * 
 * @author Slaviša Čovakušić
 */

public class NepoznazoVoziloIzuzetak extends Exception {

	private static final long serialVersionUID = -8368404786327125945L;

	public NepoznazoVoziloIzuzetak() {
	}

	/**
	 * Konstruktor koji prima poruku o gresci
	 * 
	 * @param msg Dodatne informacije o gresci
	 */
	public NepoznazoVoziloIzuzetak(String msg) {
		super("Greska, nepoznato vozilo: " + msg);
	}
}
