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
package es.eucm.eadventure.editor.gui.elementpanels.macro;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;

import es.eucm.eadventure.common.data.Documented;
import es.eucm.eadventure.common.gui.TC;
import es.eucm.eadventure.editor.control.controllers.macro.MacroDataControl;
import es.eucm.eadventure.editor.control.tools.listeners.DocumentationChangeListener;
import es.eucm.eadventure.editor.gui.editdialogs.effectdialogs.MacroReferenceEffectDialog;
import es.eucm.eadventure.editor.gui.elementpanels.general.EffectsPanel;

/**
 * Panel for a Macro
 * 
 * @author Javier
 * 
 */
public class MacroPanel extends JPanel {

    /**
     * Required
     */
    private static final long serialVersionUID = 1356438513519568096L;

    /**
     * Effects panel
     */
    private EffectsPanel effectsPanel;

    private JTextArea documentationTextArea;

    /**
     * Constructor
     * 
     * @param dataControl
     */
    public MacroPanel( MacroDataControl dataControl ) {

        GridBagConstraints c = new GridBagConstraints( );
        c.insets = new Insets( 5, 5, 5, 5 );
        setLayout( new GridBagLayout( ) );

        // Create the text area for the documentation
        c.fill = GridBagConstraints.BOTH;
        c.weighty = 0.3;
        c.weightx = 1;

        JPanel documentationPanel = new JPanel( );
        documentationPanel.setLayout( new GridLayout( ) );
        documentationTextArea = new JTextArea( dataControl.getDocumentation( ), 4, 0 );
        documentationTextArea.setLineWrap( true );
        documentationTextArea.setWrapStyleWord( true );
        documentationTextArea.getDocument( ).addDocumentListener( new DocumentationChangeListener( documentationTextArea, (Documented) dataControl.getContent( ) ) );
        documentationPanel.add( new JScrollPane( documentationTextArea, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER ) );
        documentationPanel.setBorder( BorderFactory.createTitledBorder( BorderFactory.createEtchedBorder( ), TC.get( "Macro.Documentation" ) ) );

        add( documentationPanel, c );

        c.weighty = 0.7;
        c.gridy = 1;

        MacroReferenceEffectDialog.ID = dataControl.getId( );
        effectsPanel = new EffectsPanel( dataControl.getController( ) );
        effectsPanel.setBorder( BorderFactory.createTitledBorder( BorderFactory.createEtchedBorder( ), TC.get( "Macro.Effects" ) ) );
        add( effectsPanel, c );
    }
}
