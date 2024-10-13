package net.etfbl.pj2.model;

/**
 * Predstavlja koordinatnu tačku u prostoru sa X i Y koordinatama.
 * 
 * @author Slaviša Čovakušić
 */
public class KoordinatnoPolje {

	private Integer X;
	private Integer Y;

	public KoordinatnoPolje(Integer x, Integer y) {
		this.setX(x);
		this.setY(y);
	}

	/**
	 * @return the x
	 */
	public Integer getX() {
		return X;
	}

	/**
	 * @param x the x to set
	 */
	public void setX(Integer x) {
		X = x;
	}

	/**
	 * @return the y
	 */
	public Integer getY() {
		return Y;
	}

	/**
	 * @param y the y to set
	 */
	public void setY(Integer y) {
		Y = y;
	}

	/**
	 * Vraća string reprezentaciju koordinatne tačke u formatu "[X=x, Y=y]".
	 *
	 * @return string reprezentacija koordinatne tačke
	 */
	@Override
	public String toString() {
		return " [X=" + X + ", Y=" + Y + "]";
	}

}
