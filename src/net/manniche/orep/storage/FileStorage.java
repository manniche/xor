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
import java.io.IOException;
import java.net.URI;
import java.util.List;


/**
 *
 * @author stm
 */
public class FileStorage implements StorageProvider
{

    private final File storage_file;

    public FileStorage()
    {
        String storage_path = System.getProperty( "user.home" ) + System.getProperty( "file.separator" ) + "objectstorage";
        System.out.println( String.format( "%s", storage_path ) );
        this.storage_file = new File( storage_path );
    }


    @Override
    public URI save( byte[] object ) throws IOException
    {
        String hash = Integer.toString( object.hashCode() );

        if( hash.startsWith( "-" ) )
        {
            hash = hash.substring( 1 );
        }

        String path = new Long( System.currentTimeMillis() ).toString() + hash;

        URI id = null;
        try
        {
            id = new URI( "file", null, path, null );
        }
        catch( URISyntaxException ex )
        {
            String error = String.format( "Could not construct storage location: %s", ex.getMessage() );
            Logger.getLogger( FileStorage.class.getName() ).log( Level.SEVERE, error, ex );
            throw new IOException( error, ex );
        }

        return id;
    }


    @Override
    public void save( byte[] object, URI uri ) throws IOException
    {
        throw new UnsupportedOperationException( "Not supported yet." );
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
    public boolean delete( URI identifier )
    {
        throw new UnsupportedOperationException( "Not supported yet." );
    }
}
