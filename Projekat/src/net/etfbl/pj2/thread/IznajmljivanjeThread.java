package net.etfbl.pj2.thread;

import java.awt.Color;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.SwingUtilities;

import net.etfbl.pj2.gui.MapaGUI;
import net.etfbl.pj2.model.Iznajmljivanje;
import net.etfbl.pj2.model.KoordinatnoPolje;
import net.etfbl.pj2.racun.Racun;
import net.etfbl.pj2.vozila.Vozilo;

/**
 * Klasa IznajmljivanjeThread predstavlja nit koja simulira proces
 * iznajmljivanja vozila. Tokom izvršavanja, ova nit upravlja pomeranjem vozila
 * po mapi, brisanjem prethodnih polja, prati nivo baterije...
 *
 * @author Slavisa Covakusic
 */
public class IznajmljivanjeThread extends Thread {
	private Iznajmljivanje iznajmljivanje;
	private MapaGUI mapaGUI;

	/**
	 * Konstruktor za inicijalizaciju niti za iznajmljivanje vozila.
	 *
	 * @param iznajmljivanje objekat koji sadrži informacije o iznajmljivanju vozila
	 * @param mapaGUI        referenca na grafički interfejs mape
	 */
	public IznajmljivanjeThread(Iznajmljivanje iznajmljivanje, MapaGUI mapaGUI) {
		this.iznajmljivanje = iznajmljivanje;
		this.mapaGUI = mapaGUI;
	}

	/**
	 * Dodaje vozilo na polje te ispisuje njegov id i bateriju
	 * @param polje
	 * @param vozilo
	 */
	private synchronized void dodajVoziloNaPolje(KoordinatnoPolje polje, Vozilo vozilo) {
	    SwingUtilities.invokeLater(() -> {
	        // lista vozila na trenutnom polju
	        List<Vozilo> vozilaNaPolju = mapaGUI.getVozilaNaPolju(polje);
	        vozilaNaPolju.add(vozilo);

	        // ispisivanje na polje na osnovu liste vozila na tom polju
	        mapaGUI.updatujPolje(polje, vozilaNaPolju);

	        JLabel label = mapaGUI.getFieldLabel(polje);
	        label.setForeground(new Color(0, 255, 13));
	        label.setBackground(Color.BLACK);
	    });
	}

	/**
	 * Brise vozilo sa proslog polja kako bi se stvorilo kretanje
	 * @param polje
	 * @param vozilo
	 */
	private synchronized void smakniVoziloSaPolja(KoordinatnoPolje polje, Vozilo vozilo) {
	    SwingUtilities.invokeLater(() -> {
	        List<Vozilo> vozilaNaPolju = mapaGUI.getVozilaNaPolju(polje);
	        vozilaNaPolju.remove(vozilo);

	        mapaGUI.updatujPolje(polje, vozilaNaPolju);

	        //ako je polje prazno, postavi boju polja na pocetnu
	        if (vozilaNaPolju.isEmpty()) {
	            mapaGUI.resetujPolje(polje);
	        }
	    });
	}

	/**
	 * Metoda koja definiše ponašanje niti. Simulira kretanje vozila po mapi na
	 * osnovu unapred definisane putanje, ažurira stanje vozila, prikazuje promene
	 * na mapi i kreira račun po završetku vožnje.
	 */
    @Override
    public void run() {
        Vozilo vozilo = mapaGUI.getVoziloById(iznajmljivanje.getVoziloId());
        List<KoordinatnoPolje> putanja = iznajmljivanje.getPutanja();
        int trajanje = iznajmljivanje.getTrajanje();
        int brojPolja = putanja.size();
        KoordinatnoPolje prev = iznajmljivanje.getPocetak();
        double pocetniNovoBaterije = vozilo.getTrenutniNivoBaterije();
        int vrijemeCekanja = (int) Math.round((double) trajanje*1000 / brojPolja);

        // Dodavanje vozila na startno polje
        dodajVoziloNaPolje(prev, vozilo);

        for (KoordinatnoPolje polje : putanja) {
            try {
                Thread.sleep(vrijemeCekanja);

                // brisanje sa prethodnog polja radi simulacije kretanja
                smakniVoziloSaPolja(prev, vozilo);

                // pomjeranje na sledece polje
                dodajVoziloNaPolje(polje, vozilo);

                vozilo.isprazniBateriju();
                prev = polje;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        // pauza od 0.5s kako bi se vidjelo da vozilo dodje na svoje odrediste, u
        // suporotnom samo nestane nakon predzadnjeg polja
        try {
        	Thread.sleep(500);
        } catch(InterruptedException e) {
        	e.printStackTrace();
        }

        // Brisanje vozila sa zadnjeg polja na kojem se nadje
        smakniVoziloSaPolja(prev, vozilo);

        // Pravljenje racuna
        Racun racun = new Racun(iznajmljivanje, vozilo);
        racun.setPocetniNivoBaterije(pocetniNovoBaterije);
        racun.setKrajnjiNivoBaterije(vozilo.getTrenutniNivoBaterije());
        racun.napraviRacun();
    }
}
