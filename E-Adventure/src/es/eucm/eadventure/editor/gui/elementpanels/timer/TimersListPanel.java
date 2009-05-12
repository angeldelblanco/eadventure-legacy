package es.eucm.eadventure.editor.gui.elementpanels.timer;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;

import es.eucm.eadventure.common.gui.TextConstants;
import es.eucm.eadventure.editor.control.Controller;
import es.eucm.eadventure.editor.control.controllers.Searchable;
import es.eucm.eadventure.editor.control.controllers.timer.TimerDataControl;
import es.eucm.eadventure.editor.control.controllers.timer.TimersListDataControl;
import es.eucm.eadventure.editor.control.tools.timer.AddTimerTool;
import es.eucm.eadventure.editor.control.tools.timer.DeleteTimerTool;
import es.eucm.eadventure.editor.control.tools.timer.DuplicateTimerTool;
import es.eucm.eadventure.editor.gui.DataControlsPanel;
import es.eucm.eadventure.editor.gui.Updateable;
import es.eucm.eadventure.editor.gui.auxiliar.components.JFiller;
import es.eucm.eadventure.editor.gui.elementpanels.general.TableScrollPane;

public class TimersListPanel extends JPanel implements DataControlsPanel, Updateable {

	/**
	 * Required.
	 */
	private static final long serialVersionUID = 1L;

	private static final int HORIZONTAL_SPLIT_POSITION = 100;

	private TimersListDataControl dataControl;
	
	private JPanel timerInfoPanel;
	
	private JTable table;
	
	private JButton deleteButton;
	
	private JButton duplicateButton;
	
	/**
	 * Constructor.
	 * 
	 * @param timersListDataControl
	 *            timers list controller
	 */
	public TimersListPanel( TimersListDataControl timersListDataControl ) {
		this.dataControl = timersListDataControl;
		setLayout( new BorderLayout( ) );
		
		timerInfoPanel = new JPanel();
		timerInfoPanel.setLayout(new BorderLayout());
		
		JPanel tablePanel = createTablePanel(timerInfoPanel);
		
		JSplitPane tableWithSplit = new JSplitPane(JSplitPane.VERTICAL_SPLIT, tablePanel, timerInfoPanel);
		tableWithSplit.setOneTouchExpandable(true);
		tableWithSplit.setDividerLocation(HORIZONTAL_SPLIT_POSITION);
		tableWithSplit.setContinuousLayout(true);
		tableWithSplit.setResizeWeight(0);
		tableWithSplit.setDividerSize(10);

		add(tableWithSplit, BorderLayout.CENTER);
	}
	
	
	private JPanel createTablePanel(JPanel timerInfoPanel) {
		JPanel tablePanel = new JPanel();
		
		table = new TimersTable(dataControl, this);
		JScrollPane scroll = new TableScrollPane(table, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scroll.setMinimumSize(new Dimension(0, 	HORIZONTAL_SPLIT_POSITION));

		table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent arg0) {
				if (table.getSelectedRow() >= 0) {
					deleteButton.setEnabled(true);
					duplicateButton.setEnabled(true);
				}
				else {
					deleteButton.setEnabled(false);
					duplicateButton.setEnabled(true);
				}
				updateInfoPanel(table.getSelectedRow());
				deleteButton.repaint();
			}			
		});
		
		JPanel buttonsPanel = new JPanel();
		JButton newButton = new JButton(new ImageIcon("img/icons/addNode.png"));
		newButton.setContentAreaFilled( false );
		newButton.setMargin( new Insets(0,0,0,0) );
		newButton.setBorder(BorderFactory.createEmptyBorder());
		newButton.setToolTipText( TextConstants.getText( "TimersList.AddTimer" ) );
		newButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				addTimer();
			}
		});
		deleteButton = new JButton(new ImageIcon("img/icons/deleteNode.png"));
		deleteButton.setContentAreaFilled( false );
		deleteButton.setMargin( new Insets(0,0,0,0) );
		deleteButton.setBorder(BorderFactory.createEmptyBorder());
		deleteButton.setToolTipText( TextConstants.getText( "TimersList.DeleteTimer" ) );
		deleteButton.setEnabled(false);
		deleteButton.addActionListener(new ActionListener(){
			public void actionPerformed( ActionEvent e ) {
				deleteTimer();
			}
		});
		duplicateButton = new JButton(new ImageIcon("img/icons/duplicateNode.png"));
		duplicateButton.setContentAreaFilled( false );
		duplicateButton.setMargin( new Insets(0,0,0,0) );
		duplicateButton.setBorder(BorderFactory.createEmptyBorder());
		duplicateButton.setToolTipText( TextConstants.getText( "TimersList.DuplicateTimer" ) );
		duplicateButton.setEnabled(false);
		duplicateButton.addActionListener(new ActionListener(){
			public void actionPerformed( ActionEvent e ) {
				duplicateTimer();
			}
		});
		buttonsPanel.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 0;
		buttonsPanel.add(newButton, c);
		c.gridy = 1;
		buttonsPanel.add(duplicateButton, c);
		c.gridy = 3;
		buttonsPanel.add(deleteButton, c);
		c.gridy = 2;
		c.weighty = 2.0;
		c.fill = GridBagConstraints.VERTICAL;
		buttonsPanel.add(new JFiller(), c);
		
		tablePanel.setLayout(new BorderLayout());
		tablePanel.add(scroll, BorderLayout.CENTER);
		tablePanel.add(buttonsPanel, BorderLayout.EAST);
		
		return tablePanel;
	}
	
	
	public void updateInfoPanel(int row) {
		timerInfoPanel.removeAll();		
		if (row >= 0) {
			TimerDataControl timer = dataControl.getTimers().get(row);
			JPanel timerPanel = new TimerPanel(timer);
			timerInfoPanel.add(timerPanel);
		}
		timerInfoPanel.updateUI();
	}
	
	protected void addTimer() {
		Controller.getInstance().addTool(new AddTimerTool(dataControl));
		((AbstractTableModel) table.getModel()).fireTableDataChanged();
		table.changeSelection(dataControl.getTimers().size() - 1, 0, false, false);
	}

	protected void duplicateTimer() {
		Controller.getInstance().addTool(new DuplicateTimerTool(dataControl, table.getSelectedRow()));
		((AbstractTableModel) table.getModel()).fireTableDataChanged();
		table.changeSelection(dataControl.getTimers().size() - 1, 0, false, false);
	}

	protected void deleteTimer() {
		Controller.getInstance().addTool(new DeleteTimerTool(dataControl, table.getSelectedRow()));
		table.clearSelection();
		((AbstractTableModel) table.getModel()).fireTableDataChanged();
	}
	
	public void setSelectedItem(List<Searchable> path) {
		if (path.size() > 0) {
			for (int i = 0 ; i < dataControl.getTimers().size(); i++) {
				if (dataControl.getTimers().get(i) == path.get(path.size() -1))
					table.changeSelection(i, i, false, false);
			}
		}
	}

	public boolean updateFields() {
		int selected = table.getSelectedRow();
		int items = table.getRowCount();
		if (table.getCellEditor() != null)
			table.getCellEditor().cancelCellEditing();
		
		((AbstractTableModel) table.getModel()).fireTableDataChanged();
		
		if (items > 0 && items == dataControl.getTimers().size()) {
			if (selected != -1 && selected < table.getRowCount()) {
				table.changeSelection(selected, 0, false, false);
				updateInfoPanel(table.getSelectedRow());
			}
		}
		return true;
	}

}
