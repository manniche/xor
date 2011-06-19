/*
 *  This file is part of xor
 *  Copyright © 2009, Steen Manniche.
 * 
 *  xor is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 * 
 *  xor is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 * 
 *  You should have received a copy of the GNU General Public License
 *  along with xor.  If not, see <http://www.gnu.org/licenses/>.
 */
package net.manniche.xor.storage;

import java.io.ByteArrayOutputStream;
import java.net.URISyntaxException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.util.HashSet;
import java.util.Set;
import net.manniche.xor.utils.RepositoryUtilities;


/**
 *
 * @author Steen Manniche
 */
public class FileStorage implements StorageProvider
{

    private final static String SCHEME = "file";

    private static final Logger Log = Logger.getLogger( FileStorage.class.getName() );

    private final Set<String> storagePaths;

    public FileStorage()
    {
        this.storagePaths = new HashSet<String>( 2 );
    }


    //TODO: this should be wrapped in a private static class instead. Or
    // something that prohibits the rest of the members in observing this.storagePath
    private synchronized String setCacheOrCheckStoragePath( String storagePath )
    {
        if( !this.storagePaths.contains( storagePath ) )
        {
            // ensure that the folder exists:
            File storage_dir = new File( storagePath );

            if( !storage_dir.exists() )
            {
                Log.info( String.format( "%s does not exist, creating it", storagePath ) );
                boolean could_create_dirs = storage_dir.mkdirs();
                if( !could_create_dirs )
                {
                    String error = String.format( "Could not create dir: %s, Please check permissions and disk space", storagePath );
                    Log.severe( error );
                    throw new IllegalStateException( error );
                }
            }
            this.storagePaths.add( storagePath );
            Log.info( String.format( "Storing files at %s", storagePath ) );
        }
        return storagePath;
    }


    @Override
    public URI save( byte[] object, String storagePath ) throws IOException
    {
        String storagePath_cached = this.setCacheOrCheckStoragePath( storagePath );
        return this.saveObject( object, null, storagePath_cached );
    }


    @Override
    public void save(  byte[] object, URI uri, String storagePath) throws IOException
    {
        String storagePath_cached = this.setCacheOrCheckStoragePath( storagePath );
        URI returnedURL = this.saveObject( object, uri, storagePath_cached );
        assert returnedURL.equals( uri );
    }

    @Override
    public String getScheme()
    {
        return SCHEME;
    }


    private URI saveObject( byte[] object, URI url, final String storagePath ) throws IOException
    {
        final String hash = Integer.toString( object.hashCode() );


        URI id = null;

        if( null == url )
        {
            try
            {
                id = RepositoryUtilities.generateURI( "file", storagePath, hash );
                Log.info( String.format( "URI for object was null, generated %s", id ) );
            }
            catch( URISyntaxException ex )
            {
                String error = String.format( "Could not construct storage location from %s: %s", url, ex.getMessage() );
                Log.log( Level.SEVERE, error, ex );
                throw new IOException( error, ex );
            }
        }
        else
        {
            id = url;
            Log.info( String.format( "Using path '%s' for object", id.getPath() ) );
        }


        File objectFile = new File( id );

        final FileOutputStream fos = new FileOutputStream( objectFile, false );
        Log.info( String.format( "Storing object at %s", id.getPath() ) );
        fos.write( object );
        fos.flush();
        fos.close();

        if( ! objectFile.exists() )
        {
            /**
             * todo: This is really a bad attempt at being humourous. I'm not
             * entirely sure that end users or clients would appreciate this
             * message from their low-level storage implementation.
             */
            String warn = String.format( "The file %s was not written to disk, and I have no further information on it's whereabouts", objectFile.getAbsolutePath() );
            Log.warning( warn );
            id = url;
        }

        return id;
    }


    @Override
    public byte[] get( URI uri ) throws IOException
    {
        Log.info( String.format( "Getting object identified by %s", uri.getPath() ) );
        File objectFile = null;
        objectFile = new File( uri );

        if( ! objectFile.isFile() )
        {
            String error = String.format( "Error - '%s' is not a file", uri );
            Log.log( Level.SEVERE, error );
            throw new FileNotFoundException( error );
        }

        FileInputStream data = new FileInputStream( objectFile );
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int b = data.read();
        while( -1 != b )
        {
            baos.write( b );
            b = data.read();
        }
        Log.info( String.format( "Returning byte array (as output stream) with size %s", baos.size() ) );
        return baos.toByteArray();
    }


    @Override
    public void close()
    {
        throw new UnsupportedOperationException( "Not supported yet." );
    }


    @Override
    public void delete( URI identifier) throws IOException
    {
        File deleteFile = null;
        deleteFile = new File( identifier );

        boolean deleted = deleteFile.delete();
        if( ! deleted )
        {
            String error = String.format( "The file %s could not be deleted", identifier );
            Log.log( Level.SEVERE, error );
        }
    }
}
