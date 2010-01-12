/*
 *  This file is part of OREP
 *  Copyright © 2009, Steen Manniche.
 * 
 *  RMIObjectRepository is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 * 
 *  RMIObjectRepositoryis distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 * 
 *  You should have received a copy of the GNU General Public License
 *  along with RMIObjectRepository.  If not, see <http://www.gnu.org/licenses/>.
 */
package net.manniche.orep.storage;

import java.net.URISyntaxException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.util.List;


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
        String hash = Integer.toString( object.hashCode() );

        if( hash.startsWith( "-" ) )
        {
            hash = hash.substring( 1 );
        }

        String fname = new Long( System.currentTimeMillis() ).toString() + hash;
        String store = this.storage_path + fname;

        URI id = this.generateURI( store );

        File f = new File( id );

        while( f.exists() )
            // this should never happen, but if it does, we should only need to
            // this once...
        {
            fname = new Long( System.currentTimeMillis() ).toString() + hash;
            store = this.storage_path + fname;
            id = this.generateURI( store );
            f = new File( id );
        }

        log.info( String.format( "Abs filename: %s", store ) );

        final FileOutputStream fos = new FileOutputStream( f, false );
        fos.write( object );
        fos.flush();
        fos.close();

        if( ! f.exists() )
        {
            /**
             * todo: This is really just sarcastic. I'm not entirely sure that
             * end users or clients would appreciate this message from their
             * low-level storage implementation.
             */
            String warn = String.format( "The file %s was not written to disk, and I have no further information on it's wereabouts", f.getAbsolutePath() );
            log.warning( warn );
            id = this.generateURI( "/dev/null" );
        }

        return id;
    }


    @Override
    public void save( byte[] object, URI uri ) throws IOException
    {
        throw new UnsupportedOperationException( "Not supported yet." );
    }


    private URI generateURI( String abs_path ) throws IOException
    {
        URI id = null;
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
    public List<URI> query( String query ) throws IOException
    {
        throw new UnsupportedOperationException( "Not supported yet." );
    }


    @Override
    public byte[] get( URI identifier ) throws IOException
    {
        throw new UnsupportedOperationException( "Not supported yet." );
    }


    @Override
    public void close()
    {
        throw new UnsupportedOperationException( "Not supported yet." );
    }


    @Override
    public void delete( URI identifier ) throws IOException
    {
        throw new UnsupportedOperationException( "Not supported yet." );
    }


}
