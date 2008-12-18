package es.eucm.eadventure.engine.core.control.functionaldata.functionalactions;

import es.eucm.eadventure.engine.core.control.ActionManager;
import es.eucm.eadventure.engine.core.control.animations.AnimationState;
import es.eucm.eadventure.engine.core.control.functionaldata.FunctionalElement;
import es.eucm.eadventure.engine.core.control.functionaldata.FunctionalPlayer;

public class FunctionalLook extends FunctionalAction {

	private FunctionalElement element;
	
	public FunctionalLook(FunctionalElement element) {
		super(null);
		type = ActionManager.ACTION_LOOK;
		this.element = element;
	}

	@Override
	public void drawAditionalElements() {
	}

	@Override
	public void start(FunctionalPlayer functionalPlayer) {
		this.functionalPlayer = functionalPlayer;
        if( element.isInInventory( ) ) {
            functionalPlayer.setDirection( AnimationState.SOUTH );
        } else {
            if( element.getX( ) < functionalPlayer.getX( ) )
                functionalPlayer.setDirection( AnimationState.WEST );
            else
                functionalPlayer.setDirection( AnimationState.EAST );
        }
		finished = true;
        functionalPlayer.speak(element.getElement( ).getDescription( ));
	}

	@Override
	public void stop() {
		finished = true;
	}

	@Override
	public void update(long elapsedTime) {
	}

	@Override
	public void setAnotherElement(FunctionalElement element) {
	}

}
