package es.eucm.eadventure.common.data.chapter.conditions;

import java.util.ArrayList;
import java.util.List;


/**
 * This class holds a list of conditions
 */
public class Conditions implements Cloneable {

	/**
	 * List of simple conditions to be checked
	 */
	private List<Condition> conditions;

	/**
	 * List of conditions to be evaluated with OR logic
	 */
	private List<Conditions> eitherConditions;

	/**
	 * Create a new Conditions
	 */
	public Conditions( ) {
		conditions = new ArrayList<Condition>( );
		eitherConditions = new ArrayList<Conditions>( );
	}

	/**
	 * Adds a new condition to the list
	 * 
	 * @param condition
	 *            the condition to add
	 */
	public void addCondition( Condition condition ) {
		conditions.add( condition );
	}

	/**
	 * Adds a list of conditions, such that at least one of these must be ok
	 * 
	 * @param conditions
	 *            the conditions to add
	 */
	public void addEitherCondition( Conditions conditions ) {
		eitherConditions.add( conditions );
	}
	
	/**
	 * Inserts a list of conditions in the given position
	 * 
	 * @param conditions
	 *            the conditions to add
	 * @param index
	 *            the index where conditions must be inserted
	 */
	public void addEitherCondition( int index, Conditions conditions ) {
		eitherConditions.add( index, conditions );
	}

	/**
	 * Returns whether the conditions block is empty or not.
	 * 
	 * @return True if the block has no conditions, false otherwise
	 */
	public boolean isEmpty( ) {
		return conditions.isEmpty( ) && eitherConditions.isEmpty( );
	}

	/**
	 * Deletes the given either conditions block.
	 * 
	 * @param index
	 *            Index of the either conditions block
	 */
	public void deleteEitherCondition( int index ) {
		eitherConditions.remove( index );
	}

	/**
	 * Returns the main block of conditions (evaluated with AND).
	 * 
	 * @return List of conditions
	 */
	public List<Condition> getMainConditions( ) {
		return conditions;
	}

	/**
	 * Returns the number of either conditions blocks present.
	 * 
	 * @return Count of either conditions blocks
	 */
	public int getEitherConditionsBlockCount( ) {
		return eitherConditions.size( );
	}

	/**
	 * Returns the either block of conditions specified.
	 * 
	 * @param index
	 *            Index of the either block of conditions
	 * @return List of conditions
	 */
	public List<Condition> getEitherConditions( int index ) {
		return eitherConditions.get( index ).getMainConditions( );
	}
	
	/**
	 * Returns the either block of conditions specified.
	 * 
	 * @param index
	 *            Index of the either block of conditions
	 * @return List of conditions
	 */
	public Conditions getEitherBlock( int index ) {
		return eitherConditions.get( index );
	}

	public Object clone() throws CloneNotSupportedException {
		Conditions c = (Conditions) super.clone();
		if (conditions != null) {
			c.conditions = new ArrayList<Condition>();
			for (Condition cs : conditions)
				c.conditions.add((Condition) cs.clone());
		}
		if (eitherConditions != null) {
			c.eitherConditions = new ArrayList<Conditions>();
			for (Conditions cs : eitherConditions)
				c.eitherConditions.add((Conditions) cs.clone());
		}		
		return c;
	}
}