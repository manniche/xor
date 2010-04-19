/*
 *  This file is part of OREP.
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

package net.manniche.orep.server.rmi;

import java.rmi.AccessException;
import java.rmi.AlreadyBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.manniche.orep.server.FileBasedLogMessageHandler;
import net.manniche.orep.server.LogMessageHandler;
import net.manniche.orep.server.ServiceLocator;
import net.manniche.orep.storage.StorageProvider;
import net.manniche.orep.storage.StorageType;
import net.manniche.orep.types.ObjectRepositoryService;


/**
 * Server main class for the RMI server. The main loop reads a properties file
 * from the config/ directory and starts a server which is registered with a
 * running RMI registry.
 *
 * @author stm
 */
public class RMIServer {

    private final static Logger Log = Logger.getLogger( RMIServer.class.getName() );
    private static RMIObjectManagement manager = null;

    ////////////////////////////////////////////////////////////////////////////
    // Below follows the RMI server main method.                              //
    ////////////////////////////////////////////////////////////////////////////

    public static void main( String[] args )// throws UnknownHostException, AlreadyBoundException
    {
        int port = 8181;

        String storagetype = "FileStorage";
        StorageType type = StorageType.valueOf( storagetype );

        Class<ObjectRepositoryService> storage = ServiceLocator.getImplementation( type );

        StorageProvider store;
        try
        {
            Log.log( Level.INFO, "trying to export server" );

            store = (StorageProvider) storage.newInstance();
            LogMessageHandler logMessageHandler = new FileBasedLogMessageHandler( store );
            manager = new RMIRepositoryServer( store, logMessageHandler );
            Remote remote = UnicastRemoteObject.exportObject( manager, port );
            Registry registry = LocateRegistry.createRegistry( Registry.REGISTRY_PORT );
            registry.bind( RMIRepositoryServer.class.getName(), remote );

            Log.log( Level.INFO, String.format( "Started server on port %s", port ) );
        }
        catch( InstantiationException ex )
        {
            Log.log( Level.SEVERE, ex.getMessage(), ex );
        }
        catch( AlreadyBoundException ex )
        {
            Log.log( Level.SEVERE, ex.getMessage(), ex );
        }
        catch( AccessException ex )
        {
            Log.log( Level.SEVERE, ex.getMessage(), ex );
        }
        catch( IllegalAccessException ex )
        {
            Log.log( Level.SEVERE, ex.getMessage(), ex );
        }
        catch( RemoteException ex )
        {
            Log.log( Level.SEVERE, ex.getMessage(), ex );
        }
    }
}
