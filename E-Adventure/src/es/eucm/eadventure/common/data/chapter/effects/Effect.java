/*******************************************************************************
 * <e-Adventure> (formerly <e-Game>) is a research project of the <e-UCM>
 *         research group.
 *  
 *   Copyright 2005-2010 <e-UCM> research group.
 * 
 *   You can access a list of all the contributors to <e-Adventure> at:
 *         http://e-adventure.e-ucm.es/contributors
 * 
 *   <e-UCM> is a research group of the Department of Software Engineering
 *         and Artificial Intelligence at the Complutense University of Madrid
 *         (School of Computer Science).
 * 
 *         C Profesor Jose Garcia Santesmases sn,
 *         28040 Madrid (Madrid), Spain.
 * 
 *         For more info please visit:  <http://e-adventure.e-ucm.es> or
 *         <http://www.e-ucm.es>
 * 
 * ****************************************************************************
 * 
 * This file is part of <e-Adventure>, version 1.2.
 * 
 *     <e-Adventure> is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Lesser General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 * 
 *     <e-Adventure> is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Lesser General Public License for more details.
 * 
 *     You should have received a copy of the GNU Lesser General Public License
 *     along with <e-Adventure>.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package es.eucm.eadventure.common.data.chapter.effects;

/**
 * This interface defines any individual effect that can be triggered by a
 * player's action during the game.
 */
public interface Effect extends Cloneable {

    /**
     * Constant for activate effect.
     */
    public static final int ACTIVATE = 0;

    /**
     * Constant for deactivate effect.
     */
    public static final int DEACTIVATE = 1;

    /**
     * Constant for consume-object effect.
     */
    public static final int CONSUME_OBJECT = 2;

    /**
     * Constant for generate-object effect.
     */
    public static final int GENERATE_OBJECT = 3;

    /**
     * Constant for cancel-action effect.
     */
    public static final int CANCEL_ACTION = 4;

    /**
     * Constant for speak-player effect.
     */
    public static final int SPEAK_PLAYER = 5;

    /**
     * Constant for speak-char effect.
     */
    public static final int SPEAK_CHAR = 6;

    /**
     * Constant for trigger-book effect.
     */
    public static final int TRIGGER_BOOK = 7;

    /**
     * Constant for play-sound effect.
     */
    public static final int PLAY_SOUND = 8;

    /**
     * Constant for play-animation effect.
     */
    public static final int PLAY_ANIMATION = 9;

    /**
     * Constant for move-player effect.
     */
    public static final int MOVE_PLAYER = 10;

    /**
     * Constant for move-npc effect.
     */
    public static final int MOVE_NPC = 11;

    /**
     * Constant for trigger-conversation effect.
     */
    public static final int TRIGGER_CONVERSATION = 12;

    /**
     * Constant for trigger-cutscene effect.
     */
    public static final int TRIGGER_CUTSCENE = 13;

    /**
     * Constant for trigger-scene effect.
     */
    public static final int TRIGGER_SCENE = 14;

    /**
     * Constant for trigger-last-scene effect.
     */
    public static final int TRIGGER_LAST_SCENE = 15;

    /**
     * Constant for random-effect.
     */
    public static final int RANDOM_EFFECT = 16;

    /**
     * Constant for set-value effect.
     */
    public static final int SET_VALUE = 17;

    /**
     * Constant for increment var effect.
     */
    public static final int INCREMENT_VAR = 18;

    /**
     * Constant for decrement var effect.
     */
    public static final int DECREMENT_VAR = 19;

    /**
     * Constant for macro-ref effect.
     */
    public static final int MACRO_REF = 20;

    /**
     * Constant for wait-time effect
     */
    public static final int WAIT_TIME = 21;

    /**
     * Constant for show-text effect
     */
    public static final int SHOW_TEXT = 22;
    
    /**
     * Constant for highlight element effect
     */
    public static final int HIGHLIGHT_ITEM = 23;
    
    /**
     * Constant for move object effect
     */
    public static final int MOVE_OBJECT = 24;
    

    /**
     * Returns the type of the effect.
     * 
     * @return Type of the effect
     */
    public int getType( );

    public Object clone( ) throws CloneNotSupportedException;

}
