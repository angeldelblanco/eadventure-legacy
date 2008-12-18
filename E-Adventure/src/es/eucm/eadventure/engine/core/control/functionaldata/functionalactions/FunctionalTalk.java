package es.eucm.eadventure.engine.core.control.functionaldata.functionalactions;

import java.util.List;

import es.eucm.eadventure.common.data.chapter.Action;
import es.eucm.eadventure.common.data.chapter.ConversationReference;
import es.eucm.eadventure.engine.core.control.ActionManager;
import es.eucm.eadventure.engine.core.control.Game;
import es.eucm.eadventure.engine.core.control.functionaldata.FunctionalConditions;
import es.eucm.eadventure.engine.core.control.functionaldata.FunctionalElement;
import es.eucm.eadventure.engine.core.control.functionaldata.FunctionalNPC;
import es.eucm.eadventure.engine.core.control.functionaldata.FunctionalPlayer;
import es.eucm.eadventure.engine.core.data.GameText;

public class FunctionalTalk extends FunctionalAction {

	public static final int DEFAULT_DISTANCE_TO_KEEP = 35;
	
	FunctionalNPC npc;
	
	private boolean anyConversation;
	
	public FunctionalTalk(Action action, FunctionalElement npc) {
		super(action);
		this.npc = (FunctionalNPC) npc;
		this.type = ActionManager.ACTION_TALK;
		keepDistance = DEFAULT_DISTANCE_TO_KEEP;

        List<ConversationReference> conversationReferences = this.npc.getNPC( ).getConversationReferences( );     
        anyConversation = false;
        for( int i = 0; i < conversationReferences.size( ) && !anyConversation; i++ )
            if( new FunctionalConditions( conversationReferences.get( i ).getConditions( ) ).allConditionsOk( ) )
                anyConversation = true;
        
        if( anyConversation ) {
            needsGoTo = true;
        } else {
            needsGoTo = false;
        }

	
	}

	@Override
	public void drawAditionalElements() {
	}

	@Override
	public void setAnotherElement(FunctionalElement element) {
	}

	@Override
	public void start(FunctionalPlayer functionalPlayer) {
		this.functionalPlayer = functionalPlayer;
        
        if( anyConversation ) {
            finished = false;
        } else {
            functionalPlayer.speak( GameText.getTextTalkCannot( ) );
            finished = true;
        }
	}

	@Override
	public void stop() {
		finished = true;
	}

	@Override
	public void update(long elapsedTime) {
		List<ConversationReference> conversationReferences = npc.getNPC( ).getConversationReferences( );
		boolean triggeredConversation = false;

		for( int i = 0; i < conversationReferences.size( ) && !triggeredConversation; i++ ) {
			if( new FunctionalConditions( conversationReferences.get( i ).getConditions( ) ).allConditionsOk( ) ) {
				Game.getInstance( ).setCurrentNPC( npc );
				Game.getInstance( ).setConversation( conversationReferences.get( i ).getIdTarget( ) );
				Game.getInstance( ).setState( Game.STATE_CONVERSATION );
				triggeredConversation = true;
			}
		}
		
		if( !triggeredConversation )
			functionalPlayer.speak( GameText.getTextTalkCannot( ) );
		
		finished = true;
	}
}
