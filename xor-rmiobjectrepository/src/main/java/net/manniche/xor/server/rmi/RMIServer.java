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

package net.manniche.xor.server.rmi;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.rmi.AccessException;
import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;
import net.manniche.xor.exceptions.StorageProviderException;
import net.manniche.xor.server.RepositoryObserver;
import net.manniche.xor.server.ServiceLocator;
import net.manniche.xor.services.search.SearchProvider;
import net.manniche.xor.services.search.SearchType;
import net.manniche.xor.storage.FileStorage;
import net.manniche.xor.storage.StorageProvider;


/**
 * Server main class for the RMI server. The main loop reads a properties file
 * from the config/ directory and starts a server which is registered with a
 * running RMI registry.
 *
 * @author Steen Manniche
 */
public class RMIServer {

    private final static Logger Log= Logger.getLogger( RMIServer.class.getName() );
    private static RMIRepositoryServer manager;
    private static Registry registry;

    ////////////////////////////////////////////////////////////////////////////
    // Below follows the RMI server main method.                              //
    ////////////////////////////////////////////////////////////////////////////

    public static void main( String[] args ) throws IOException
    {
        int port = 8181;
        String sep = System.getProperty( "file.separator" );
        String storagePath = System.getProperty( "user.home" ) + sep + "objectstorage" + sep;
        String metadataStoragePath = System.getProperty( "user.home" ) + sep + "objectstorage" + sep + "contenttypes" + sep;

        LogManager logManager = LogManager.getLogManager();
        logManager.readConfiguration();

        try
        {
            Log.log( Level.INFO, "trying to export server" );

            StorageProvider store = new FileStorage( storagePath );
            manager = new RMIRepositoryServer( store, metadataStoragePath );

            SearchProvider search = getSearchProvider();
            manager.addObserver( (RepositoryObserver) search);

            Remote remote = UnicastRemoteObject.exportObject( manager, port );
            registry = LocateRegistry.createRegistry( Registry.REGISTRY_PORT );
            registry.bind( RMIRepositoryServer.class.getName(), remote );

            Log.log( Level.INFO, String.format( "Started server on port %s", port ) );
            Runtime.getRuntime().addShutdownHook( new Thread(){
                @Override
                public void run()
                {
                    shutdown();
                }
            } );

        }
        catch( AlreadyBoundException ex )
        {
            Log.log( Level.SEVERE, ex.getMessage(), ex );
        }
        catch( AccessException ex )
        {
            Log.log( Level.SEVERE, ex.getMessage(), ex );
        }
        catch( RemoteException ex )
        {
            Log.log( Level.SEVERE, ex.getMessage(), ex );
        }

    }

    private static SearchProvider getSearchProvider()
    {
        SearchProvider search = null;
        SearchType service = SearchType.valueOf( "DublinCoreIndexService" );
        try
        {
            search = (SearchProvider) ServiceLocator.getService( service.getClassofService() );
        }
        catch( NoSuchMethodException ex )
        {
            Log.log( Level.SEVERE, ex.getMessage(), ex );
        }
        catch( IllegalArgumentException ex )
        {
            Log.log( Level.SEVERE, ex.getMessage(), ex );
        }
        catch( InstantiationException ex )
        {
            Log.log( Level.SEVERE, ex.getMessage(), ex );
        }
        catch( IllegalAccessException ex )
        {
            Log.log( Level.SEVERE, ex.getMessage(), ex );
        }
        catch(InvocationTargetException ex )
        {
            Log.log( Level.SEVERE, ex.getMessage(), ex );
        }
        return search;
    }
    public static void shutdown()
    {
        try
        {
            registry.unbind( RMIRepositoryServer.class.getName() );
        }
        catch( RemoteException ex )
        {
            Log.log( Level.SEVERE, ex.getMessage(), ex );
        }
        catch( NotBoundException ex )
        {
            Log.log( Level.SEVERE, ex.getMessage(), ex );
        }
    }
}
