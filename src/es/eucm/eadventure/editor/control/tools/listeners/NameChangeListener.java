package es.eucm.eadventure.editor.control.tools.listeners;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.JTextComponent;

import es.eucm.eadventure.common.data.Named;
import es.eucm.eadventure.editor.control.Controller;
import es.eucm.eadventure.editor.control.tools.Tool;
import es.eucm.eadventure.editor.control.tools.general.ChangeNameTool;

public class NameChangeListener implements DocumentListener {

	private Named named;
	
	private JTextComponent textComponent;
	
	public NameChangeListener(JTextComponent textComponent, Named named) {
		this.textComponent = textComponent;
		this.named = named;
	}
	
	@Override
	public void changedUpdate(DocumentEvent e) {
		// DO NOTHING
	}

	@Override
	public void insertUpdate(DocumentEvent e) {
		Tool tool = new ChangeNameTool(named, textComponent.getText());
		Controller.getInstance().addTool(tool);
	}

	@Override
	public void removeUpdate(DocumentEvent e) {
		Tool tool = new ChangeNameTool(named, textComponent.getText());
		Controller.getInstance().addTool(tool);
	}

}
