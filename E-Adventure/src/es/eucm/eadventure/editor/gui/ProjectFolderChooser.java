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
package es.eucm.eadventure.editor.gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Font;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.accessibility.AccessibleContext;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import es.eucm.eadventure.common.auxiliar.ReleaseFolders;
import es.eucm.eadventure.common.gui.TC;
import es.eucm.eadventure.editor.auxiliar.filefilters.FolderFileFilter;

public class ProjectFolderChooser extends JFileChooser {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private JTextField projectName = null;

    private static File getDefaultSelectedFile( ) {

        String defaultName = TC.get( "Operation.NewFileTitle" );
        File parentDir = getProjectsFolder( );
        int i = 0;
        while( new File( parentDir, defaultName ).exists( ) ) {
            i++;
            defaultName = TC.get( "Operation.NewFileTitle" ) + " (" + i + ")";
        }

        return new File( defaultName );

    }

    /*	@Override
    	public File getSelectedFile() {
    		File temp = super.getSelectedFile();
    		
    		if (projectName != null) {
    			temp = new File(this.getCurrentDirectory().getAbsolutePath() + File.separatorChar + projectName.getText());
    		}
    		
    		return temp;
    	}
    */
    private static File getProjectsFolder( ) {

        File parentDir = ReleaseFolders.projectsFolder( );
        if( !parentDir.exists( ) )
            parentDir.mkdirs( );
        return parentDir;
    }

    public ProjectFolderChooser( boolean checkName, boolean checkDescriptor ) {

        super( getProjectsFolder( ) );
        super.setDialogTitle( TC.get( "Operation.NewProjectTitle" ) );
        super.setMultiSelectionEnabled( false );
        super.setFileSelectionMode( JFileChooser.FILES_AND_DIRECTORIES );
        for( javax.swing.filechooser.FileFilter filter : super.getChoosableFileFilters( ) ) {
            super.removeChoosableFileFilter( filter );
        }
        FolderFileFilter filter = new FolderFileFilter( checkName, checkDescriptor, this );
        super.addChoosableFileFilter( filter );
        super.setFileFilter( filter );
        super.setFileHidingEnabled( true );
        //super.setSelectedFile( new File ( Controller.projectsFolder(),  TextConstants.getText("GeneralText.NewProjectFolder") ) );
        super.setSelectedFile( getDefaultSelectedFile( ) );
        super.setAcceptAllFileFilterUsed( false );
        preparePanel();
    }

    
    private void preparePanel(){
        String title = TC.get( "Operation.NewProjectTitle" );
        putClientProperty( AccessibleContext.ACCESSIBLE_DESCRIPTION_PROPERTY, title );

       JPanel mainPanel = new JPanel();
        
        JPanel infoPanel = new JPanel( );
        infoPanel.setLayout( new BorderLayout( ) );
        JTextArea info = new JTextArea( );
        info.setColumns( 10 );
        info.setWrapStyleWord( true );
        info.setFont( new Font( Font.SERIF, Font.PLAIN, 12 ) );
        info.setEditable( false );
        info.setBackground( infoPanel.getBackground( ) );
        info.setText( TC.get( "Operation.NewProjectMessage", FolderFileFilter.getAllowedChars( ) ) );
        infoPanel.add( info, BorderLayout.NORTH );

        String os = System.getProperty( "os.name" );
        if( os.contains( "MAC" ) || os.contains( "mac" ) || os.contains( "Mac" ) ) {
            projectName = new JTextField( 50 );
            projectName.setText( ProjectFolderChooser.getDefaultSelectedFile( ).getName( ) );
            JPanel tempName = new JPanel( );
            tempName.add( new JLabel( TC.get( "Operation.NewProjectName" ) ) );
            tempName.add( projectName );
            JButton create = new JButton( TC.get( "Operation.CreateNewProject" ) );
            create.addActionListener( new ActionListener( ) {

                public void actionPerformed( ActionEvent arg0 ) {

                    if( projectName.getText( ) != null ) {
                        String name = projectName.getText( );
                        if( !name.endsWith( ".eap" ) )
                            name = name + ".eap";
                        File file = new File( ProjectFolderChooser.this.getCurrentDirectory( ).getAbsolutePath( ) + File.separatorChar + name );
                        if( !file.exists( ) ) {
                            try {
                                file.createNewFile( );
                                ProjectFolderChooser.this.updateUI( );
                                ProjectFolderChooser.this.setSelectedFile( file );
                                ProjectFolderChooser.this.approveSelection( );
                            }
                            catch( Exception e ) {
                            }
                        }
                    }
                }
            } );
            tempName.add( create );
            infoPanel.add( tempName, BorderLayout.SOUTH );
        }

       
        mainPanel.setLayout( new BorderLayout( ) );
        mainPanel.add( createOpenFilePanel(), BorderLayout.CENTER );
        mainPanel.add( infoPanel, BorderLayout.NORTH );

        add( mainPanel );

    }
    
    private JPanel createOpenFilePanel( ) {

        JPanel panelOpen = new JPanel( );

        // Transfer the elements in the JFileChooser to the open file panel 

        LayoutManager layout = getLayout( );
        if( layout instanceof BorderLayout ) {
            panelOpen.setLayout( new BorderLayout( ) );
            BorderLayout currentLayout = (BorderLayout) getLayout( );

            for( Component comp : getComponents( ) ) {
                panelOpen.add( comp, currentLayout.getConstraints( comp ) );
            }
        }
        else if( layout instanceof BoxLayout ) {
            BoxLayout currentLayout = (BoxLayout) getLayout( );
            panelOpen.setLayout( new BoxLayout( panelOpen, currentLayout.getAxis( ) ) );

            for( Component comp : getComponents( ) ) {
                panelOpen.add( comp );
            }
        }

        return panelOpen;
    }
    
    
    // TODO delete next code: 
    
    /*@Override
    protected JDialog createDialog( Component parent ) throws HeadlessException {

        String title = TC.get( "Operation.NewProjectTitle" );
        putClientProperty( AccessibleContext.ACCESSIBLE_DESCRIPTION_PROPERTY, title );

        JDialog dialog;
        Window window = JOptionPane.getFrameForComponent( parent );
        //Window window = JOptionPane.getWindowForComponent(parent);
        if( window instanceof Frame ) {
            dialog = new JDialog( (Frame) window, title, true );
        }
        else {
            dialog = new JDialog( (Dialog) window, title, true );
        }
        dialog.setComponentOrientation( this.getComponentOrientation( ) );

        JPanel infoPanel = new JPanel( );
        infoPanel.setLayout( new BorderLayout( ) );
        JTextArea info = new JTextArea( );
        info.setColumns( 10 );
        info.setWrapStyleWord( true );
        info.setFont( new Font( Font.SERIF, Font.PLAIN, 12 ) );
        info.setEditable( false );
        info.setBackground( infoPanel.getBackground( ) );
        info.setText( TC.get( "Operation.NewProjectMessage", FolderFileFilter.getAllowedChars( ) ) );
        infoPanel.add( info, BorderLayout.NORTH );

        String os = System.getProperty( "os.name" );
        if( os.contains( "MAC" ) || os.contains( "mac" ) || os.contains( "Mac" ) ) {
            projectName = new JTextField( 50 );
            projectName.setText( ProjectFolderChooser.getDefaultSelectedFile( ).getName( ) );
            JPanel tempName = new JPanel( );
            tempName.add( new JLabel( TC.get( "Operation.NewProjectName" ) ) );
            tempName.add( projectName );
            JButton create = new JButton( TC.get( "Operation.CreateNewProject" ) );
            create.addActionListener( new ActionListener( ) {

                public void actionPerformed( ActionEvent arg0 ) {

                    if( projectName.getText( ) != null ) {
                        String name = projectName.getText( );
                        if( !name.endsWith( ".eap" ) )
                            name = name + ".eap";
                        File file = new File( ProjectFolderChooser.this.getCurrentDirectory( ).getAbsolutePath( ) + File.separatorChar + name );
                        if( !file.exists( ) ) {
                            try {
                                file.createNewFile( );
                                ProjectFolderChooser.this.updateUI( );
                                ProjectFolderChooser.this.setSelectedFile( file );
                                ProjectFolderChooser.this.approveSelection( );
                            }
                            catch( Exception e ) {
                            }
                        }
                    }
                }
            } );
            tempName.add( create );
            infoPanel.add( tempName, BorderLayout.SOUTH );
        }

        Container contentPane = dialog.getContentPane( );
        contentPane.setLayout( new BorderLayout( ) );
        contentPane.add( this, BorderLayout.CENTER );
        contentPane.add( infoPanel, BorderLayout.NORTH );

        if( JDialog.isDefaultLookAndFeelDecorated( ) ) {
            boolean supportsWindowDecorations = UIManager.getLookAndFeel( ).getSupportsWindowDecorations( );
            if( supportsWindowDecorations ) {
                dialog.getRootPane( ).setWindowDecorationStyle( JRootPane.FILE_CHOOSER_DIALOG );
            }
        }
        dialog.pack( );
        dialog.setLocationRelativeTo( parent );

        return dialog;
    }*/

}
