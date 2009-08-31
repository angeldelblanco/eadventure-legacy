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
package es.eucm.eadventure.editor.control.writer.domwriters;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import es.eucm.eadventure.common.auxiliar.ReportDialog;
import es.eucm.eadventure.common.data.chapter.elements.Player;
import es.eucm.eadventure.common.data.chapter.resources.Resources;

public class PlayerDOMWriter {

    /**
     * Private constructor.
     */
    private PlayerDOMWriter( ) {

    }

    public static Node buildDOM( Player player ) {

        Node playerNode = null;

        try {
            // Create the necessary elements to create the DOM
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance( );
            DocumentBuilder db = dbf.newDocumentBuilder( );
            Document doc = db.newDocument( );

            // Create the root node
            playerNode = doc.createElement( "player" );

            // Append the documentation (if avalaible)
            if( player.getDocumentation( ) != null ) {
                Node playerDocumentationNode = doc.createElement( "documentation" );
                playerDocumentationNode.appendChild( doc.createTextNode( player.getDocumentation( ) ) );
                playerNode.appendChild( playerDocumentationNode );
            }

            // Append the resources
            for( Resources resources : player.getResources( ) ) {
                Node resourcesNode = ResourcesDOMWriter.buildDOM( resources, ResourcesDOMWriter.RESOURCES_CHARACTER );
                doc.adoptNode( resourcesNode );
                playerNode.appendChild( resourcesNode );
            }

            // Create the textcolor
            Element textColorNode = doc.createElement( "textcolor" );
            textColorNode.setAttribute( "showsSpeechBubble", ( player.getShowsSpeechBubbles( ) ? "yes" : "no" ) );
            textColorNode.setAttribute( "bubbleBkgColor", player.getBubbleBkgColor( ) );
            textColorNode.setAttribute( "bubbleBorderColor", player.getBubbleBorderColor( ) );

            // Create and append the frontcolor
            Element frontColorElement = doc.createElement( "frontcolor" );
            frontColorElement.setAttribute( "color", player.getTextFrontColor( ) );
            textColorNode.appendChild( frontColorElement );

            // Create and append the bordercolor
            Element borderColoElement = doc.createElement( "bordercolor" );
            borderColoElement.setAttribute( "color", player.getTextBorderColor( ) );
            textColorNode.appendChild( borderColoElement );

            // Append the textcolor
            playerNode.appendChild( textColorNode );

            // Create the description
            Node descriptionNode = doc.createElement( "description" );

            // Create and append the name, brief description and detailed description
            Node nameNode = doc.createElement( "name" );
            nameNode.appendChild( doc.createTextNode( player.getName( ) ) );
            descriptionNode.appendChild( nameNode );

            Node briefNode = doc.createElement( "brief" );
            briefNode.appendChild( doc.createTextNode( player.getDescription( ) ) );
            descriptionNode.appendChild( briefNode );

            Node detailedNode = doc.createElement( "detailed" );
            detailedNode.appendChild( doc.createTextNode( player.getDetailedDescription( ) ) );
            descriptionNode.appendChild( detailedNode );

            // Append the description
            playerNode.appendChild( descriptionNode );

            // Create the voice tag
            Element voiceNode = doc.createElement( "voice" );
            // Create and append the voice name and if is alwaysSynthesizer
            voiceNode.setAttribute( "name", player.getVoice( ) );
            if( player.isAlwaysSynthesizer( ) )
                voiceNode.setAttribute( "synthesizeAlways", "yes" );
            else
                voiceNode.setAttribute( "synthesizeAlways", "no" );

            // Append the voice tag

            playerNode.appendChild( voiceNode );

        }
        catch( ParserConfigurationException e ) {
            ReportDialog.GenerateErrorReport( e, true, "UNKNOWERROR" );
        }

        return playerNode;
    }
}
