package es.eucm.eadventure.editor.gui.elementpanels.macro;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextPane;
import javax.swing.table.AbstractTableModel;

import es.eucm.eadventure.common.gui.TextConstants;
import es.eucm.eadventure.editor.control.controllers.macro.MacroListDataControl;

public class MacrosListPanel extends JPanel {

	/**
	 * Required.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Constructor.
	 * 
	 * @param macrosListDataControl
	 *            Scenes list controller
	 */
	public MacrosListPanel( MacroListDataControl macrosListDataControl ) {
		// Set the layout and the border
		setLayout( new GridBagLayout( ) );
		setBorder( BorderFactory.createTitledBorder( BorderFactory.createEtchedBorder( ), TextConstants.getText( "MacrosList.Title" ) ) );
		GridBagConstraints c = new GridBagConstraints( );
		c.insets = new Insets( 5, 5, 5, 5 );

		// Create the text area for the documentation
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 1;
		JTextPane informationTextPane = new JTextPane( );
		informationTextPane.setEditable( false );
		informationTextPane.setBackground( getBackground( ) );
		informationTextPane.setText( TextConstants.getText( "MacrosList.Information" ) );
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
		JTable informationTable = new JTable( new MacrosInfoTableModel( macrosListDataControl.getMacrosInfo() ) );
		informationTable.removeEditor( );
		JPanel listPanel = new JPanel( );
		listPanel.setBorder( BorderFactory.createTitledBorder( BorderFactory.createEtchedBorder( ), TextConstants.getText( "MacrosList.ListTitle" ) ) );
		listPanel.setLayout( new BorderLayout( ) );
		listPanel.add( new JScrollPane( informationTable, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER ), BorderLayout.CENTER );
		add( listPanel, c );
	}

	/**
	 * Table model to display the global states information.
	 */
	private class MacrosInfoTableModel extends AbstractTableModel {

		/**
		 * Required.
		 */
		private static final long serialVersionUID = 1L;

		/**
		 * Array of data to display.
		 */
		private String[][] macrosInfo;

		/**
		 * Constructor.
		 * 
		 * @param scenesInfo
		 *            Container array of the information of the scenes
		 */
		public MacrosInfoTableModel( String[][] scenesInfo ) {
			this.macrosInfo = scenesInfo;
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
			return macrosInfo.length;
		}

		@Override
		public String getColumnName( int columnIndex ) {
			String columnName = "";

			// The first column is the global state identifier
			if( columnIndex == 0 )
				columnName = TextConstants.getText( "MacrosList.ColumnHeader0" );

			// The second one is the number of references
			else if( columnIndex == 1 )
				columnName = TextConstants.getText( "MacrosList.ColumnHeader1" );

			return columnName;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see javax.swing.table.TableModel#getValueAt(int, int)
		 */
		public Object getValueAt( int rowIndex, int columnIndex ) {
			return macrosInfo[rowIndex][columnIndex];
		}
	}
}