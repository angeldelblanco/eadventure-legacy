/*******************************************************************************
 * eAdventure (formerly <e-Adventure> and <e-Game>) is a research project of the e-UCM
 *          research group.
 *   
 *    Copyright 2005-2012 e-UCM research group.
 *  
 *     e-UCM is a research group of the Department of Software Engineering
 *          and Artificial Intelligence at the Complutense University of Madrid
 *          (School of Computer Science).
 *  
 *          C Profesor Jose Garcia Santesmases sn,
 *          28040 Madrid (Madrid), Spain.
 *  
 *          For more info please visit:  <http://e-adventure.e-ucm.es> or
 *          <http://www.e-ucm.es>
 *  
 *  ****************************************************************************
 * This file is part of eAdventure, version 1.5.
 * 
 *   You can access a list of all the contributors to eAdventure at:
 *          http://e-adventure.e-ucm.es/contributors
 *  
 *  ****************************************************************************
 *       eAdventure is free software: you can redistribute it and/or modify
 *      it under the terms of the GNU Lesser General Public License as published by
 *      the Free Software Foundation, either version 3 of the License, or
 *      (at your option) any later version.
 *  
 *      eAdventure is distributed in the hope that it will be useful,
 *      but WITHOUT ANY WARRANTY; without even the implied warranty of
 *      MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *      GNU Lesser General Public License for more details.
 *  
 *      You should have received a copy of the GNU Lesser General Public License
 *      along with Adventure.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package es.eucm.eadventure.common.auxiliar;

import java.util.TimerTask;

import com.sun.speech.freetts.Voice;
import com.sun.speech.freetts.VoiceManager;

public class TTask extends TimerTask {

    private String text;
    
    private boolean end;

    private boolean deallocate;
    
    /**
     * This is an Voice object of FreeTTS, that is used to synthesize the sound
     * of a conversation line.
     */
    private Voice voice;

    public TTask( ) {
        end=true;
    }
    
    public TTask( String voiceText, String text ) {

        this.text = text;
        this.deallocate = false;
        end=true;
        VoiceManager voiceManager = VoiceManager.getInstance( );
        voice = voiceManager.getVoice( voiceText );
        voice.allocate( );
    }

    @Override
    public void run( ) {

        try {

            end=false;
            voice.speak( text );
           end = true;

        }
        catch( IllegalStateException e ) {
            System.out.println( "TTS found one word which can not be processed." );
        }

    }

    @Override
    public boolean cancel( ) {

        if( !deallocate ) {
            end=true;
            if (voice!=null)
                voice.deallocate( );
            deallocate = true;
        }
        return true;

    }

    
    public boolean isEnd( ) {
    
        return end;
    }
}
