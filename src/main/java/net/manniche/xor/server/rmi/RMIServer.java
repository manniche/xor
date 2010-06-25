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

package net.manniche.xor.server.rmi;

import java.rmi.AccessException;
import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.manniche.xor.logger.EventLoggerType;
import net.manniche.xor.logger.LogMessageHandler;
import net.manniche.xor.server.ServiceLocator;
import net.manniche.xor.storage.StorageProvider;
import net.manniche.xor.storage.StorageType;
import net.manniche.xor.types.ObjectRepositoryService;
import net.manniche.xor.types.ObjectRepositoryServiceType;


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


    private static Class<ObjectRepositoryService> getServiceImplementation( ObjectRepositoryServiceType<? extends ObjectRepositoryService> serviceType )
    {
        return ServiceLocator.getImplementation( serviceType );
    }

    ////////////////////////////////////////////////////////////////////////////
    // Below follows the RMI server main method.                              //
    ////////////////////////////////////////////////////////////////////////////

    public static void main( String[] args )// throws UnknownHostException, AlreadyBoundException
    {
        int port = 8181;

        StorageType storageType = StorageType.valueOf( "FileStorage" );
        Class<ObjectRepositoryService> storage = getServiceImplementation( storageType );

        EventLoggerType loggerType = EventLoggerType.valueOf( "FileEventLogger" );
        Class<ObjectRepositoryService> logHandler = getServiceImplementation( loggerType );

        Registry registry = null;
        try
        {
            Log.log( Level.INFO, "trying to export server" );

            StorageProvider store = (StorageProvider) storage.newInstance();
            LogMessageHandler logMessageHandler = (LogMessageHandler) logHandler.newInstance();
            manager = new RMIRepositoryServer( store, logMessageHandler );
            Remote remote = UnicastRemoteObject.exportObject( manager, port );
            registry = LocateRegistry.createRegistry( Registry.REGISTRY_PORT );
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
        finally
        {
            try
            {
                registry.unbind( RMIRepositoryServer.class.getName() );
            }
            catch( RemoteException ex )
            {
                Logger.getLogger( RMIServer.class.getName() ).log( Level.SEVERE, ex.getMessage(), ex );
            }
            catch( NotBoundException ex )
            {
                Logger.getLogger( RMIServer.class.getName() ).log( Level.SEVERE, ex.getMessage(), ex );
            }
            System.exit( -1 );
        }
    }

}
