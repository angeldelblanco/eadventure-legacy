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
package es.eucm.eadventure.editor.gui.audio;

/**
 * This abstract class defines any kind of sound event managed in eAdventure. <p>Any concrete class for a concrete
 * sound format must implement the playOnce() method. stopPlaying() method should also be overriden, calling this class'
 * stopPlaying() method to stop the play loop and actually stopping the sound currently being played. <p>The sound is
 * played in a new thread, so execution is not stopped while playing the sound.
 */
public abstract class Sound extends Thread {

	/**
	 * Creates a new Sound.
	 */
	public Sound( ) {
		super( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Thread#run()
	 */
	public void run( ) {
		playOnce( );
	}

	/**
	 * Starts playing the sound.
	 */
	public void startPlaying( ) {
		start( );
	}

	/**
	 * Plays the sound just once. This method shouldn't be called manually. Instead, create a new Sound with loop =
	 * false, and call startPlaying().
	 */
	protected abstract void playOnce( );

	/**
	 * Stops playing the sound.
	 */
	public abstract void stopPlaying( );

}
