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

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.UnknownHostException;
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
 *
 * @author stm
 */
public class RMIServer {

    final static Logger Log = Logger.getLogger( RMIServer.class.getName() );

    ////////////////////////////////////////////////////////////////////////////
    // Below follows the RMI server main method.                              //
    ////////////////////////////////////////////////////////////////////////////

    public static void main( String[] args ) throws UnknownHostException
    {
        //String host = "127.0.0.1";
        String host = "localhost";
        int port = 8181;
        String location = "RMIObjectRepository";
        String binder = String.format( "rmi://%s:%s/%s", host, port, location );

//        if( null == System.getSecurityManager() )
//        {
//            SecurityManager s = new SecurityManager();
//            s.checkConnect( host, port );
//            System.setSecurityManager( new SecurityManager() );
//        }
        String name = "OREP-RMI";

        String storagetype = "FileStorage";
        StorageType type = StorageType.valueOf( storagetype );

        Class<ObjectRepositoryService> storage = ServiceLocator.getImplementation( type );

//        System.setProperty( "java.rmi.server.hostname", "192.168.1.74" );
//        Logger.getLogger( RMIObjectRepository.class.getName() ).log( Level.INFO, String.format( "%s", storage ) );
//        Logger.getLogger( RMIObjectRepository.class.getName() ).log( Level.INFO, String.format( "%s", System.getProperty( "java.rmi.server.hostname" ) ) );

        StorageProvider store;
        try
        {
            Log.log( Level.INFO, "trying to bind server" );

            //            StorageProvider provider = (StorageProvider) type.getClassofService().newInstance();
            store = (StorageProvider) storage.newInstance();
            LogMessageHandler logMessageHandler = new FileBasedLogMessageHandler( store );
            RMIObjectManagement k = new RMIObjectRepository( store, logMessageHandler );
            Naming.rebind( "rmi://localhost/orep", k );
            Log.log( Level.INFO, String.format( "binding to %s", binder ) );
            //Naming.rebind( binder, k );
            //LocateRegistry.createRegistry( 1099 );


            System.out.println( String.format( "%s", UnicastRemoteObject.getLog() ) );

        }
        catch( MalformedURLException ex )
        {
            Log.log( Level.WARNING, ex.getMessage(), ex );
        }
        catch( InstantiationException ex )
        {
            Log.log( Level.WARNING, ex.getMessage(), ex );
        }
        catch( IllegalAccessException ex )
        {
            Log.log( Level.WARNING, ex.getMessage(), ex );
        }
        catch( RemoteException ex )
        {
            Log.log( Level.WARNING, ex.getMessage(), ex );
        }
    }
}
