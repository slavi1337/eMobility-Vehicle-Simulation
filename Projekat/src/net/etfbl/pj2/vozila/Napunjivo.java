package net.etfbl.pj2.vozila;

/**
 * Interfejs koji definise metode za punjenje i praznjenje baterije za vozila
 *
 * @author Slaviša Čovakušić
 */
public interface Napunjivo {

	/**
	 * Metoda koja puni bateriju vozila do punog kapaciteta (100%)
	 */
	public void napuniBateriju();

	/**
	 * Metoda koja prazni bateriju vozila na razlicit nacin, u zavisnosti od vrste vozila
	 */
	public void isprazniBateriju();
}
