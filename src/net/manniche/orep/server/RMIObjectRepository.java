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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.stream.XMLStreamException;
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
public class RMIObjectRepository implements ObjectManagement
{

    private final StorageProvider storage;
    private final String host;
    private final String loc;

    public RMIObjectRepository( StorageProvider storage, String host, String sLocation )
    {
        this.storage = storage;
        this.host    = host;
        this.loc     = sLocation;
    }


    @Override
    public ObjectIdentifier storeObject( DigitalObject data, DigitalObjectMeta metadata, String message ) throws RemoteException
    {
        try
        {
            ObjectIdentifier objectID = null;
            URI id = null;
            if( null == metadata.getIdentifier() )
            {
                String fragment = new Long( System.currentTimeMillis() ).toString() + new Integer( data.hashCode() );
                id = new URI( "uri", this.host, this.loc, fragment );
                objectID = new DefaultIdentifier( id );
            }
            else
            {
                id = metadata.getIdentifier().getIdentifierAsURI();
                objectID = metadata.getIdentifier();
            }
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            metadata.serialize( baos, id.toString() );
            storage.save( data.getBytes(), baos.toByteArray() );
            return objectID;
        }
        catch( URISyntaxException ex )
        {
            String error = String.format( "Could not construct storage location: %s", ex.getMessage() );
            Logger.getLogger( RMIObjectRepository.class.getName() ).log( Level.SEVERE, error, ex );
            //wrap and send to RMI client
            throw new RemoteException( error, ex );
        }
        catch( XMLStreamException ex )
        {
            String error = String.format( "Could not construct metadata: %s", ex.getMessage() );
            Logger.getLogger( RMIObjectRepository.class.getName() ).log( Level.SEVERE, error, ex );
            //wrap and send to RMI client
            throw new RemoteException( error, ex );
        }
        catch( IOException ex )
        {
            String error = String.format( "Failed to store object: %s", ex.getMessage() );
            Logger.getLogger( RMIObjectRepository.class.getName() ).log( Level.SEVERE, error, ex );
            //wrap and send to RMI client
            throw new RemoteException( error, ex );
        }
        finally
        {
            /**
             * If there should be any cleanup todo, we'll let the storage
             * implementation do it.
             */
            this.storage.close();
        }
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

        Class<ObjectRepositoryService> storage = ServiceLocator.getImplementation( StorageType.FileStorage );

        System.out.println( String.format( "%s", storage ) );
        StorageProvider store;
        try
        {
            store = (StorageProvider) storage.newInstance();
            ObjectManagement engine = new RMIObjectRepository( store, host, location );
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
