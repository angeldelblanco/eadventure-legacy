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
package es.eucm.eadventure.editor.gui.displaydialogs;

import java.awt.Image;

import es.eucm.eadventure.common.gui.TC;
import es.eucm.eadventure.editor.control.controllers.AssetsController;

/**
 * This class displays an image.
 * 
 * @author Bruno Torijano Bueno
 */
public class ImageDialog extends GraphicDialog {

    /**
     * Required.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Image to dosplay.
     */
    private Image image;

    /**
     * Creates a new image dialog with the given path.
     * 
     * @param imagePath
     *            Path and extension of the image
     */
    public ImageDialog( String imagePath ) {

        // Load the image
        image = AssetsController.getImage( imagePath );

        // Set the dialog and show it
        setTitle( TC.get( "ImageDialog.Title", AssetsController.getFilename( imagePath ) ) );
        setVisible( true );
    }

    @Override
    protected Image getCurrentImage( ) {

        return image;
    }

    @Override
    protected void deleteImages( ) {

        if( image != null )
            image.flush( );
    }
}
