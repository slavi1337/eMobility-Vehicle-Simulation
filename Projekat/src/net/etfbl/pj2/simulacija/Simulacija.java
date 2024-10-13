package net.etfbl.pj2.simulacija;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import net.etfbl.pj2.gui.MapaGUI;
import net.etfbl.pj2.model.Iznajmljivanje;
import net.etfbl.pj2.parser.IznajmljivanjeParser;
import net.etfbl.pj2.parser.VoziloPraser;
import net.etfbl.pj2.thread.IznajmljivanjeThread;
import net.etfbl.pj2.vozila.Vozilo;

/**
 * Glavna klasa iz koje se pokrece main metoda
 * U njoj se parsiraju vozila i iznajmljivanja, te se kreiraju niti i Mapa
 * pravi pauzu od 5s izmedju svakih setova iznajmljivanja
 * 
 * @author Slaviša Čovakušić
 */
public class Simulacija {
	public static void main(String[] args) throws Throwable {
		VoziloPraser vp = new VoziloPraser();
		List<Vozilo> listaVozila = vp.parsujVozila();
		IznajmljivanjeParser ip = new IznajmljivanjeParser(listaVozila);
		List<Iznajmljivanje> iznajmljivanja = ip.parsujIznajmljivanja();

		MapaGUI mapaGUI = new MapaGUI(listaVozila,iznajmljivanja);

		//serijalizacija najprofitabilnij vozila (opcije po broju indeksa)
		mapaGUI.serijalizujNajprofitabilnijaVozila();
		LocalDateTime prosliDatumVrijeme = null;
		
		//br dostupnih procesorskih jezgara jer je to max za multithreading
		//int brojJezgara=Runtime.getRuntime().availableProcessors();
		//treba veci pool od brojJezgara jer vecina niti ceka u trenutnom polju, a za to vrijeme druga nit moze da se izvrsi, zato je 15
		ExecutorService executorService = Executors.newFixedThreadPool(15);

		for (Iznajmljivanje iznajmljivanje : iznajmljivanja) {
			LocalDateTime trenutniDatumVrijeme = iznajmljivanje.getVrijemePocetka();

			if (prosliDatumVrijeme == null || !trenutniDatumVrijeme.equals(prosliDatumVrijeme)) {
				// Cekanje za prosli datum da se zavrsi
				if (prosliDatumVrijeme != null) {
					executorService.shutdown();
					executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
					Thread.sleep(5000);
				}

				// Updejt vremena i datuma na mapi
				mapaGUI.updateDateTimeLabel(trenutniDatumVrijeme);

				prosliDatumVrijeme = trenutniDatumVrijeme;

				executorService = Executors.newFixedThreadPool(15);

			}

			// PRavljenje i submitanje threada za iznajmljivanje
			IznajmljivanjeThread thread = new IznajmljivanjeThread(iznajmljivanje, mapaGUI);
			executorService.submit(thread);
		}
		// zadnji datum zavrsavanje
		executorService.shutdown();
		executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);

		//prikaz kraj animacije na krajju simulacije
		mapaGUI.prikaziKraj();
	}
}
