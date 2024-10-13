package net.etfbl.pj2.vozila;

/**
 * Predstavlja elektricni bicikl koji nasljedjuje klasu Vozilo Implementira
 * specificne metode za punjenje i praznjenje baterije bicikla (iz interfejsa
 * Napunjivo)
 * 
 * @param BICIKL_SMANJENJE_BATERIJE_PO_SEKUNDI konstanta smanjenja baterije po
 *                                             sekundi za biciklo
 *
 * @author Slaviša Čovakušić
 */
public class ElektricniBicikl extends Vozilo {

	private static final long serialVersionUID = -1198248937390013816L;
	private static final double BICIKL_SMANJENJE_BATERIJE_PO_SEKUNDI = 3;
	private Integer dometSaJednimPunjenjem;

	public ElektricniBicikl() {

	}

	/**
	 * Parametrizovani konstruktor koji postavlja sve atribute, uključujući trenutni
	 * nivo baterije i domet sa jednim punjenjem
	 *
	 * @param id                     Identifikacioni broj vozila
	 * @param cijenaNabavke          Cijena nabavke bicikla
	 * @param proizvodjac            Proizvođač bicikla
	 * @param model                  Model bicikla
	 * @param trenutniNivoBaterije   Trenutni nivo baterije bicikla
	 * @param dometSaJednimPunjenjem Domet bicikla sa jednim punjenjem
	 */
	public ElektricniBicikl(String id, double cijenaNabavke, String proizvodjac, String model,
			double trenutniNivoBaterije, Integer dometSaJednimPunjenjem) {
		super(id, cijenaNabavke, proizvodjac, model, trenutniNivoBaterije);
		setDometSaJednimPunjenjem(dometSaJednimPunjenjem);
	}

	/**
	 * Parametrizovani konstruktor koji postavlja sve atribute osim trenutnog nivoa
	 * baterije, koji se podrazumevano postavlja na 100%
	 *
	 * @param id                     Identifikacioni broj vozila
	 * @param cijenaNabavke          Cijena nabavke bicikla
	 * @param proizvodjac            Proizvođač bicikla
	 * @param model                  Model bicikla
	 * @param dometSaJednimPunjenjem Domet bicikla sa jednim punjenjem
	 */
	public ElektricniBicikl(String id, double cijenaNabavke, String proizvodjac, String model,
			Integer dometSaJednimPunjenjem) {
		super(id, cijenaNabavke, proizvodjac, model, 100);
		setDometSaJednimPunjenjem(dometSaJednimPunjenjem);
	}

	/**
	 * Puni bateriju bicikla na maksimalni nivo (100%). Overrajduje metodu iz
	 * Interfejsa Napunjivo
	 */
	@Override
	public void napuniBateriju() {
		setTrenutniNivoBaterije(100);

	}

	/**
	 * Prazni bateriju bicikla za definisanu količinu po sekundi. Ako nivo baterije
	 * padne ispod 0, baterija se automatski puni.
	 */
	@Override
	public void isprazniBateriju() {
		if (getTrenutniNivoBaterije() - BICIKL_SMANJENJE_BATERIJE_PO_SEKUNDI <= 0) {
			napuniBateriju();
		}
		setTrenutniNivoBaterije(getTrenutniNivoBaterije() - BICIKL_SMANJENJE_BATERIJE_PO_SEKUNDI);

	}

	/**
	 * @return the dometSaJednimPunjenjem
	 */
	public Integer getDometSaJednimPunjenjem() {
		return dometSaJednimPunjenjem;
	}

	/**
	 * @param dometSaJednimPunjenjem the dometSaJednimPunjenjem to set
	 */
	public void setDometSaJednimPunjenjem(Integer dometSaJednimPunjenjem) {
		this.dometSaJednimPunjenjem = dometSaJednimPunjenjem;
	}

	/**
	 * Vraća string reprezentaciju objekta ElektricniBicikl
	 *
	 * @return String koji predstavlja bicikl
	 */
	@Override
	public String toString() {
		return "ElektricniBicikl [dometSaJednimPunjenjem=" + dometSaJednimPunjenjem + ", toString()=" + super.toString()
				+ "]";
	}
}
