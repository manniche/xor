/*
 *  This file is part of OREP
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
import java.net.URI;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
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
public final class RMIObjectRepository extends UnicastRemoteObject implements RMIObjectManagement
{

    static final long serialVersionUID = -8975965744773865183L;
    private final StorageProvider repositoryStorageMechanism;
    private final LogMessageHandler logMessageHandler;

    public RMIObjectRepository( StorageProvider storage, LogMessageHandler logMessageHandler ) throws RemoteException
    {
        this.logMessageHandler = logMessageHandler;
        this.repositoryStorageMechanism = storage;
    }

    @Override
    public DigitalObject getRepositoryObject( ObjectIdentifier identifier ) throws RemoteException
    {
        DigitalObject digitalObject = null;

        try
        {
            digitalObject = this.getObject( identifier );
        }
        catch( XMLStreamException ex )
        {
            String error = String.format( "Failed to retrieve object identified by %s: %s", identifier, ex.getMessage() );
            Logger.getLogger( RMIObjectRepository.class.getName() ).log( Level.WARNING, error, ex );
            //wrap and send to RMI client
            throw new RemoteException( error, ex );
        }
        catch( IOException ex )
        {
            String error = String.format( "Failed to retrieve object identified by %s: %s", identifier, ex.getMessage() );
            Logger.getLogger( RMIObjectRepository.class.getName() ).log( Level.WARNING, error, ex );
            //wrap and send to RMI client
            throw new RemoteException( error, ex );
        }

        return digitalObject;
    }

    /**
     * Stores a {@link DigitalObject} {@code data} into the object repository,
     * returning the {@link ObjectIdentifier identifier} uniquely identifying
     * the object.
     * 
     * @param data the object to be stored
     * @param metadata any metadata object stored with the object
     * @param logmessage a message describing the operation, provided by the user
     * @return an {@link ObjectIdentifier} that identifies the object within the
     * object repository
     * @throws RemoteException if either the metadata could not be retrived or
     * some lower level operation in the storage implementation failed. Detailed
     * information is available in the wrapped exception.
     */
    @Override
    public final ObjectIdentifier storeRepositoryObject( DigitalObject data , DigitalObjectMeta metadata, String logmessage ) throws RemoteException
    {
        return this.storeRepositoryObject( data, metadata, null, logmessage );
    }

    private ObjectIdentifier storeRepositoryObject( DigitalObject data, DigitalObjectMeta metadata, ObjectIdentifier identifier, String logmessage ) throws RemoteException
    {
        ObjectIdentifier oIdentifier = null;
        try
        {
             oIdentifier = this.storeObject(data, metadata, identifier, logmessage );

        }
        catch( XMLStreamException ex )
        {
            String error = String.format( "Could not construct metadata: %s", ex.getMessage() );
            Logger.getLogger( RMIObjectRepository.class.getName() ).log( Level.WARNING, error, ex );
            //wrap and send to RMI client
            throw new RemoteException( error, ex );
        }
        catch( IOException ex )
        {
            String error = String.format( "Failed to store object: %s", ex.getMessage() );
            Logger.getLogger( RMIObjectRepository.class.getName() ).log( Level.WARNING, error, ex );
            //wrap and send to RMI client
            throw new RemoteException( error, ex );
        }
        // we return the given, if it's non-null, the constructed otherwise
        if( null == identifier )
        {
            identifier = oIdentifier;
        }

        return identifier;
    }


    @Override
    public boolean deleteRepositoryObject( ObjectIdentifier identifier, String logmessage ) throws RemoteException
    {
        try
        {
            return this.deleteObject( identifier, logmessage );
        }
        catch( IOException ex )
        {
            String error = String.format( "Failed to delete object identified by '%s': %s", identifier, ex.getMessage() );
            Logger.getLogger( RMIObjectRepository.class.getName() ).log( Level.WARNING, error, ex );
            //wrap and send to RMI client
            throw new RemoteException( error, ex );
        }
    }


    @Override
    public ObjectIdentifier storeObject( DigitalObject data, DigitalObjectMeta metadata, String message ) throws XMLStreamException, IOException
    {
        return this.storeObject( data, metadata, null, message );
    }


    @Override
    public ObjectIdentifier storeObject( DigitalObject data, DigitalObjectMeta metadata, ObjectIdentifier identifier, String message ) throws XMLStreamException, IOException
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        metadata.serialize( baos );

        URI uid;

        ObjectIdentifier objectID = null;

        if( null == identifier )
        {
            uid = this.repositoryStorageMechanism.save( data.getBytes(), baos.toByteArray() );
            objectID = new DefaultIdentifier( uid );
        }
        else
        {
            this.repositoryStorageMechanism.save( data.getBytes(), baos.toByteArray(), identifier.getIdentifierAsURI() );
            objectID = identifier;
        }

        return objectID;
    }


    @Override
    public DigitalObject getObject( ObjectIdentifier identifier ) throws XMLStreamException, IOException
    {
        throw new UnsupportedOperationException( "Not supported yet." );
    }


    @Override
    public boolean deleteObject( ObjectIdentifier identifier, String logmessage ) throws IOException
    {
        this.logMessageHandler.commitLogMessage( RMIObjectRepository.class.getName(), "deleteObject", logmessage );
        return this.repositoryStorageMechanism.delete( identifier.getIdentifierAsURI() );
    }

    ////////////////////////////////////////////////////////////////////////////
    // Below follows the RMI server main method.                              //
    ////////////////////////////////////////////////////////////////////////////
    public static void main( String[] args )
    {
        String host = "127.0.0.1";
        int port = 8080;
        String location = "RMIObjectRepository";

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

        System.out.println( String.format( "%s", storage ) );
        StorageProvider store;
        try
        {
//            StorageProvider provider = (StorageProvider) type.getClassofService().newInstance();
            store = (StorageProvider) storage.newInstance();
            LogMessageHandler logMessageHandler = new FileBasedLogMessageHandler( store );
            RMIObjectManagement engine = new RMIObjectRepository( store, logMessageHandler );
            Context context = new InitialContext();
            context.bind( "rmi:RMIObjectRepository", engine );
            System.out.println( String.format( "OREP-RMI bound to %s", context.PROVIDER_URL ) );
        }
        catch( NamingException ex )
        {
            Logger.getLogger( RMIObjectRepository.class.getName() ).log( Level.WARNING, ex.getMessage(), ex );
        }
        catch( InstantiationException ex )
        {
            Logger.getLogger( RMIObjectRepository.class.getName() ).log( Level.WARNING, ex.getMessage(), ex );
        }
        catch( IllegalAccessException ex )
        {
            Logger.getLogger( RMIObjectRepository.class.getName() ).log( Level.WARNING, ex.getMessage(), ex );
        }
        catch( RemoteException ex )
        {
            Logger.getLogger( RMIObjectRepository.class.getName() ).log( Level.WARNING, ex.getMessage(), ex );
        }
    }
}
