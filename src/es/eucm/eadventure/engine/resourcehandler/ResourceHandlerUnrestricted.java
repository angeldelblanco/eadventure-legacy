package es.eucm.eadventure.engine.resourcehandler;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

import javax.media.MediaLocator;

import de.schlichtherle.io.File;
import es.eucm.eadventure.engine.resourcehandler.zipurl.ZipURL;

/**
 * This resource handler loads files for being used on a standard java application
 */
class ResourceHandlerUnrestricted extends ResourceHandler {
    
    /**
     * Singleton
     */
    private static ResourceHandlerUnrestricted instance;

    /*
     *  (non-Javadoc)
     * @see es.eucm.eadventure.engine.resourcehandler.ResourceHandler#getInstance()
     */
    public static ResourceHandler getInstance( ) {
        return instance;
    }
    public static void create(){
        instance = new ResourceHandlerUnrestricted( );
    }
    public static void delete(){
        if (instance!=null && instance.tempFiles!=null){
            for (File file: instance.tempFiles){
                file.delete( );
            }
        }
        instance = null;
    }

    /**
     * Empty constructor
     */
    private ResourceHandlerUnrestricted( ) {
    }
    
    /*
     *  (non-Javadoc)
     * @see es.eucm.eadventure.engine.resourcehandler.ResourceHandler#setZipFile(java.lang.String)
     */
    public void setZipFile( String zipFilename ) {
        try {
            this.zipPath=zipFilename;
            zipFile = new ZipFile( zipFilename );
            /*Enumeration entries = zipFile.entries( ); 
            int n =0;
            while (entries.hasMoreElements( )){
                n++;
                ZipEntry entry = (ZipEntry)entries.nextElement( );
                System.out.println( n+" "+entry.getName( )+" - "+(entry.isDirectory( )?"DIR":""));
            }*/
        } catch( ZipException e ) {
            e.printStackTrace( );
        } catch( IOException e ) {
            e.printStackTrace( );
        }
    }
    
    /*
     *  (non-Javadoc)
     * @see es.eucm.eadventure.engine.resourcehandler.ResourceHandler#getOutputStream(java.lang.String)
     */
    public OutputStream getOutputStream( String path ) {
        OutputStream os = null;

        if( path.startsWith( "/" ) ) {
            path = path.substring( 1 );
        }

        try {
            os = new FileOutputStream( path );
        } catch( SecurityException e ) {
            e.printStackTrace( );
        } catch( FileNotFoundException e ) {
            os = null;
        }

        return os;
    }

    /*
     *  (non-Javadoc)
     * @see es.eucm.eadventure.engine.resourcehandler.ResourceHandler#getResourceAsStream(java.lang.String)
     */
    public InputStream getResourceAsStream( String path ) {

        InputStream is = null;

        if( path.startsWith( "/" ) ) {
            path = path.substring( 1 );
        }

        try {
            is = new FileInputStream( path );
        } catch( SecurityException e ) {
            e.printStackTrace( );
        } catch( FileNotFoundException e ) {
            is = null;
        }

        return is;
    }
    @Override
    public MediaLocator getResourceAsMediaLocator( String path ) {
        // Add the file
        String absolutePath=generateTempFileAbsolutePath( getExtension(path ) );
        File sourceFile = new File( zipPath, path );
        File destinyFile = new File( absolutePath );
        if( sourceFile.exists( ) )
            sourceFile.copyTo( destinyFile );
        
        
        if (destinyFile.exists( ))
            try {
                
                MediaLocator mediaLocator= new MediaLocator(destinyFile.toURI( ).toURL( ));
                tempFiles.add( destinyFile );
                return mediaLocator;
            } catch( MalformedURLException e ) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return null;
            }
        else
            return null;
    }
    
    public URL getResourceAsURLFromZip( String path ){
        try {
            return ZipURL.createAssetURL( zipPath, path );
        } catch( MalformedURLException e ) {
            return null;
        }
    }
}
