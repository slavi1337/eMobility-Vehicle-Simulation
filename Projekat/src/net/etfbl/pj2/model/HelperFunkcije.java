package net.etfbl.pj2.model;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

/**
 * Klasa HelperFunkcije pruža pomoćne metode za generisanje ID-ova dokumenata i
 * kreiranje putanja vozila
 * 
 * @param DUZINA_DOKUMENTA predstavlja duzinu id-a dokumenta
 *
 * @author Slaviša Čovakušič
 * @version 1.1
 */
public class HelperFunkcije {

	private static final String KARAKTERI = "QWERTZUIOPASDFGJHJKLYXCVBNM0123456789";
	private static final int DUZINA_DOKUMENTA = 9;

	/**
	 * Generiše random ID dokumenta
	 *
	 * @return random generisani ID dokumenta kao String.
	 */
	public static String generisiIdDokumenta() {
		SecureRandom random = new SecureRandom();
		StringBuilder sb = new StringBuilder(DUZINA_DOKUMENTA);

		for (int i = 0; i < DUZINA_DOKUMENTA; i++) {
			sb.append(KARAKTERI.charAt(random.nextInt(KARAKTERI.length())));
		}

		return sb.toString();
	}

	/**
	 * Kreira listu koordinata koje predstavljaju putanju od početne tačke do
	 * krajnje tačke. Pronalazak se vrsi tako sto se prvo ide vertikalno do reda
	 * gdje se nalazi kraj, a zatim horizontalno do kolone gdje je kraj
	 *
	 * @param pocetak Početna koordinata putanje.
	 * @param kraj    Krajnja koordinata putanje.
	 * @return Lista koordinata koja predstavlja putanju od početka do kraja.
	 */

	public static List<KoordinatnoPolje> napraviPutanju(KoordinatnoPolje pocetak, KoordinatnoPolje kraj) {
		List<KoordinatnoPolje> putanja = new ArrayList<>();

		int trenutniX = pocetak.getX();
		int trenutniY = pocetak.getY();

		while (trenutniX != kraj.getX()) {
			if (trenutniX < kraj.getX()) {
				trenutniX++;
			} else {
				trenutniX--;
			}
			putanja.add(new KoordinatnoPolje(trenutniX, trenutniY));
		}

		while (trenutniY != kraj.getY()) {
			if (trenutniY < kraj.getY()) {
				trenutniY++;
			} else {
				trenutniY--;
			}
			putanja.add(new KoordinatnoPolje(trenutniX, trenutniY));
		}

		return putanja;
	}

}
