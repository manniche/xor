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

import java.io.InputStream;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import net.manniche.orep.types.ObjectIdentifier;
import net.manniche.orep.storage.StorageProvider;
import net.manniche.orep.storage.StorageType;
import net.manniche.orep.types.DigitalObject;
import net.manniche.orep.types.DigitalObjectMeta;
import net.manniche.orep.types.ObjectRepositoryService;


/**
 *
 * @author stm
 */
public class RMIObjectRepository implements ObjectManagement
{

    private StorageProvider storage;

    public RMIObjectRepository( StorageProvider storage )
    {
            this.storage = storage;
    }


    @Override
    public ObjectIdentifier storeObject( DigitalObject data, DigitalObjectMeta metadata, String message ) throws RemoteException
    {
        throw new UnsupportedOperationException( "Not supported yet." );
    }


    @Override
    public DigitalObject getObject( ObjectIdentifier identifier ) throws RemoteException
    {
        throw new UnsupportedOperationException( "Not supported yet." );
    }


    @Override
    public DigitalObjectMeta getObjectMetadata( ObjectIdentifier identifier ) throws RemoteException
    {
        throw new UnsupportedOperationException( "Not supported yet." );
    }


    @Override
    public boolean deleteObject( ObjectIdentifier identifier ) throws RemoteException
    {
        throw new UnsupportedOperationException( "Not supported yet." );
    }


    @Override
    public boolean addDataToObject( ObjectIdentifier identifier, InputStream data, String message ) throws RemoteException
    {
        throw new UnsupportedOperationException( "Not supported yet." );
    }


    @Override
    public InputStream getDataFromObject( ObjectIdentifier objectIdentifier, ObjectIdentifier dataIdentifier ) throws RemoteException
    {
        throw new UnsupportedOperationException( "Not supported yet." );
    }


    @Override
    public boolean deleteDataFromObject( ObjectIdentifier objectIdentifier, ObjectIdentifier dataIdentifier ) throws RemoteException
    {
        throw new UnsupportedOperationException( "Not supported yet." );
    }


    public static void main( String[] args )
    {
        if( null == System.getSecurityManager() )
        {
            System.setSecurityManager( null );//new SecurityManager() );
        }
        try
        {
            String name = "OREP-RMI";
           
            @SuppressWarnings( "unchecked" )
            Class<ObjectRepositoryService> storage = ServiceLocator.getImplementation( StorageType.FileStorage );
            System.out.println( String.format( "%s", storage ) );
            StorageProvider store = (StorageProvider) storage.newInstance();
            ObjectManagement engine = new RMIObjectRepository( store );
            ObjectManagement stub = (ObjectManagement) UnicastRemoteObject.exportObject( engine, 0 );
            Registry registry = LocateRegistry.getRegistry();
            registry.rebind( name, stub );

            System.out.println( String.format( "OREP-RMI bound to %s", registry.REGISTRY_PORT ) );
        }
        catch( Exception e )
        {
            System.err.println( "ComputeEngine exception:" );
            e.printStackTrace();
        }

    }


}
