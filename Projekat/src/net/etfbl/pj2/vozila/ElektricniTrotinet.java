package net.etfbl.pj2.vozila;

/**
 * Predstavlja električni trotinet koji nasljedjuje klasu Vozilo Implementira
 * specifične metode za punjenje i pražnjenje baterije trotineta (iz interfejsa
 * Napunjivo)
 * 
 * @param TROTINET_SMANJENJE_BATERIJE_PO_SEKUNDI konstanta smanjenja baterije po
 *                                               sekundi za trotinet
 *
 * @author Slaviša Čovakušić
 */
public class ElektricniTrotinet extends Vozilo {

	private static final long serialVersionUID = -602687197538118166L;
	private static final double TROTINET_SMANJENJE_BATERIJE_PO_SEKUNDI = 2;
	private Integer maksimalnaBrzina;

	public ElektricniTrotinet() {

	}

	/**
	 * Parametrizovani konstruktor koji postavlja sve atribute, uključujući trenutni
	 * nivo baterije i maksimalnu brzinu.
	 *
	 * @param id                   Identifikacioni broj vozila
	 * @param cijenaNabavke        Cijena nabavke trotineta
	 * @param proizvodjac          Proizvođač trotineta
	 * @param model                Model trotineta
	 * @param trenutniNivoBaterije Trenutni nivo baterije trotineta
	 * @param maksimalnaBrzina     Maksimalna brzina trotineta
	 */
	public ElektricniTrotinet(String id, double cijenaNabavke, String proizvodjac, String model,
			double trenutniNivoBaterije, Integer maksimalnaBrzina) {
		super(id, cijenaNabavke, proizvodjac, model, trenutniNivoBaterije);
		setMaksimalnaBrzina(maksimalnaBrzina);
	}

	/**
	 * Parametrizovani konstruktor koji postavlja sve atribute osim trenutnog nivoa
	 * baterije, koji se podrazumevano postavlja na 100%.
	 *
	 * @param id               Identifikacioni broj vozila
	 * @param cijenaNabavke    Cijena nabavke trotineta
	 * @param proizvodjac      Proizvođač trotineta
	 * @param model            Model trotineta
	 * @param maksimalnaBrzina Maksimalna brzina trotineta
	 */
	public ElektricniTrotinet(String id, double cijenaNabavke, String proizvodjac, String model,
			Integer maksimalnaBrzina) {
		super(id, cijenaNabavke, proizvodjac, model, 100);
		setMaksimalnaBrzina(maksimalnaBrzina);
	}

	/**
	 * Puni bateriju trotineta na maksimalni nivo (100%). Overrajduje metodu iz
	 * Interfejsa Napunjivo.
	 */
	@Override
	public void napuniBateriju() {
		setTrenutniNivoBaterije(100);
	}

	/**
	 * Prazni bateriju trotineta za definisanu količinu po sekundi. Ako nivo
	 * baterije padne ispod 0, baterija se automatski puni.
	 */
	@Override
	public void isprazniBateriju() {
		if (getTrenutniNivoBaterije() - TROTINET_SMANJENJE_BATERIJE_PO_SEKUNDI <= 0) {
			napuniBateriju();
		}
		setTrenutniNivoBaterije(getTrenutniNivoBaterije() - TROTINET_SMANJENJE_BATERIJE_PO_SEKUNDI);

	}

	/**
	 * @return the maksimalnaBrzina
	 */
	public Integer getMaksimalnaBrzina() {
		return maksimalnaBrzina;
	}

	/**
	 * @param maksimalnaBrzina the maksimalnaBrzina to set
	 */
	public void setMaksimalnaBrzina(Integer maksimalnaBrzina) {
		this.maksimalnaBrzina = maksimalnaBrzina;
	}

	/**
	 * Vraća string reprezentaciju objekta ElektricniTrotinet.
	 *
	 * @return String koji predstavlja trotinet.
	 */
	@Override
	public String toString() {
		return "ElektricniTrotinet [maksimalnaBrzina=" + maksimalnaBrzina + ", toString()=" + super.toString() + "]";
	}
}
