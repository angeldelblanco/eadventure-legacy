package es.eucm.eadventure.editor.gui.elementpanels.general;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import es.eucm.eadventure.common.gui.TextConstants;
import es.eucm.eadventure.editor.control.controllers.DataControlWithResources;
import es.eucm.eadventure.editor.control.controllers.general.CustomActionDataControl;
import es.eucm.eadventure.editor.gui.Updateable;

/**
 * The panel for the edition of a custom action
 * 
 * @author Eugenio Marchiori
 *
 */
public class CustomActionPropertiesPanel extends JPanel implements ActionTypePanel,Updateable{

	/**
	 * Default generated serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * The data control for the custom action
	 */
	private CustomActionDataControl customActionDataControl;
	
	/**
	 * The panel where the general action configuration is displayed
	 */
	private ActionPropertiesPanel actionPanel;
	
	/**
	 * The panel used to configure the resources
	 */
	private CustomActionLooksPanel looksPanel;
	
	/**
	 * The tab panel that allows to switch between the ActionPanel
	 * and the one with personalization elements
	 */
	private JTabbedPane tabPanel;
			
	/**
	 * Defaul constructor
	 * 
	 * @param customActionDataControl the dataControl for the customaction
	 */
	public CustomActionPropertiesPanel(CustomActionDataControl customActionDataControl) {
		this.customActionDataControl = customActionDataControl;
		
		tabPanel = new JTabbedPane( );
		
		actionPanel = new ActionPropertiesPanel(customActionDataControl);
		
		JPanel personalizationPanel = createPersonalizationPanel();
		
		tabPanel.insertTab( TextConstants.getText( "CustomAction.PersonalizationTitle" ), null, personalizationPanel, TextConstants.getText( "CustomAction.PersonalizationTip" ), 0 );
		tabPanel.insertTab( TextConstants.getText( "CustomAction.ConfigurationTitle" ), null, actionPanel, TextConstants.getText( "CustomAction.ConfigurationTip" ), 1 );

		setLayout( new BorderLayout( ) );
		add( tabPanel, BorderLayout.CENTER );

	}
	
	/**
	 * Creates the panel where the personalization elements of the action
	 * are placed.
	 * 
	 * @return the panel with the necessary elements
	 */
	private JPanel createPersonalizationPanel() {
		JPanel personalizationPanel = new JPanel();
		personalizationPanel.setLayout(new BorderLayout());
		
		looksPanel = new CustomActionLooksPanel( customActionDataControl );
		personalizationPanel.add(looksPanel, BorderLayout.CENTER);
		
		return personalizationPanel;
	}

	private class CustomActionLooksPanel extends LooksPanel {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public CustomActionLooksPanel( DataControlWithResources control ) {
			super( control );
			// TODO Parche, arreglar
			lookPanel.setPreferredSize( new Dimension( 0, 90 ) );

		}

		@Override
		protected void createPreview( ) {

		}

		@Override
		public void updatePreview( ) {
			if (getParent() != null && getParent().getParent() != null)
				getParent( ).getParent( ).repaint( );
		}

		public void updateResources( ) {
			super.updateResources( );
			if (getParent() != null && getParent().getParent() != null)
				getParent( ).getParent( ).repaint( );
		}

	}

	public int getType() {
	    return ActionTypePanel.CUSTOM_TYPE;
	}

	public boolean updateFields() {
	   boolean update = actionPanel.updateFields();
	    return update;
	}
	
}
