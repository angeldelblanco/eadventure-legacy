package es.eucm.eadventure.editor.gui.structurepanel.structureelements.Effects;

import java.net.MalformedURLException;
import java.net.URL;

import javax.swing.ImageIcon;

import es.eucm.eadventure.common.gui.TextConstants;

public class TriggerStructureListElement extends EffectsStructureListElement{

    private static final String[] COMPONENTS = {TextConstants.getText( "Effect.TriggerConversation" ), TextConstants.getText( "Effect.TriggerCutscene" ),
	TextConstants.getText( "Effect.TriggerBook" ),TextConstants.getText( "Effect.TriggerScene" ),TextConstants.getText( "Effect.TriggerLastScene" )};

    private static final String LIST_URL = "Effects_TriggerEvents.html";
    
    public TriggerStructureListElement() {
	super(TextConstants.getText("EffectsGroup.Trigger"));
	icon = new ImageIcon( "img/icons/adaptationProfiles.png" );
	groupEffects = COMPONENTS;
	path = LIST_URL;
    }
    
}