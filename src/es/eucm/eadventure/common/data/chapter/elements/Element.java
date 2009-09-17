/**
 * <e-Adventure> is an <e-UCM> research project. <e-UCM>, Department of Software
 * Engineering and Artificial Intelligence. Faculty of Informatics, Complutense
 * University of Madrid (Spain).
 * 
 * @author Del Blanco, A., Marchiori, E., Torrente, F.J. (alphabetical order) *
 * @author L�pez Ma�as, E., P�rez Padilla, F., Sollet, E., Torijano, B. (former
 *         developers by alphabetical order)
 * @author Moreno-Ger, P. & Fern�ndez-Manj�n, B. (directors)
 * @year 2009 Web-site: http://e-adventure.e-ucm.es
 */

/*
 * Copyright (C) 2004-2009 <e-UCM> research group
 * 
 * This file is part of <e-Adventure> project, an educational game & game-like
 * simulation authoring tool, available at http://e-adventure.e-ucm.es.
 * 
 * <e-Adventure> is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the Free
 * Software Foundation; either version 2 of the License, or (at your option) any
 * later version.
 * 
 * <e-Adventure> is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * <e-Adventure>; if not, write to the Free Software Foundation, Inc., 59 Temple
 * Place, Suite 330, Boston, MA 02111-1307 USA
 * 
 */
package es.eucm.eadventure.common.data.chapter.elements;

import java.util.ArrayList;
import java.util.List;

import es.eucm.eadventure.common.data.Described;
import es.eucm.eadventure.common.data.Detailed;
import es.eucm.eadventure.common.data.Documented;
import es.eucm.eadventure.common.data.HasId;
import es.eucm.eadventure.common.data.Named;
import es.eucm.eadventure.common.data.chapter.Action;
import es.eucm.eadventure.common.data.chapter.resources.Resources;

/**
 * This class holds the common data for any element in eAdventure. Here, element
 * means item or character
 */
public abstract class Element implements Cloneable, Named, HasId, Documented, Described, Detailed {

    /**
     * The element's id
     */
    protected String id;

    /**
     * Documentation of the element.
     */
    private String documentation;

    /**
     * The element's name
     */
    protected String name;

    /**
     * The element's brief description
     */
    protected String description;

    /**
     * The element's detailed description
     */
    protected String detailedDescription;

    /**
     * The element's set of resources
     */
    private List<Resources> resources;

    /**
     * List of actions associated with the item
     */
    protected List<Action> actions;

    /**
     * If true, the element returns to its last position when dragged
     */
    private boolean returnsWhenDragged;
    
    /**
     * Creates a new element
     * 
     * @param id
     *            the element's id
     */
    public Element( String id ) {

        this.id = id;
        this.name = "";
        this.description = "";
        this.detailedDescription = "";
        this.returnsWhenDragged = true;
        resources = new ArrayList<Resources>( );
        actions = new ArrayList<Action>( );
    }

    /**
     * Returns the element's id
     * 
     * @return the element's id
     */
    public String getId( ) {

        return id;
    }

    /**
     * Returns the documentation of the element.
     * 
     * @return the documentation of the element
     */
    public String getDocumentation( ) {

        return documentation;
    }

    /**
     * Returns the element's name
     * 
     * @return the element's name
     */
    public String getName( ) {

        return name;
    }

    /**
     * Returns the element's brief description
     * 
     * @return the element's brief description
     */
    public String getDescription( ) {

        return description;
    }

    /**
     * Returns the element's detailed description
     * 
     * @return the element's detailed description
     */
    public String getDetailedDescription( ) {

        return detailedDescription;
    }

    /**
     * Returns the element's list of resources
     * 
     * @return the element's list of resources
     */
    public List<Resources> getResources( ) {

        return resources;
    }

    /**
     * Sets the a new identifier for the element.
     * 
     * @param id
     *            New identifier
     */
    public void setId( String id ) {

        this.id = id;
    }

    /**
     * Changes the documentation of this element.
     * 
     * @param documentation
     *            The new documentation
     */
    public void setDocumentation( String documentation ) {

        this.documentation = documentation;
    }

    /**
     * Changes the element's name
     * 
     * @param name
     *            the new name
     */
    public void setName( String name ) {

        this.name = name;
    }

    /**
     * Changes the element's brief description
     * 
     * @param description
     *            the new brief description
     */
    public void setDescription( String description ) {

        this.description = description;
    }

    /**
     * Changes the element's detailed description
     * 
     * @param detailedDescription
     *            the new detailed description
     */
    public void setDetailedDescription( String detailedDescription ) {

        this.detailedDescription = detailedDescription;
    }

    /**
     * Adds some resources to the list of resources
     * 
     * @param resources
     *            the resources to add
     */
    public void addResources( Resources resources ) {

        this.resources.add( resources );
    }

    /**
     * Adds an action to this item
     * 
     * @param action
     *            the action to add
     */
    public void addAction( Action action ) {

        actions.add( action );
    }

    /**
     * Returns the list of actions of the item
     * 
     * @return the list of actions of the item
     */
    public List<Action> getActions( ) {

        return actions;
    }

    /**
     * Returns the size of the list of actions
     * 
     * @return Size (int) of the list of actions
     */
    public int getActionsCount( ) {

        if( actions == null )
            return 0;
        else
            return actions.size( );
    }

    /**
     * Returns Action object at place i
     * 
     * @param i
     * @return
     */
    public Action getAction( int i ) {

        return actions.get( i );
    }

    /**
     * Changes the list of actions of the item
     * 
     * @param actions
     *            the new list of actions
     */
    public void setActions( ArrayList<Action> actions ) {

        this.actions = actions;
    }

    
    /**
     * Changes the returns when dragged value
     * 
     * @param returnsWhenDragged
     *              the new value
     */
    public void setReturnsWhenDragged(boolean returnsWhenDragged) {
        this.returnsWhenDragged = returnsWhenDragged;
    }
    
    /**
     * Returns if the element must return when dragged
     * 
     * @return true if the element must return when dragged
     */
    public boolean isReturnsWhenDragged() {
        return returnsWhenDragged;
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString( ) {

        StringBuffer sb = new StringBuffer( 40 );

        sb.append( "Name: " );
        sb.append( name );

        sb.append( "\nDescription: " );
        sb.append( description );

        sb.append( "\nDetailed description:" );
        sb.append( detailedDescription );

        sb.append( "\n" );

        return sb.toString( );
    }

    @Override
    public Object clone( ) throws CloneNotSupportedException {

        Element e = (Element) super.clone( );
        if( actions != null ) {
            e.actions = new ArrayList<Action>( );
            for( Action action : actions ) {
                e.actions.add( (Action) action.clone( ) );
            }
        }
        e.description = ( description != null ? new String( description ) : null );
        e.detailedDescription = ( detailedDescription != null ? new String( detailedDescription ) : null );
        e.documentation = ( documentation != null ? new String( documentation ) : null );
        e.id = ( id != null ? new String( id ) : null );
        e.name = ( name != null ? new String( name ) : null );
        e.returnsWhenDragged = returnsWhenDragged;
        if( resources != null ) {
            e.resources = new ArrayList<Resources>( );
            for( Resources r : resources ) {
                e.resources.add( (Resources) r.clone( ) );
            }
        }
        return e;
    }

}