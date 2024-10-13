package net.etfbl.pj2.vozila;

import java.time.LocalDate;

/**
 * Predstavlja električni automobil koji nasljedjuje klasu Vozilo. Implementira
 * specifične metode za punjenje i pražnjenje baterije automobila (iz interfejsa
 * Napunjivo).
 * 
 * @param AUTO_SMANJENJE_BATERIJE_PO_SEKUNDI konstanta smanjenja baterije po
 *                                           sekundi za automobil.
 * @param mozePrevozitiViseLjudi             svaki automobil moze prevoziti vise
 *                                           ljudi
 *
 * @author Slaviša Čovakušić
 */
public class ElektricniAutomobil extends Vozilo {

	private static final long serialVersionUID = -8336938958066967468L;
	private static final double AUTO_SMANJENJE_BATERIJE_PO_SEKUNDI = 6;
	private LocalDate datumNabavke;
	private String opis;
	private boolean mozePrevozitiViseLjudi = true;

	public ElektricniAutomobil() {

	}

	/**
	 * Parametrizovani konstruktor koji postavlja sve atribute, uključujući trenutni
	 * nivo baterije, datum nabavke i opis automobila.
	 *
	 * @param id                   Identifikacioni broj vozila.
	 * @param cijenaNabavke        Cijena nabavke automobila.
	 * @param proizvodjac          Proizvođač automobila.
	 * @param model                Model automobila.
	 * @param trenutniNivoBaterije Trenutni nivo baterije automobila.
	 * @param datumNabavke         Datum kada je automobil nabavljen.
	 * @param opis                 Opis automobila.
	 */
	public ElektricniAutomobil(String id, double cijenaNabavke, String proizvodjac, String model,
			double trenutniNivoBaterije, LocalDate datumNabavke, String opis) {
		super(id, cijenaNabavke, proizvodjac, model, trenutniNivoBaterije);
		this.setDatumNabavke(datumNabavke);
		this.setOpis(opis);
	}

	/**
	 * Parametrizovani konstruktor koji postavlja sve atribute osim trenutnog nivoa
	 * baterije, koji se podrazumevano postavlja na 100%.
	 *
	 * @param id            Identifikacioni broj vozila.
	 * @param cijenaNabavke Cijena nabavke automobila.
	 * @param proizvodjac   Proizvođač automobila.
	 * @param model         Model automobila.
	 * @param datumNabavke  Datum kada je automobil nabavljen.
	 * @param opis          Opis automobila.
	 */
	public ElektricniAutomobil(String id, double cijenaNabavke, String proizvodjac, String model,
			LocalDate datumNabavke, String opis) {
		super(id, cijenaNabavke, proizvodjac, model, 100);
		this.setDatumNabavke(datumNabavke);
		this.setOpis(opis);
	}

	/**
	 * Puni bateriju automobila na maksimalni nivo (100%). Overrajduje metodu iz
	 * interfejsa Napunjivo.
	 */
	@Override
	public void napuniBateriju() {
		setTrenutniNivoBaterije(100);
	}

	/**
	 * Prazni bateriju automobila za definisanu količinu po sekundi. Ako nivo
	 * baterije padne ispod 0, baterija se automatski puni.
	 */
	@Override
	public void isprazniBateriju() {
		if (getTrenutniNivoBaterije() - AUTO_SMANJENJE_BATERIJE_PO_SEKUNDI <= 0) {
			napuniBateriju();
		}
		setTrenutniNivoBaterije(getTrenutniNivoBaterije() - AUTO_SMANJENJE_BATERIJE_PO_SEKUNDI);
	}

	/**
	 * @return datumNabavke - Datum kada je automobil nabavljen.
	 */
	public LocalDate getDatumNabavke() {
		return datumNabavke;
	}

	/**
	 * @param datumNabavke the datumNabavke to set
	 */
	public void setDatumNabavke(LocalDate datumNabavke) {
		this.datumNabavke = datumNabavke;
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
	 * @return the mozePrevozitiViseLjudi
	 */
	public boolean isMozePrevozitiViseLjudi() {
		return mozePrevozitiViseLjudi;
	}

	/**
	 * @param mozePrevozitiViseLjudi the mozePrevozitiViseLjudi to set
	 */
	public void setMozePrevozitiViseLjudi(boolean mozePrevozitiViseLjudi) {
		this.mozePrevozitiViseLjudi = mozePrevozitiViseLjudi;
	}

	/**
	 * Vraća string reprezentaciju objekta ElektricniAutomobil.
	 *
	 * @return String koji predstavlja automobil.
	 */
	@Override
	public String toString() {
		return "ElektricniAutomobil [datumNabavke=" + datumNabavke + ", opis=" + opis + ", mozePrevozitiViseLjudi="
				+ mozePrevozitiViseLjudi + ", toString()=" + super.toString() + "]";
	}
}
