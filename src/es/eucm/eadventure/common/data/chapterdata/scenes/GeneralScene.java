package es.eucm.eadventure.common.data.chapterdata.scenes;

import java.util.ArrayList;
import java.util.List;

import es.eucm.eadventure.common.data.chapterdata.resources.Resources;

/**
 * This class holds the data of a scene of any type in eAdventure.
 */
public abstract class GeneralScene {

	/**
	 * A regular eAdventure scene.
	 */
	public static final int SCENE = 0;

	/**
	 * A scene of type "slidescene".
	 */
	public static final int SLIDESCENE = 1;

	/**
	 * A scene of type "videoscene".
	 */
	public static final int VIDEOSCENE = 2;

	/**
	 * Type of the scene.
	 */
	private int type;

	/**
	 * Id of the scene.
	 */
	private String id;

	/**
	 * Name of the scene.
	 */
	private String name;

	/**
	 * Documentation of the scene.
	 */
	private String documentation;

	/**
	 * List of resources for the scene.
	 */
	private List<Resources> resources;

	/**
	 * Creates a new GeneralScene.
	 * 
	 * @param type
	 *            the type of the scene
	 * @param id
	 *            the id of the scene
	 */
	protected GeneralScene( int type, String id ) {
		this.type = type;
		this.id = id;
		name = "";
		documentation = null;
		resources = new ArrayList<Resources>( );
	}

	/**
	 * Returns the type of the scene.
	 * 
	 * @return the type of the scene
	 */
	public int getType( ) {
		return type;
	}

	/**
	 * Returns the id of the scene.
	 * 
	 * @return the id of the scene
	 */
	public String getId( ) {
		return id;
	}

	/**
	 * Returns the name of the scene.
	 * 
	 * @return the name of the scene
	 */
	public String getName( ) {
		return name;
	}

	/**
	 * Returns the documentation of the scene.
	 * 
	 * @return the documentation of the scene
	 */
	public String getDocumentation( ) {
		return documentation;
	}

	/**
	 * Returns the list of resources of this scene.
	 * 
	 * @return the list of resources of this scene
	 */
	public List<Resources> getResources( ) {
		return resources;
	}

	/**
	 * Sets the a new identifier for the general scene.
	 * 
	 * @param id
	 *            New identifier
	 */
	public void setId( String id ) {
		this.id = id;
	}

	/**
	 * Changes the name of this scene.
	 * 
	 * @param name
	 *            the new name
	 */
	public void setName( String name ) {
		this.name = name;
	}

	/**
	 * Changes the documentation of this scene.
	 * 
	 * @param documentation
	 *            The new documentation
	 */
	public void setDocumentation( String documentation ) {
		this.documentation = documentation;
	}

	/**
	 * Adds some resources to the list of resources.
	 * 
	 * @param resources
	 *            the resources to add
	 */
	public void addResources( Resources resources ) {
		this.resources.add( resources );
	}
}
