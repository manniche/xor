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

package net.manniche.orep.storage;

import java.io.IOException;
import java.net.URI;
import java.util.List;
import javax.naming.ConfigurationException;


/**
 *
 * @author stm
 */
public class DBStorage implements StorageProvider{

    private final String driver;
    private final String url;
    private final String user;
    private final String passwd;

    public DBStorage( ) throws ConfigurationException
    {
        this.driver = System.getProperty( "driver_name" );
        this.url = System.getProperty( "url" );
        this.user = System.getProperty( "user" );
        this.passwd = System.getProperty( "passwd" );

        if( null == this.driver )
        {
            throw new ConfigurationException( "Value for driver could not be read from configuration / cl argument" );
        }
        else if( null == this.url )
        {
            throw new ConfigurationException( "Value for url could not be read from configuration / cl argument" );
        }
        else if( null == this.user )
        {
            throw new ConfigurationException( "Value for user could not be read from configuration / cl argument" );
        }
        else if( null == this.passwd )
        {
            throw new ConfigurationException( "Value for password could not be read from configuration / cl argument" );
        }
    }


    @Override
    public URI save( byte[] object ) throws IOException
    {
        throw new UnsupportedOperationException( "Not supported yet." );
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
    public void delete( URI identifier ) throws IOException
    {
        throw new UnsupportedOperationException( "Not supported yet." );
    }
}
