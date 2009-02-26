package es.eucm.eadventure.common.data.animation;

/**
 * This class holds the information for an animation transition
 * 
 * @author Eugenio Marchiori
 *
 */
public class Transition implements Cloneable, Timed {
	
	/**
	 * The transition does nothing
	 */
	public static final int TYPE_NONE = 0;
	
	/**
	 * The transition makes the previous frame disappear while the new one appears
	 */
	public static final int TYPE_FADEIN = 1;
	
	/**
	 * The transition places the new frame over the old one from left to right
	 */
	public static final int TYPE_VERTICAL = 2;
	
	/**
	 * The transition places the new frame over the old one from top to bottom
	 */
	public static final int TYPE_HORIZONTAL = 3;
	
	/**
	 * Time (duration) of the transition
	 */
	private long time;
	
	/**
	 * Type of the transition: {@link #TYPE_FADEIN}, {@link #TYPE_NONE}, {@link #TYPE_HORIZONTAL} or {@link #TYPE_VERTICAL}
	 */
	private int type;
	
	/**
	 * Creates a new empty transition
	 */
	public Transition() {
		time = 0;
		type = TYPE_NONE;
	}

	/**
	 * Returns the time (duration) of the transition in milliseconds
	 * 
	 * @return the time (duration) of the transition in milliseconds
	 */
	public long getTime() {
		return time;
	}

	/**
	 * Sets the time (duration) of the transition in milliseconds
	 * 
	 * @param time
	 * 		the new time (duration) of the transition in milliseconds
	 */
	public void setTime(long time) {
		this.time = time;
	}

	/**
	 * Returns the type of the transition
	 * 
	 * @return the type of the transition
 	 */
	public int getType() {
		return type;
	}

	/**
	 * Sets the type of the transition
	 * 
	 * @param type
	 * 			The new type of the transition
	 */
	public void setType(int type) {
		this.type = type;
	}
	
	public Object clone() throws CloneNotSupportedException {
		Transition t = (Transition) super.clone();
		t.time = time;
		t.type = type;
		return t;
	}

}
