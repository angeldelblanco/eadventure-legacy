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
package es.eucm.eadventure.editor.data.meta.auxiliar;

import es.eucm.eadventure.common.gui.TC;
import es.eucm.eadventure.editor.data.meta.Vocabulary;

public class LOMRequirement extends LOMESContainer {

    public LOMRequirement( ) {

        super( );
        add( new LOMOrComposite( ) );
    }

    public LOMRequirement( Vocabulary type, Vocabulary name, String minimumVersion, String maximumVersion ) {

        this( );
        add( new LOMOrComposite( type, name, minimumVersion, maximumVersion ) );
    }

    public void addOrComposite( Vocabulary type, Vocabulary name, String minimumVersion, String maximumVersion ) {

        add( new LOMOrComposite( type, name, minimumVersion, maximumVersion ) );
    }

    public void addOrComposite( Vocabulary type, Vocabulary name, String minimumVersion, String maximumVersion, int index ) {

        if( index == 0 ) {
            add( new LOMOrComposite( type, name, minimumVersion, maximumVersion ) );
        }
        else {
            delete( index - 1 );
            add( new LOMOrComposite( type, name, minimumVersion, maximumVersion ), index - 1 );

        }
    }

    @Override
    public String[] elements( ) {

        String[] elements = new String[ container.size( ) ];
        for( int i = 0; i < container.size( ); i++ )
            elements[i] = ( (LOMOrComposite) container.get( i ) ).getType( ).getValue( );
        return elements;
    }

    @Override
    public String getTitle( ) {

        return TC.get( "LOMES.Requirement.Title" );
    }

    @Override
    public String[] attributes( ) {

        // TODO Auto-generated method stub
        return null;
    }

}