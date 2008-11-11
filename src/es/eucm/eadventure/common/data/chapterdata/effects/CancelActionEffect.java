package es.eucm.eadventure.common.data.chapterdata.effects;

/**
 * An effect that cancels the standard action
 */
public class CancelActionEffect implements Effect {

	public int getType( ) {
		return CANCEL_ACTION;
	}

}
