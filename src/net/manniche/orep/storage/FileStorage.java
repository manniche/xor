/*
 *  This file is part of RMIObjectRepository.
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

import java.io.File;
import java.io.IOException;
import net.manniche.orep.types.ObjectIdentifier;


/**
 *
 * @author stm
 */
public class FileStorage implements StorageProvider{

    private final File storage_file;

    public FileStorage( )
    {
        String storage_path = System.getProperty( "user.home" ) + System.getProperty( "file.separator" ) + "objectstorage";
        System.out.println( String.format( "%s", storage_path ) );
        this.storage_file = new File( storage_path );
    }

    
    @Override
    public void save( byte[] object ) throws IOException
    {
        throw new UnsupportedOperationException( "Not supported yet." );
    }


    @Override
    public QueryResult query( Query query ) throws IOException
    {
        throw new UnsupportedOperationException( "Not supported yet." );
    }


    @Override
    public byte[] get( ObjectIdentifier identifier ) throws IOException
    {
        throw new UnsupportedOperationException( "Not supported yet." );
    }
}
