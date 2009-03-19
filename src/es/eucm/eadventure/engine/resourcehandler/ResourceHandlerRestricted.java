package es.eucm.eadventure.engine.resourcehandler;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

import javax.media.MediaLocator;

/**
 * This resource handler loads files for being used on an applet
 */
class ResourceHandlerRestricted extends ResourceHandler {
    
    /**
     * Singleton
     */
    private static ResourceHandlerRestricted instance;

    /*
     *  (non-Javadoc)
     * @see es.eucm.eadventure.engine.resourcehandler.ResourceHandler#getInstance()
     */
    public static ResourceHandler getInstance( ) {
        return instance;
    }
    
    public static void create(){
        instance = new ResourceHandlerRestricted( );
    }
    
    public static void delete(){
    	SecurityManager sm = System.getSecurityManager();
        if (instance!=null && instance.tempFiles!=null){
            for (File file: instance.tempFiles){
            	try  {
	            	sm.checkDelete(file.getPath());
	                file.delete( );
            	} catch (Exception e) {}
            }
        }
        instance = null;
    }

    /**
     * Empty constructor
     */
    private ResourceHandlerRestricted( ) {
    }
    
    /*
     *  (non-Javadoc)
     * @see es.eucm.eadventure.engine.resourcehandler.ResourceHandler#setZipFile(java.lang.String)
     */
    public void setZipFile( String zipFilename ) {
        try {
            this.zipPath=zipFilename;
            /*if( !zipFilename.startsWith( "/" ) ) {
                zipFilename = "/" + zipFilename;
            }*/
            
            
            /*URL url =this.getClass( ).getResource( "/"+zipFilename );
            //System.out.println((url==null)?"URL IS NULL":"URL IS NOT NULL");
            
            File file = new File(zipFilename);
            //if (ResourceHandler.extraRestriction){
                InputStream xmlInputStream = this.getClass( ).getResourceAsStream( "/"+zipFilename );
           
                FileOutputStream xmlOutputStream = new FileOutputStream( file );
                
                int byt;                
                while ((byt = xmlInputStream.read()) != -1) {
                    xmlOutputStream.write(byt);
                }
                
                xmlOutputStream.close();
            //}*/
            
            zipFile = null; 
                //new ZipFile( file );
       /* } catch( ZipException e ) {
            e.printStackTrace( );
        } catch( IOException e ) {
            e.printStackTrace( );
        }*/
        } catch (Exception e){
            
        }
    }
    
    /*
     *  (non-Javadoc)
     * @see es.eucm.eadventure.engine.resourcehandler.ResourceHandler#getOutputStream(java.lang.String)
     */
    public OutputStream getOutputStream( String path ) {
        return null;
    }

    /*
     *  (non-Javadoc)
     * @see es.eucm.eadventure.engine.resourcehandler.ResourceHandler#getResourceAsStream(java.lang.String)
     */
    public InputStream getResourceAsStream( String path ) {
        if( !path.startsWith( "/" ) ) {
            path = "/" + path;
        }
        
        InputStream is = this.getClass( ).getResourceAsStream( path );
        
        return is;
    }


    public URL getResourceAsURL( String path ){
        if (!path.startsWith( "/" ))
            path = "/"+path;
        InputStream is = this.getClass().getResourceAsStream(path);
        byte[] data = new byte[1024];
        de.schlichtherle.io.File osFile = new de.schlichtherle.io.File(path.substring( path.lastIndexOf( "/" )+1 ));

        boolean copy = true;
        for (TempFile file:tempFiles){
            if(file.getOriginalAssetPath().equals( path )){
                copy = false; break;
            }
        }
        
        if (copy){
        	// Search the file name. If exists, change name
        	int i=0;
        	while (osFile.exists()){
        		i++;
        		osFile = new de.schlichtherle.io.File (i+"_"+path.substring( path.lastIndexOf( "/" )+1 ));
        	}
            TempFile tempFile = new TempFile (((de.schlichtherle.io.File) osFile).getAbsolutePath());
            tempFile.setOriginalAssetPath(path);
            tempFiles.add( tempFile );
        }
        

        FileOutputStream os;
        try {
            if (copy){
                os = new FileOutputStream(new File(osFile.getAbsolutePath( )));
                int length = 0;
                while ((length = is.read( data ))!=-1){
                    os.write( data, 0, length );
                }
                os.close( );
                is.close( );
            }
            return osFile.toURI( ).toURL( );
        } catch( FileNotFoundException e ) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        } catch( IOException e ) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }
    	
    }
    
    @Override
    public MediaLocator getResourceAsMediaLocator( String path ) {
    	MediaLocator mediaLocator = null;
    	URL url = getResourceAsURL(path);
    	if (url!=null)
    		mediaLocator = new MediaLocator ( url );
    	return mediaLocator;
                 
    }

    public URL getResourceAsURLFromZip( String path ){
        if (!path.startsWith( "/" )){
            path = "/"+path;
        }
        return this.getClass( ).getResource( path );
    }

	public InputStream buildInputStream(String filePath) {
		return getResourceAsStream ( filePath );
	}

	public String[] listNames(String filePath) {
		return new String[0];
	}
}