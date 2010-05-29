/*
 *  This file is part of OREP
 *  Copyright © 2009, Steen Manniche.
 * 
 *  OREP is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 * 
 *  OREP is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 * 
 *  You should have received a copy of the GNU General Public License
 *  along with OREP.  If not, see <http://www.gnu.org/licenses/>.
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


/**
 *
 * @author stm
 */
public class FileStorage implements StorageProvider
{

    private static final Logger log = Logger.getLogger( FileStorage.class.getName() );
    private final String storage_path;

    public FileStorage()
    {
        String sep = System.getProperty( "file.separator" );
        this.storage_path = System.getProperty( "user.home" ) + sep + "objectstorage" + sep;

        // ensure that the folder exists:
        File storage_dir = new File( this.storage_path );

        if( ! storage_dir.exists() )
        {
            log.info( String.format( "%s does not exist, creating it", this.storage_path ) );
            boolean could_create_dirs = storage_dir.mkdirs();
            if( ! could_create_dirs )
            {
                String error = String.format( "Could not create dir: %s, Please check permissions and disk space", this.storage_path );
                log.severe( error );
                throw new IllegalStateException( error );
            }
        }
        log.info( String.format( "Storing files at %s", this.storage_path ) );
    }


    @Override
    public URI save( byte[] object ) throws IOException
    {
        return this.saveObject( object, null );
    }


    @Override
    public void save( byte[] object, URI uri ) throws IOException
    {
        URI returnedURI = this.saveObject( object, uri );
        assert returnedURI.equals( uri );
    }

    private URI saveObject( byte[] object, URI uri ) throws IOException
    {
        String hash = Integer.toString( object.hashCode() );


        URI id = null;

        if( null == uri )
        {
            id = this.generateURI( this.storage_path, hash );
        }
        else
        {
            id = uri;
        }
        File f = new File( id );

        while( f.exists() )
            // this should never happen, but if it does, we should only need to
            // do this one pass.
        {
            id = this.generateURI( this.storage_path, hash );
            f = new File( id );
        }

        final FileOutputStream fos = new FileOutputStream( f, false );
        fos.write( object );
        fos.flush();
        fos.close();

        if( ! f.exists() )
        {
            /**
             * todo: This is really a bad attempt at being humourous. I'm not
             * entirely sure that end users or clients would appreciate this
             * message from their low-level storage implementation.
             */
            String warn = String.format( "The file %s was not written to disk, and I have no further information on it's whereabouts", f.getAbsolutePath() );
            log.warning( warn );
            id = this.generateURI( "/dev/null", hash );
        }

        return id;
    }

    private URI generateURI( String storage_path, String hash ) throws IOException
    {
        URI id = null;
        if( hash.startsWith( "-" ) )
        {
            hash = hash.substring( 1 );
        }

        String fname = new Long( System.currentTimeMillis() ).toString() + hash;
        String abs_path = storage_path + fname;

        try
        {
            id = new URI( "file", null, abs_path, null );
        }
        catch( URISyntaxException ex )
        {
            String error = String.format( "Could not construct storage location: %s", ex.getMessage() );
            log.log( Level.SEVERE, error, ex );
            throw new IOException( error, ex );
        }
        return id;
    }

    @Override
    public byte[] get( URI identifier ) throws IOException
    {
        File objectFile = new File( identifier );
        if( ! objectFile.isFile() )
        {
            String error = String.format( "Error - '%s' is not a file", identifier );
            log.log( Level.SEVERE, error );
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
        return baos.toByteArray();
    }


    @Override
    public void close()
    {
        throw new UnsupportedOperationException( "Not supported yet." );
    }


    @Override
    public void delete( URI identifier ) throws IOException
    {
        File deleteFile = new File( identifier );
        boolean deleted = deleteFile.delete();
        if( ! deleted )
        {
            String error = String.format( "The file %s could not be deleted", identifier );
            log.log( Level.SEVERE, error );
        }
    }
}
