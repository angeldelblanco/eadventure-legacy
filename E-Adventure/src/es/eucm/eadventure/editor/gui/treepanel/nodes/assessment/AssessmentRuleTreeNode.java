package es.eucm.eadventure.editor.gui.treepanel.nodes.assessment;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JPanel;

import es.eucm.eadventure.common.gui.TextConstants;
import es.eucm.eadventure.editor.control.Controller;
import es.eucm.eadventure.editor.control.controllers.DataControl;
import es.eucm.eadventure.editor.control.controllers.assessment.AssessmentProfileDataControl;
import es.eucm.eadventure.editor.control.controllers.assessment.AssessmentRuleDataControl;
import es.eucm.eadventure.editor.gui.elementpanels.assessment.AssessmentRulePanel;
import es.eucm.eadventure.editor.gui.treepanel.nodes.TreeNode;

public class AssessmentRuleTreeNode extends TreeNode{

	/**
	 * Contained micro-controller.
	 */
	private AssessmentRuleDataControl dataControl;

	/**
	 * The icon for this node class.
	 */
	private static Icon icon;

	/**
	 * Loads the icon of the node class.
	 */
	public static void loadIcon( ) {
		icon = new ImageIcon( "img/icons/assessmentRule.png" );
	}

	/**
	 * Constructor.
	 * 
	 * @param parent
	 *            Parent node
	 * @param dataControl
	 *            Contained game data
	 */
	public AssessmentRuleTreeNode( TreeNode parent, AssessmentRuleDataControl dataControl ) {
		super( parent );
		this.dataControl = dataControl;

		//children.add( new ScenesListTreeNode( this, dataControl.getScenesList( ) ) );
		
	}

	@Override
	public TreeNode checkForNewChild( int type ) {
		// This node don't accept new children
		return null;
	}

	@Override
	public void checkForDeletedReferences( ) {
		// Spread the call to the children
		for( TreeNode treeNode : children )
			treeNode.checkForDeletedReferences( );
	}

	@Override
	protected int getNodeType( ) {
		return Controller.ASSESSMENT_RULE;
	}

	@Override
	public DataControl getDataControl( ) {
		return dataControl;
	}

	@Override
	public Icon getIcon( ) {
		return icon;
	}

	@Override
	public String getToolTipText( ) {
		return TextConstants.getElementName(Controller.ASSESSMENT_RULE);
	}

	@Override
	public JComponent getEditPanel( ) {
		//return new ChapterPanel( dataControl );
		boolean scorm12 = ((AssessmentProfileDataControl)((AssessmentProfileTreeNode)parent).getDataControl()).isScorm12();
		boolean scorm2004 = ((AssessmentProfileDataControl)((AssessmentProfileTreeNode)parent).getDataControl()).isScorm2004();
		return new AssessmentRulePanel( dataControl,scorm12,scorm2004 );
	}

	@Override
	public String toString( ) {
		//return dataControl.getTitle( );
		return TextConstants.getElementName(Controller.ASSESSMENT_RULE)+":"+dataControl.getId();
	}

	@Override
	public TreeNode isObjectTreeNode(Object object) {
		if (dataControl == object)
			return this;
		return null;
	}
	
	@Override
	public TreeNode isObjectContentTreeNode(Object object) {
		if (dataControl.getContent() == object)
			return this;
		return null;
	}

}