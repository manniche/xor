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

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.UnknownHostException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.manniche.orep.documents.DefaultDigitalObject;
import net.manniche.orep.types.ObjectIdentifier;
import net.manniche.orep.storage.StorageProvider;
import net.manniche.orep.storage.StorageType;
import net.manniche.orep.types.DefaultIdentifier;
import net.manniche.orep.types.DigitalObject;
import net.manniche.orep.types.ObjectRepositoryService;


/**
 * The connection handler for RMI requests. This class represents the RMI
 * server implementation.
 * 
 * @author stm
 */
public final class RMIObjectRepository extends UnicastRemoteObject implements RMIObjectManagement
{

    static final long serialVersionUID = -8975965744773865183L;
    private final StorageProvider repositoryStorageMechanism;
    private final LogMessageHandler logMessageHandler;

    /**
     * Sets up the RMI server for the object repository.
     *
     * @param storage the StorageProvider that handles storage of objects for
     * this RMI server instance
     * @param logMessageHandler handles the log message storing/notifications
     * @throws RemoteException if the server could not be started
     */
    public RMIObjectRepository( StorageProvider storage, LogMessageHandler logMessageHandler ) throws RemoteException
    {
        super();
        this.logMessageHandler = logMessageHandler;
        this.repositoryStorageMechanism = storage;
    }


    /**
     * Retrieves a {@link DigitalObject} identified by {@code identifier} from
     * the object repository. A RemoteException is thrown if no object matches
     * the identifier given.
     *
     * @param identifier identifying the object to be retrieved
     * @return a DigitalObject if the identifier matches
     * @throws RemoteException if anything goes wrong in the object retrieval
     * process
     */
    @Override
    public DigitalObject getRepositoryObject( ObjectIdentifier identifier ) throws RemoteException
    {
        DigitalObject digitalObject = null;

        try
        {
            digitalObject = this.getObject( identifier );
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
    public final ObjectIdentifier storeRepositoryObject( DigitalObject data, String logmessage ) throws RemoteException
    {
        return this.storeRepositoryObject( data, null, logmessage );
    }


    /**
     * Stores a {@link DigitalObject} {@code data} into the object repository,
     * returning the {@link ObjectIdentifier identifier} uniquely identifying
     * the object.
     *
     * @param data the object to be stored
     * @param identifier the object, when stored will be identified by this id
     * @param logmessage a message describing the operation, provided by the user
     * @return an {@link ObjectIdentifier} that identifies the object within the
     * object repository
     * @throws RemoteException if either the metadata could not be retrived or
     * some lower level operation in the storage implementation failed. Detailed
     * information is available in the wrapped exception.
     */
    private ObjectIdentifier storeRepositoryObject( DigitalObject data, ObjectIdentifier identifier, String logmessage ) throws RemoteException
    {
        ObjectIdentifier oIdentifier = null;
        try
        {
            oIdentifier = this.storeObject( data, identifier, logmessage );

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


    /**
     * Deletes a {@link DigitalObject} identified by {@code identifier} from
     * the object repository. If the object could be found _and_ deleted, this
     * method returns true, otherwise it returns false. E.g. a RemoteException
     * will be thrown if the object can be found, but not deleted.
     *
     * @param identifier identifying the object to be retrieved
     * @param logmessage user supplied message describing the context of the action
     * @return true iff the object can be found _and_ deleted, false otherwise.
     * @throws RemoteException if anything goes wrong in the object deletion.
     * process
     */
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


    /**
     * provides the implementation for the {@link #storeRepositoryObject(net.manniche.orep.types.DigitalObject, java.lang.String)} method
     *
     * @see #storeRepositoryObject(net.manniche.orep.types.DigitalObject, java.lang.String)
     * @throws IOException if anything goes wrong in the storage process
     */
    @Override
    public ObjectIdentifier storeObject( DigitalObject data, String message ) throws IOException
    {
        return this.storeObject( data, null, message );
    }


    /**
     * provides the implementation for the {@link #storeRepositoryObject(net.manniche.orep.types.DigitalObject, net.manniche.orep.types.ObjectIdentifier, java.lang.String)} method
     *
     * @see #storeRepositoryObject(net.manniche.orep.types.DigitalObject, net.manniche.orep.types.ObjectIdentifier, java.lang.String)
     * @throws IOException if anything goes wrong in the storage process
     */
    @Override
    public ObjectIdentifier storeObject( DigitalObject data, ObjectIdentifier identifier, String message ) throws IOException
    {
        URI uid;

        ObjectIdentifier objectID = null;

        if( null == identifier )
        {
            uid = this.repositoryStorageMechanism.save( data.getBytes() );
            objectID = new DefaultIdentifier( uid );
        }
        else
        {
            this.repositoryStorageMechanism.save( data.getBytes(), identifier.getIdentifierAsURI() );
            objectID = identifier;
        }

        return objectID;
    }


    /**
     * Provides the implementation for the {@link #getRepositoryObject(net.manniche.orep.types.ObjectIdentifier) } method
     *
     * @throws IOException if the object cannot be retrieved from the storage
     * implementation
     */
    @Override
    public DigitalObject getObject( ObjectIdentifier identifier ) throws IOException
    {
        byte[] object = this.repositoryStorageMechanism.get( identifier.getIdentifierAsURI() );
        return new DefaultDigitalObject( object );
    }


    /**
     *  Provides the implementation for the {@link #deleteRepositoryObject(net.manniche.orep.types.ObjectIdentifier, java.lang.String) } method
     * @throws IOException if the object could not be found and or subsequently
     * deleted.
     */
    @Override
    public boolean deleteObject( ObjectIdentifier identifier, String logmessage ) throws IOException
    {
        this.logMessageHandler.commitLogMessage( RMIObjectRepository.class.getName(), "deleteObject", logmessage );
        return this.repositoryStorageMechanism.delete( identifier.getIdentifierAsURI() );
    }

    ////////////////////////////////////////////////////////////////////////////
    // Below follows the RMI server main method.                              //
    ////////////////////////////////////////////////////////////////////////////

    public static void main( String[] args ) throws UnknownHostException
    {
        String host = "127.0.0.1";
        int port = 8181;
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

//        System.setProperty( "java.rmi.server.hostname", "192.168.1.74" );
//        Logger.getLogger( RMIObjectRepository.class.getName() ).log( Level.INFO, String.format( "%s", storage ) );
//        Logger.getLogger( RMIObjectRepository.class.getName() ).log( Level.INFO, String.format( "%s", System.getProperty( "java.rmi.server.hostname" ) ) );

        StorageProvider store;
        try
        {


            //            StorageProvider provider = (StorageProvider) type.getClassofService().newInstance();
            store = (StorageProvider) storage.newInstance();
            LogMessageHandler logMessageHandler = new FileBasedLogMessageHandler( store );
            RMIObjectManagement k = new RMIObjectRepository( store, logMessageHandler );
            Naming.rebind( "rmi://localhost/orep", k );
            //LocateRegistry.createRegistry( 1099 );


            System.out.println( String.format( "%s", UnicastRemoteObject.getLog() ) );

        }
        catch( MalformedURLException ex )
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
