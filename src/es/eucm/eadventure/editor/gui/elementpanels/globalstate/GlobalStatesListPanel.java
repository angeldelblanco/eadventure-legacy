package es.eucm.eadventure.editor.gui.elementpanels.globalstate;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextPane;
import javax.swing.table.AbstractTableModel;

import es.eucm.eadventure.common.gui.TextConstants;
import es.eucm.eadventure.editor.control.controllers.DataControl;
import es.eucm.eadventure.editor.control.controllers.globalstate.GlobalStateListDataControl;
import es.eucm.eadventure.editor.gui.treepanel.TreeNodeControl;

public class GlobalStatesListPanel extends JPanel {

	/**
	 * Required.
	 */
	private static final long serialVersionUID = 1L;
	
	private GlobalStateListDataControl globalStateListDataControl;

	/**
	 * Constructor.
	 * 
	 * @param gloabalStatesListDataControl
	 *            Scenes list controller
	 */
	public GlobalStatesListPanel( GlobalStateListDataControl gloabalStatesListDataControl ) {
		this.globalStateListDataControl = gloabalStatesListDataControl;
		// Set the layout and the border
		setLayout( new GridBagLayout( ) );
		setBorder( BorderFactory.createTitledBorder( BorderFactory.createEtchedBorder( ), TextConstants.getText( "GlobalStatesList.Title" ) ) );
		GridBagConstraints c = new GridBagConstraints( );
		c.insets = new Insets( 5, 5, 5, 5 );

		// Create the text area for the documentation
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 1;
		JTextPane informationTextPane = new JTextPane( );
		informationTextPane.setEditable( false );
		informationTextPane.setBackground( getBackground( ) );
		informationTextPane.setText( TextConstants.getText( "GlobalStatesList.Information" ) );
		JPanel informationPanel = new JPanel( );
		informationPanel.setBorder( BorderFactory.createTitledBorder( BorderFactory.createEtchedBorder( ), TextConstants.getText( "GeneralText.Information" ) ) );
		informationPanel.setLayout( new BorderLayout( ) );
		informationPanel.add( informationTextPane, BorderLayout.CENTER );
		add( informationPanel, c );

		// Create the table with the data
		c.gridy = 2;
		c.fill = GridBagConstraints.BOTH;
		c.weightx = 1;
		c.weighty = 1;
		JTable informationTable = new JTable( new GlobalStatesInfoTableModel( gloabalStatesListDataControl.getGlobalStatesInfo() ) );
		informationTable.removeEditor( );
		informationTable.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2) {
					JTable table = (JTable) e.getSource();
					DataControl dataControl = GlobalStatesListPanel.this.globalStateListDataControl.getGlobalStates().get(table.getSelectedRow());
					TreeNodeControl.getInstance().changeTreeNode(dataControl);
				}
			}
		});
		JPanel listPanel = new JPanel( );
		listPanel.setBorder( BorderFactory.createTitledBorder( BorderFactory.createEtchedBorder( ), TextConstants.getText( "GlobalStatesList.ListTitle" ) ) );
		listPanel.setLayout( new BorderLayout( ) );
		listPanel.add( new JScrollPane( informationTable, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER ), BorderLayout.CENTER );
		add( listPanel, c );
	}

	/**
	 * Table model to display the global states information.
	 */
	private class GlobalStatesInfoTableModel extends AbstractTableModel {

		/**
		 * Required.
		 */
		private static final long serialVersionUID = 1L;

		/**
		 * Array of data to display.
		 */
		private String[][] globalStatesInfo;

		/**
		 * Constructor.
		 * 
		 * @param scenesInfo
		 *            Container array of the information of the scenes
		 */
		public GlobalStatesInfoTableModel( String[][] scenesInfo ) {
			this.globalStatesInfo = scenesInfo;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see javax.swing.table.TableModel#getColumnCount()
		 */
		public int getColumnCount( ) {
			// Two columns, always
			return 2;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see javax.swing.table.TableModel#getRowCount()
		 */
		public int getRowCount( ) {
			return globalStatesInfo.length;
		}

		@Override
		public String getColumnName( int columnIndex ) {
			String columnName = "";

			// The first column is the global state identifier
			if( columnIndex == 0 )
				columnName = TextConstants.getText( "GlobalStatesList.ColumnHeader0" );

			// The second one is the number of references
			else if( columnIndex == 1 )
				columnName = TextConstants.getText( "GlobalStatesList.ColumnHeader1" );

			return columnName;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see javax.swing.table.TableModel#getValueAt(int, int)
		 */
		public Object getValueAt( int rowIndex, int columnIndex ) {
			return globalStatesInfo[rowIndex][columnIndex];
		}
	}
}