package es.eucm.eadventure.common.data.chapter;

import java.awt.Point;
import java.util.List;

/**
 * The influence area for an item reference or active area
 */
public class InfluenceArea implements Cloneable, Rectangle {

	/**
	 * True if the influence area exists (is defined)
	 */
	private boolean exists = false;
	
	/**
	 * The x axis value of the influence area, relative
	 * to the objects top left corner
	 */
	private int x;
	
	/**
	 * The y axis value of the influence area, relative
	 * to the objects top left corner
	 */
	private int y;
	
	/**
	 * The width of the active area
	 */
	private int width;
	
	/**
	 * The height of the active area
	 */
	private int height;
	
	public InfluenceArea() {}
		
	/**
	 * Creates a new influence area with the given parameters
	 * 
	 * @param x The x axis value
	 * @param y The y axis value
	 * @param width The width of the influence area
	 * @param height The height of the influence area
	 */
	public InfluenceArea(int x, int y, int width, int height) {
		exists = true;
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}
	
	/**
	 * @return the exists
	 */
	public boolean isExists() {
		return exists;
	}

	/**
	 * @param exists the exists to set
	 */
	public void setExists(boolean exists) {
		this.exists = exists;
	}

	/**
	 * @return the x
	 */
	public int getX() {
		return x;
	}

	/**
	 * @param x the x to set
	 */
	public void setX(int x) {
		this.x = x;
	}

	/**
	 * @return the y
	 */
	public int getY() {
		return y;
	}

	/**
	 * @param y the y to set
	 */
	public void setY(int y) {
		this.y = y;
	}

	/**
	 * @return the width
	 */
	public int getWidth() {
		return width;
	}

	/**
	 * @param width the width to set
	 */
	public void setWidth(int width) {
		this.width = width;
	}

	/**
	 * @return the height
	 */
	public int getHeight() {
		return height;
	}

	/**
	 * @param height the height to set
	 */
	public void setHeight(int height) {
		this.height = height;
	}

	public void setValues(int x, int y, int width, int height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}	
	
	public Object clone() throws CloneNotSupportedException {
		InfluenceArea ia = (InfluenceArea) super.clone();
		ia.exists = exists;
		ia.height = height;
		ia.width = width;
		ia.x = x;
		ia.y = y;
		return ia;
	}

	public boolean isRectangular() {
		return true;
	}

	public void setRectangular(boolean rectangular) {
	}
	
	public List<Point> getPoints() {
		return null;
	}
}