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
import net.manniche.xor.utils.RepositoryUtilities;


/**
 *
 * @author Steen Manniche
 */
public class FileStorage implements StorageProvider
{

    private static final Logger Log = Logger.getLogger( FileStorage.class.getName() );

    private final String storagePath;

    public FileStorage( String storagePath )
    {
        this.storagePath = storagePath;
    }


    @Override
    public URI save( byte[] object ) throws IOException
    {
        return this.saveObject( object, null );
    }


    @Override
    public void save(  byte[] object, URI uri ) throws IOException
    {
        URI returnedURL = this.saveObject( object, uri );
        assert returnedURL.equals( uri );
    }

    private URI saveObject( byte[] object, URI uri ) throws IOException
    {
        final String hash = Integer.toString( object.hashCode() );


        URI id = null;

        if( null == uri )
        {
            try
            {
                id = RepositoryUtilities.generateURI( "file", this.storagePath, hash );
                Log.info( String.format( "URI for object was null, generated %s", id ) );
            }
            catch( URISyntaxException ex )
            {
                String error = String.format( "Could not construct storage location from %s: %s", uri, ex.getMessage() );
                Log.log( Level.SEVERE, error, ex );
                throw new IOException( error, ex );
            }
        }
        else
        {
            id = uri;
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
            id = uri;
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
        data.close();
        Log.info( String.format( "Returning byte array with size %s", baos.size() ) );
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
