package es.eucm.eadventure.editor.gui.metadatadialog.lomes;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;

import es.eucm.eadventure.common.gui.TextConstants;
import es.eucm.eadventure.editor.data.meta.auxiliar.LOMContribute;
import es.eucm.eadventure.editor.data.meta.auxiliar.LOMESContainer;
import es.eucm.eadventure.editor.data.meta.auxiliar.LOMIdentifier;
import es.eucm.eadventure.editor.data.meta.auxiliar.LOMRequirement;
import es.eucm.eadventure.editor.data.meta.auxiliar.LOMTaxon;
import es.eucm.eadventure.editor.data.meta.auxiliar.LOMTaxonPath;
import es.eucm.eadventure.editor.gui.metadatadialog.lomes.elementdialog.LOMRequirementsDialog;
import es.eucm.eadventure.editor.gui.metadatadialog.lomes.elementdialog.LOMTaxonDialog;
import es.eucm.eadventure.editor.gui.metadatadialog.lomes.elementdialog.LOMTaxonPathDialog;
import es.eucm.eadventure.editor.gui.metadatadialog.lomes.elementdialog.LOMlIdentifierDialog;
import es.eucm.eadventure.editor.gui.metadatadialog.lomes.elementdialog.LOMContributeDialog;


/**
 * Panel for container of compose types. 
 *
 */
public class LOMESCreateContainerPanel extends JPanel{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8233716462597295660L;

	private JButton add;
	
	private JButton delete;
	
	private JComboBox elements;
	
	private  LOMESContainer container;
	
	private int type;
	
	public LOMESCreateContainerPanel(LOMESContainer container,String title,int type){
		this.container = container;
		this.type = type;
		this.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 0;
		
		String[] containerElements = container.elements();
		String[] ele = new String[containerElements.length+1];
		ele[0] = TextConstants.getText("LOMES.AddElement");
		for (int i=0; i<containerElements.length;i++){
			ele[i+1] = containerElements[i];
		}
		
		elements = new JComboBox(ele);
		
		this.add(elements, c);
		
		add = new JButton(TextConstants.getText("LOMES.Add"));
		add.addActionListener( new AddButtonListener());
		
		c.gridx++;
		c.fill = GridBagConstraints.NONE;
		
		this.add(add, c);
		
		delete = new JButton(TextConstants.getText("LOMES.Delete"));
		delete.addActionListener( new DeleteButtonListener ());
		
		c.gridx ++;
		this.add(delete, c);
		
		this.setBorder(BorderFactory.createTitledBorder( BorderFactory.createEtchedBorder( ), title )); 
		
		
		
	}
	
	
	
	/**
	 * Listener for the "Delete" button
	 */
	private class DeleteButtonListener implements ActionListener {

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
		 */
		public void actionPerformed( ActionEvent arg0 ) {
			if (elements.getSelectedIndex()!=0){
				container.delete(elements.getSelectedIndex()-1);
				elements.removeItemAt(elements.getSelectedIndex());		
			}
		}
	}
	
	/**
	 * Listener for the "Add" button
	 */
	private class AddButtonListener implements ActionListener {

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
		 */
		public void actionPerformed( ActionEvent arg0 ) {
			if (container instanceof LOMIdentifier){
				int selectedIndex = elements.getSelectedIndex();
				LOMlIdentifierDialog idDialog = new LOMlIdentifierDialog(container,selectedIndex);
				//only add new element if it in not empty
				if (!idDialog.getCatalog().equals("")&&!idDialog.getEntry().equals("")){
					((LOMIdentifier)container).addIdentifier(idDialog.getCatalog(),idDialog.getEntry(),selectedIndex);
				if (selectedIndex==0)
					elements.addItem(idDialog.getEntry());
				else {
					elements.removeItemAt(selectedIndex);
					elements.insertItemAt(idDialog.getEntry(), selectedIndex);
				}
				}
			}else if (container instanceof LOMContribute){
				int selectedIndex = elements.getSelectedIndex();
				LOMContributeDialog idDialog = new LOMContributeDialog(container,selectedIndex,type);
				
				if (idDialog.getEntityValue().size()!=0){
					((LOMContribute)container).addContribute(idDialog.getRoleValue(),idDialog.getEntityValue(),idDialog.getDateValue(),selectedIndex);
				if (selectedIndex==0)
					elements.addItem(idDialog.getRoleValue().getValue());
				else {
					elements.removeItemAt(selectedIndex);
					elements.insertItemAt(idDialog.getRoleValue().getValue(), selectedIndex);
				}
				}
			}else if (container instanceof LOMRequirement){
				int selectedIndex = elements.getSelectedIndex();
				LOMRequirementsDialog idDialog = new LOMRequirementsDialog(container,selectedIndex);
				
				
				((LOMRequirement)container).addOrComposite(idDialog.getTypeValue(),idDialog.getNameValue(),idDialog.getMaxValue(),idDialog.getMinValue(),selectedIndex);
				if (selectedIndex==0)
					elements.addItem(idDialog.getTypeValue().getValue());
				else {
					elements.removeItemAt(selectedIndex);
					elements.insertItemAt(idDialog.getTypeValue().getValue(), selectedIndex);
				}
				
			}else if (container instanceof LOMTaxonPath){
				int selectedIndex = elements.getSelectedIndex();
				LOMTaxonPathDialog idDialog = new LOMTaxonPathDialog(container,selectedIndex);
				
				
				((LOMTaxonPath)container).addTaxonPath(idDialog.getSourceValue(),idDialog.getTaxonValue(), selectedIndex);
				if (selectedIndex==0)
					elements.addItem(idDialog.getSourceValue().getValue(0));
				else {
					elements.removeItemAt(selectedIndex);
					elements.insertItemAt(idDialog.getSourceValue().getValue(0), selectedIndex);
				}
				
			}else if (container instanceof LOMTaxon){
				int selectedIndex = elements.getSelectedIndex();
				LOMTaxonDialog idDialog = new LOMTaxonDialog(container,selectedIndex);
				
				
				((LOMTaxon)container).addTaxon(idDialog.getIdValue(), idDialog.getEntryValue(), selectedIndex);
				if (selectedIndex==0)
					elements.addItem(idDialog.getIdValue());
				else {
					elements.removeItemAt(selectedIndex);
					elements.insertItemAt(idDialog.getIdValue(), selectedIndex);
				}
				
			}
		}
	}
	
	
}