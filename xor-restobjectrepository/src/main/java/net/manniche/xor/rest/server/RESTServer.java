/*
 *  This file is part of xor.
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

package net.manniche.xor.rest.server;

import java.util.logging.Logger;
import net.manniche.xor.storage.FileStorage;
import net.manniche.xor.storage.StorageProvider;
import org.apache.cxf.jaxrs.JAXRSServerFactoryBean;
import org.apache.cxf.jaxrs.lifecycle.SingletonResourceProvider;


public class RESTServer{

    private final static Logger Log= Logger.getLogger( RESTServer.class.getName() );

    protected RESTServer()
    {
        String sep = System.getProperty( "file.separator" );
        String storagePath = System.getProperty( "user.home" ) + sep + "objectstorage" + sep;
        String metadataStoragePath = System.getProperty( "user.home" ) + sep + "objectstorage" + sep + "contenttypes" + sep;
        StorageProvider store = new FileStorage();

        JAXRSServerFactoryBean sf = new JAXRSServerFactoryBean();
        sf.setResourceClasses( RESTRepositoryRelayer.class );
        sf.setResourceProvider( RESTRepositoryRelayer.class,
                                new SingletonResourceProvider( new RESTRepositoryRelayer( store, storagePath, metadataStoragePath ) ) );
        sf.setAddress("http://localhost:9000/");

        sf.create();
    }

    public static void main(String args[]) throws Exception {
        new RESTServer();
        Log.info("Server ready...");

    }
}
