package es.eucm.eadventure.editor.gui.elementpanels.scene;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JTextPane;

import es.eucm.eadventure.common.gui.TextConstants;
import es.eucm.eadventure.editor.control.Controller;
import es.eucm.eadventure.editor.control.controllers.scene.ActiveAreaDataControl;
import es.eucm.eadventure.editor.control.controllers.scene.ActiveAreasListDataControl;
import es.eucm.eadventure.editor.control.controllers.scene.BarrierDataControl;
import es.eucm.eadventure.editor.control.controllers.scene.ElementReferenceDataControl;
import es.eucm.eadventure.editor.control.controllers.scene.ExitDataControl;
import es.eucm.eadventure.editor.gui.otherpanels.ScenePreviewEditionPanel;

public class ActiveAreasListPanel extends JPanel {

	/**
	 * Required.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Constructor.
	 * 
	 * @param activeAreasListDataControl
	 *            ActiveAreas list controller
	 */
	public ActiveAreasListPanel( ActiveAreasListDataControl activeAreasListDataControl ) {

		// Take the path of the background
		String scenePath = Controller.getInstance( ).getSceneImagePath( activeAreasListDataControl.getParentSceneId( ) );

		// Set the layout
		setLayout( new GridBagLayout( ) );
//		setBorder( BorderFactory.createTitledBorder( BorderFactory.createEtchedBorder( ), TextConstants.getText( "ActiveAreasList.Title" ) ) );
		GridBagConstraints c = new GridBagConstraints( );
		c.insets = new Insets( 5, 5, 5, 5 );

		// Create the text area for the documentation
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 1;
		JTextPane informationTextPane = new JTextPane( );
		informationTextPane.setEditable( false );
		informationTextPane.setBackground( getBackground( ) );
		informationTextPane.setText( TextConstants.getText( "ActiveAreasList.Information" ) );
		JPanel informationPanel = new JPanel( );
		informationPanel.setBorder( BorderFactory.createTitledBorder( BorderFactory.createEtchedBorder( ), TextConstants.getText( "GeneralText.Information" ) ) );
		informationPanel.setLayout( new BorderLayout( ) );
		informationPanel.add( informationTextPane, BorderLayout.CENTER );
		add( informationPanel, c );

		// Create and set the preview panel
		c.gridy = 1;
		c.fill = GridBagConstraints.BOTH;
		c.weightx = 1;
		c.weighty = 1;
		ScenePreviewEditionPanel spep = new ScenePreviewEditionPanel(false, scenePath);
		add( spep, c );

		if( scenePath != null ) {
			for( ElementReferenceDataControl elementReference : activeAreasListDataControl.getParentSceneItemReferences( ) ) {
				spep.addElement(ScenePreviewEditionPanel.CATEGORY_OBJECT, elementReference);
			}
			spep.setMovableCategory(ScenePreviewEditionPanel.CATEGORY_OBJECT, false);
			for( ElementReferenceDataControl elementReference : activeAreasListDataControl.getParentSceneNPCReferences( ) ) {
				spep.addElement(ScenePreviewEditionPanel.CATEGORY_CHARACTER, elementReference);
			}
			spep.setMovableCategory(ScenePreviewEditionPanel.CATEGORY_CHARACTER, false);
			for( ElementReferenceDataControl elementReference : activeAreasListDataControl.getParentSceneAtrezzoReferences( ) ) {
				spep.addElement(ScenePreviewEditionPanel.CATEGORY_ATREZZO, elementReference);
			}
			spep.setMovableCategory(ScenePreviewEditionPanel.CATEGORY_ATREZZO, false);
			for( ExitDataControl exit : activeAreasListDataControl.getParentSceneExits( ) ) {
				spep.addExit(exit);
			}
			spep.setMovableCategory(ScenePreviewEditionPanel.CATEGORY_EXIT, false);
			for( BarrierDataControl barrier : activeAreasListDataControl.getParentSceneBarriers( ) ) {
				spep.addBarrier(barrier);
			}
			spep.setMovableCategory(ScenePreviewEditionPanel.CATEGORY_BARRIER, false);

			for( ActiveAreaDataControl activeArea : activeAreasListDataControl.getActiveAreas( ) ) {
				spep.addActiveArea(activeArea);
			}
		}
	}
}
