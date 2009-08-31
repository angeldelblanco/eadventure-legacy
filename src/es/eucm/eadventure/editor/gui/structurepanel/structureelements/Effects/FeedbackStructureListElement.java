/**
 * <e-Adventure> is an <e-UCM> research project.
 * <e-UCM>, Department of Software Engineering and Artificial Intelligence.
 * Faculty of Informatics, Complutense University of Madrid (Spain).
 * @author Del Blanco, A., Marchiori, E., Torrente, F.J.
 * @author Moreno-Ger, P. & Fern�ndez-Manj�n, B. (directors)
 * @year 2009
 * Web-site: http://e-adventure.e-ucm.es
 */

/*
    Copyright (C) 2004-2009 <e-UCM> research group

    This file is part of <e-Adventure> project, an educational game & game-like 
    simulation authoring tool, availabe at http://e-adventure.e-ucm.es. 

    <e-Adventure> is free software; you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation; either version 2 of the License, or
    (at your option) any later version.

    <e-Adventure> is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with <e-Adventure>; if not, write to the Free Software
    Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA

*/
package es.eucm.eadventure.editor.gui.structurepanel.structureelements.Effects;

import java.net.MalformedURLException;
import java.net.URL;

import javax.swing.ImageIcon;

import es.eucm.eadventure.common.gui.TextConstants;

public class FeedbackStructureListElement extends EffectsStructureListElement{

 
    private static final String LIST_URL = "effects_short/Effects_Feedback.html";
    
    public FeedbackStructureListElement() {
	super(TextConstants.getText("EffectsGroup.Feedback"));
	//icon = new ImageIcon( "img/icons/adaptationProfiles.png" );
	groupEffects = new String[]{TextConstants.getText( "Effect.SpeakPlayer" ),TextConstants.getText( "Effect.SpeakCharacter" ),
		TextConstants.getText( "Effect.TriggerBook" ),TextConstants.getText( "Effect.ShowText" ),TextConstants.getText( "Effect.TriggerCutscene" ),TextConstants.getText( "Effect.TriggerConversation" )};

	path = LIST_URL;
    }
    

}
