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

import java.io.IOException;
import java.net.URI;
import javax.naming.ConfigurationException;


/**
 * Base class for all database storage providers. This class defines the common
 * behaviour expected from the database storage backends.
 * 
 * @author stm
 */
public abstract class DBStorage implements StorageProvider{

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
    public abstract URI save( byte[] object, String storagePath ) throws IOException;

    @Override
    public abstract void save( byte[] object, URI url, String storagePath ) throws IOException;

    @Override
    public abstract byte[] get( URI identifier) throws IOException;

    @Override
    public abstract void close();

    @Override
    public abstract void delete( URI identifier) throws IOException;
}
