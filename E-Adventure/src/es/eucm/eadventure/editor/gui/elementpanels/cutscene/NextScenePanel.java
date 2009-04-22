package es.eucm.eadventure.editor.gui.elementpanels.cutscene;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSpinner;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import es.eucm.eadventure.common.gui.TextConstants;
import es.eucm.eadventure.editor.control.Controller;
import es.eucm.eadventure.editor.control.controllers.cutscene.CutsceneDataControl;
import es.eucm.eadventure.editor.control.controllers.general.NextSceneDataControl;
import es.eucm.eadventure.editor.gui.editdialogs.EffectsDialog;
import es.eucm.eadventure.editor.gui.editdialogs.PlayerPositionDialog;


public class NextScenePanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 9164972283091760862L;

	private CutsceneDataControl dataControl;

	private JRadioButton returnToPrevious;
	private JRadioButton goToNewScene;
	private JRadioButton endChapter;
	private JComboBox nextSceneCombo;
	private JCheckBox usePosition;
	private JButton setPosition;
	private JButton editEffects;
	private JComboBox transition;
	private JSpinner timeSpinner;

	public NextScenePanel(CutsceneDataControl cutsceneDataControl) {
		this.dataControl = cutsceneDataControl;
		
		setBorder( BorderFactory.createTitledBorder( BorderFactory.createEtchedBorder( ), TextConstants.getText( "Cutscene.CutsceneEndReached" ) ) );

		returnToPrevious = new JRadioButton(TextConstants.getText("Cutscene.ReturnToLastScene"));
		goToNewScene = new JRadioButton(TextConstants.getText("Cutscene.GoToNextScene"));
		endChapter = new JRadioButton(TextConstants.getText("Cutscene.ChapterEnd"));
		returnToPrevious.addActionListener(new ReturnToPreviousActionListener());
		goToNewScene.addActionListener(new GoToNewSceneActionListener());
		endChapter.addActionListener(new EndChapterActionListener());
		
		ButtonGroup group = new ButtonGroup();
		group.add(returnToPrevious);
		group.add(goToNewScene);
		group.add(endChapter);
		
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new GridLayout(1,3));
		buttonPanel.add(returnToPrevious);
		buttonPanel.add(goToNewScene);
		buttonPanel.add(endChapter);

		setLayout(new BorderLayout());
		add(buttonPanel, BorderLayout.NORTH);

		JPanel detailsPanel = new JPanel();
		
		nextSceneCombo = new JComboBox(Controller.getInstance().getIdentifierSummary().getGeneralSceneIds());
		nextSceneCombo.addActionListener( new NextSceneComboBoxListener( ) );

		JPanel positionPanel = new JPanel();
		positionPanel.setLayout(new GridBagLayout());
		GridBagConstraints posC = new GridBagConstraints();
		posC.gridx = 0;
		posC.gridy = 0;
		usePosition = new JCheckBox(TextConstants.getText( "NextScene.UseDestinyPosition" ));
		usePosition.addActionListener( new DestinyPositionCheckBoxListener());

		setPosition = new JButton(TextConstants.getText( "NextScene.EditDestinyPositionShort" ));
		setPosition.addActionListener(new DestinyPositionButtonListener());
		positionPanel.add(usePosition, posC);
		posC.gridx++;
		positionPanel.add(setPosition, posC);
		
		editEffects = new JButton(TextConstants.getText( "GeneralText.EditEffects" ));
		editEffects.addActionListener(new EffectsButtonListener());

		JPanel transitionPanel = new JPanel();
		String[] options = new String[]{ TextConstants.getText("NextScene.NoTransition"),
				TextConstants.getText("NextScene.TopToBottom"),
				TextConstants.getText("NextScene.BottomToTop"),
				TextConstants.getText("NextScene.LeftToRight"),
				TextConstants.getText("NextScene.RightToLeft"),
				TextConstants.getText("NextScene.FadeIn")};
		transition = new JComboBox(options);
		transition.addActionListener(new TransitionComboChangeListener());
		
	    SpinnerModel sm = new SpinnerNumberModel(0, 0, 5000, 100);
		timeSpinner = new JSpinner(sm);
		timeSpinner.addChangeListener(new TransitionSpinnerChangeListener());
		transitionPanel.add(transition);
		transitionPanel.add(timeSpinner);
		
		detailsPanel.setLayout(new GridLayout(4,1));
		JPanel temp = new JPanel();
		temp.add(nextSceneCombo);
		detailsPanel.add(temp);
		detailsPanel.add(positionPanel);
		temp = new JPanel();
		temp.add(editEffects);
		detailsPanel.add(temp);
		detailsPanel.add(transitionPanel);
		
		add(detailsPanel, BorderLayout.CENTER);
		
		updateNextSceneInfo();
		
	}
	
	private void updateNextSceneInfo() {
		boolean enablePosition = false;
		boolean enablePositionButton = false;
		
		if (dataControl.isEndScene()) {
			endChapter.setSelected(true);
		} else if (dataControl.getNextScenes().size() == 0) {
			returnToPrevious.setSelected(true);
		} else {
			goToNewScene.setSelected(true);
			NextSceneDataControl nextScene = dataControl.getNextScenes().get(0);
			nextSceneCombo.setSelectedItem(nextScene.getNextSceneId());
			usePosition.setSelected(nextScene.hasDestinyPosition());
			transition.setSelectedIndex(nextScene.getTransitionType());
			timeSpinner.setValue(nextScene.getTransitionTime());
			enablePosition = Controller.getInstance( ).getIdentifierSummary( ).isScene( nextScene.getNextSceneId( ) );
			enablePositionButton = enablePosition && nextScene.hasDestinyPosition();
		}
		nextSceneCombo.setEnabled(goToNewScene.isSelected());
		usePosition.setEnabled(enablePosition && goToNewScene.isSelected());
		setPosition.setEnabled(enablePositionButton && goToNewScene.isSelected());
		editEffects.setEnabled(goToNewScene.isSelected());
		transition.setEnabled(goToNewScene.isSelected());
		timeSpinner.setEnabled(goToNewScene.isSelected());
		updateUI();
	}

	
	/**
	 * Listener for next scene combo box.
	 */
	private class NextSceneComboBoxListener implements ActionListener {
		public void actionPerformed( ActionEvent e ) {
			if (dataControl.getNextScenes().size() == 0)
				return;
			
			NextSceneDataControl nextSceneDataControl = dataControl.getNextScenes().get(0);
			nextSceneDataControl.setNextSceneId( nextSceneCombo.getSelectedItem( ).toString( ) );
			updateNextSceneInfo();
		}
	}

	/**
	 * Listener for the "Use destiny position in this next scene" check box.
	 */
	private class DestinyPositionCheckBoxListener implements ActionListener {
		public void actionPerformed( ActionEvent e ) {
			if (dataControl.getNextScenes().size() == 0)
				return;
			
			NextSceneDataControl nextSceneDataControl = dataControl.getNextScenes().get(0);
			nextSceneDataControl.toggleDestinyPosition( );
			updateNextSceneInfo();
		}
	}

	/**
	 * Listener for the "Set destiny player position" button
	 */
	private class DestinyPositionButtonListener implements ActionListener {
		public void actionPerformed( ActionEvent arg0 ) {
			if (dataControl.getNextScenes().size() == 0)
				return;
			
			NextSceneDataControl nextSceneDataControl = dataControl.getNextScenes().get(0);
			PlayerPositionDialog destinyPositionDialog = new PlayerPositionDialog( nextSceneCombo.getSelectedItem( ).toString( ), nextSceneDataControl.getDestinyPositionX( ), nextSceneDataControl.getDestinyPositionY( ) );
			nextSceneDataControl.setDestinyPosition( destinyPositionDialog.getPositionX( ), destinyPositionDialog.getPositionY( ) );
		}
	}

	/**
	 * Listener for the edit effects button.
	 */
	private class EffectsButtonListener implements ActionListener {

		public void actionPerformed( ActionEvent e ) {
			if (dataControl.getNextScenes().size() == 0)
				return;
			
			NextSceneDataControl nextSceneDataControl = dataControl.getNextScenes().get(0);
			new EffectsDialog( nextSceneDataControl.getEffects( ) );
		}
	}
	
	private class TransitionComboChangeListener implements ActionListener {

		public void actionPerformed( ActionEvent e ) {
			if (dataControl.getNextScenes().size() == 0)
				return;
			
			NextSceneDataControl nextSceneDataControl = dataControl.getNextScenes().get(0);
			nextSceneDataControl.setTransitionType(transition.getSelectedIndex());
		}
	}
	
	private class TransitionSpinnerChangeListener implements ChangeListener {

		public void stateChanged(ChangeEvent e) {
			if (dataControl.getNextScenes().size() == 0)
				return;
			
			NextSceneDataControl nextSceneDataControl = dataControl.getNextScenes().get(0);
			nextSceneDataControl.setTransitionTime((Integer) timeSpinner.getValue());					
		}
	}
	

	private class ReturnToPreviousActionListener implements ActionListener {
		public void actionPerformed(ActionEvent arg0) {
			if (returnToPrevious.isSelected()) {
				if (dataControl.getNextScenes().size() >= 0) {
					NextSceneDataControl nextSceneDataControl = dataControl.getNextScenes().get(0);
					dataControl.deleteElement(nextSceneDataControl, false);
				}
				if (dataControl.isEndScene()) {
					dataControl.deleteElement(dataControl.getEndScene(), false);
				}
				updateNextSceneInfo();
			}
		}
	}
	
	private class GoToNewSceneActionListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if (goToNewScene.isSelected()) {
				if (dataControl.isEndScene()) {
					dataControl.deleteElement(dataControl.getEndScene(), false);
				}
				if (dataControl.getNextScenes().size() == 0)
					dataControl.addElement(Controller.NEXT_SCENE, (String) nextSceneCombo.getItemAt(0));
				updateNextSceneInfo();
			}
		}
	}
	
	private class EndChapterActionListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if (endChapter.isSelected()) {
				dataControl.addElement(Controller.END_SCENE, null);
				updateNextSceneInfo();
			}
		}
		
	}
}