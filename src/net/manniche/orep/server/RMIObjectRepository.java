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
package net.manniche.orep.server;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.manniche.orep.types.ObjectIdentifier;
import net.manniche.orep.storage.StorageProvider;
import net.manniche.orep.storage.StorageType;
import net.manniche.orep.types.DefaultIdentifier;
import net.manniche.orep.types.DigitalObject;
import net.manniche.orep.types.DigitalObjectMeta;
import net.manniche.orep.types.ObjectRepositoryService;


/**
 *
 * @author stm
 */
public final class RMIObjectRepository extends ObjectRepository
{

    private final String host;
    private final String loc;
    private final int port;

    public RMIObjectRepository( StorageProvider storage, String host, String sLocation, int port )
    {
        super( storage );
        this.host    = host;
        this.loc     = sLocation;
        this.port    = port;
    }


    @Override
    public ObjectIdentifier storeObject( DigitalObject data, DigitalObjectMeta metadata, ObjectIdentifier identifier, String message ) throws RemoteException
    {
        ObjectIdentifier id = super.storeObject( data, metadata, identifier, message );
        return new DefaultIdentifier( id.getIdentifierAsURI() );
    }

    public static void main( String[] args )
    {
        String host = "127.0.0.1";
        int port = 8080;
        String location = "RMIObjectRepository";

        if( null == System.getSecurityManager() )
        {
            SecurityManager s = new SecurityManager();
            s.checkConnect( host, port );
            System.setSecurityManager( new SecurityManager() );
        }
        String name = "OREP-RMI";

        String storagetype = "FileStorage";
        StorageType type = StorageType.valueOf( storagetype );

        Class<ObjectRepositoryService> storage = ServiceLocator.getImplementation( type );

        System.out.println( String.format( "%s", storage ) );
        StorageProvider store;
        try
        {
//            StorageProvider provider = (StorageProvider) type.getClassofService().newInstance();
            store = (StorageProvider) storage.newInstance();
            ObjectManagement engine = new RMIObjectRepository( store, host, location, port );
            ObjectManagement stub = (ObjectManagement) UnicastRemoteObject.exportObject( engine, 0 );
            Registry registry = LocateRegistry.getRegistry();
            registry.rebind( name, stub );
            System.out.println( String.format( "OREP-RMI bound to %s", registry.REGISTRY_PORT ) );
        }
        catch( InstantiationException ex )
        {
            Logger.getLogger( RMIObjectRepository.class.getName() ).log( Level.SEVERE, ex.getMessage(), ex );
        }
        catch( IllegalAccessException ex )
        {
            Logger.getLogger( RMIObjectRepository.class.getName() ).log( Level.SEVERE, ex.getMessage(), ex );
        }
        catch( RemoteException ex )
        {
            Logger.getLogger( RMIObjectRepository.class.getName() ).log( Level.SEVERE, ex.getMessage(), ex );
        }
    }


}
