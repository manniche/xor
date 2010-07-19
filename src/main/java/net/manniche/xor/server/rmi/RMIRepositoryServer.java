/*
 *  This file is part of xor
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
import java.net.URI;
import java.net.URISyntaxException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.manniche.xor.types.ObjectIdentifier;
import net.manniche.xor.storage.StorageProvider;
import net.manniche.xor.types.DigitalObject;
import net.manniche.xor.server.RepositoryServer;
import net.manniche.xor.exceptions.RepositoryServiceException;
import net.manniche.xor.server.RepositoryObserver;
import net.manniche.xor.types.BasicContentType;
import net.manniche.xor.types.DefaultIdentifier;
import net.manniche.xor.types.ObjectRepositoryContentType;
import net.manniche.xor.types.RepositoryAction;
import net.manniche.xor.utils.RepositoryUtilities;

/**
 * The connection handler for RMI requests. This class represents the RMI
 * server implementation.
 *
 * {@link RepositoryObserver}s can register with this class through the
 * {@link #registerObservers() } method and recieve notifications for each of the
 * actions defined in the {@link RepositoryAction} enum. The nitifications
 * have an accompanying content type defined by the
 * {@link RMIRepositoryServerContentType} enum.
 * 
 * @author Steen Manniche
 */
public final class RMIRepositoryServer extends RepositoryServer implements RMIObjectManagement
{
    static final long serialVersionUID = -8975965744773865183L;

    private final static Logger Log = Logger.getLogger( RMIRepositoryServer.class.getName() );

    private final List< RepositoryObserver > observers;
    private final List<ObjectRepositoryContentType> registeredContentTypes;
    private final String storagePath;
    private final String metadataStoragePath;

    /**
     * Sets up the RMI server for the object repository.
     *
     * @param storage the StorageProvider that handles storage of objects for
     * this RMI server instance
     * @param storagePath path to which data will be stored for this server instance
     * @param metadataStoragePath path to which metadata of data will be stored for this server instance
     * @param logMessageHandler handles the log message storing/notifications
     * @throws RemoteException if the server could not be started
     */
    public RMIRepositoryServer( StorageProvider storage, String storagePath, String metadataStoragePath ) throws RemoteException
    {
        super( storage );
        this.storagePath = storagePath;
        this.metadataStoragePath = metadataStoragePath;
        this.observers = new ArrayList<RepositoryObserver>();
        this.registeredContentTypes = new ArrayList<ObjectRepositoryContentType>();
        this.registerContentTypes( BasicContentType.values() );
        Log.info( "Constructed repository server over RMI" );
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

        Log.info( String.format( "Getting object from %s", identifier.getURI() ) );
        try
        {
            digitalObject = super.getObject( identifier );
        }
        catch( IOException ex )
        {
            String error = String.format( "Failed to retrieve object identified by %s: %s", identifier, ex.getMessage() );
            Logger.getLogger( RMIRepositoryServer.class.getName() ).log( Level.WARNING, error, ex );
            //wrap and send to RMI client
            throw new RemoteException( error, ex );
        }

        ObjectRepositoryContentType contentTypeForObject;
        try
        {
            contentTypeForObject = this.getContentTypeForObject( identifier );
            this.notifyObservers( identifier, null, RepositoryAction.REQUEST, contentTypeForObject );
        }
        catch( IOException ex )
        {
            String error = String.format( "Failed to retrieve content type for object identified by %s: %s", identifier, ex.getMessage() );
            Logger.getLogger( RMIRepositoryServer.class.getName() ).log( Level.WARNING, error, ex );
            //wrap and send to RMI client
            throw new RemoteException( error, ex );
        }
        catch( RepositoryServiceException ex )
        {
            String error = String.format( "Failed to retrieve content type for object identified by %s: %s", identifier, ex.getMessage() );
            Logger.getLogger( RMIRepositoryServer.class.getName() ).log( Level.WARNING, error, ex );
            //wrap and send to RMI client
            throw new RemoteException( error, ex );
        }
        catch( URISyntaxException ex )
        {
            String error = String.format( "Failed to retrieve content type for object identified by %s: %s", identifier, ex.getMessage() );
            Logger.getLogger( RMIRepositoryServer.class.getName() ).log( Level.WARNING, error, ex );
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
     * @param contentType the content type of the data, as defined by {@link RMIRepositoryServerContentType}
     * @param logmessage a message describing the operation, provided by the user
     * @return an {@link ObjectIdentifier} that identifies the object within the
     * object repository
     * @throws RemoteException if either the metadata could not be retrived or
     * some lower level operation in the storage implementation failed. Detailed
     * information is available in the wrapped exception.
     */
    @Override
    public final ObjectIdentifier storeRepositoryObject( DigitalObject data, ObjectRepositoryContentType contentType, String logmessage ) throws RemoteException
    {
        Log.info( "Storing object without predefined identifier" );
        ObjectIdentifier identifier = null;
        try
        {
            identifier = this.storeRepositoryObject( data, contentType, null, logmessage );
        }
        catch( IOException ex )
        {
            String error = String.format( "Failed to store object: %s", ex.getMessage() );
            Logger.getLogger( RMIRepositoryServer.class.getName() ).log( Level.WARNING, error, ex );
            //wrap and send to RMI client
            throw new RemoteException( error, ex );
        }
        catch( RepositoryServiceException ex )
        {
            String error = String.format( "Failed to store object: %s", ex.getMessage() );
            Logger.getLogger( RMIRepositoryServer.class.getName() ).log( Level.SEVERE, error, ex );
            throw new RemoteException(error, ex );
        }
        
        return identifier;
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
    private ObjectIdentifier storeRepositoryObject( DigitalObject data,
                                                    ObjectRepositoryContentType contentType,
                                                    ObjectIdentifier identifier,
                                                    String logmessage ) throws RemoteException, IOException, RepositoryServiceException
    {

        Log.info( "Storing object" );
        ObjectIdentifier oIdentifier = null;
        oIdentifier = super.storeObject( data.getBytes(), this.storagePath, identifier , logmessage);

        Log.info( String.format( "Stored object with uri %s", oIdentifier.getURI() ) );

        // we return the given, if it's non-null, the constructed otherwise
        if( null == identifier )
        {
            Log.info( String.format( "given id=%s, returned id=%s", identifier, oIdentifier ) );
            identifier = oIdentifier;
        }

        Log.info( String.format( "URL path: %s, URL toString: %s", identifier.getURI().getPath(), identifier.getURI().toString() ) );


        try
        {
            Log.info( String.format( "Storing content type at uri %s", identifier.getURI() ) );

            String objectName = identifier.getName();
            URI contentURI = RepositoryUtilities.generateURI( "file", this.metadataStoragePath, objectName );
            ObjectIdentifier contentIdentifier = new DefaultIdentifier( contentURI );
            super.storeObject( contentType.toString().getBytes(), this.metadataStoragePath, contentIdentifier, "Storing content type" );
            this.notifyObservers( identifier, data, RepositoryAction.ADD, contentType );
        }
        catch( IOException ex )
        {
            String error = String.format( "Failed to store object content type: %s", ex.getMessage() );
            Log.log( Level.WARNING, error, ex);
            //wrap and send to RMI client
            throw new RemoteException( error, ex );
        }
        catch( URISyntaxException ex )
        {
            String error = String.format( "Failed to create uri for object content type: %s", ex.getMessage() );
            Log.log( Level.WARNING, error, ex);
            //wrap and send to RMI client
            throw new RemoteException( error, ex );
        }

        Log.info( String.format( "Stored object at %s", identifier.getURI() ) );

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
     * @throws RemoteException if anything goes wrong in the object deletion.
     * process
     */
    @Override
    public void deleteRepositoryObject( ObjectIdentifier identifier, String logmessage ) throws RemoteException
    {
        String.format( String.format( "Deleting object from %s", identifier.getURI() ) );
        ObjectRepositoryContentType contentTypeForObject;
        try
        {
            contentTypeForObject = this.getContentTypeForObject( identifier );
        }
        catch( IOException ex )
        {
            String error = String.format( "Failed to retrieve content type for object identified by %s: %s", identifier, ex.getMessage() );
            Logger.getLogger( RMIRepositoryServer.class.getName() ).log( Level.WARNING, error, ex );
            //wrap and send to RMI client
            throw new RemoteException( error, ex );
        }
        catch( RepositoryServiceException ex )
        {
            String error = String.format( "Failed to retrieve content type for object identified by %s: %s", identifier, ex.getMessage() );
            Logger.getLogger( RMIRepositoryServer.class.getName() ).log( Level.WARNING, error, ex );
            //wrap and send to RMI client
            throw new RemoteException( error, ex );
        }
        catch( URISyntaxException ex )
        {
            String error = String.format( "Failed to retrieve content type for object identified by %s: %s", identifier, ex.getMessage() );
            Logger.getLogger( RMIRepositoryServer.class.getName() ).log( Level.WARNING, error, ex );
            //wrap and send to RMI client
            throw new RemoteException( error, ex );
        }

        try
        {
            super.deleteObject( identifier, logmessage );
            URI cURI = RepositoryUtilities.generateURI( "file", this.metadataStoragePath, identifier.getName() );
            ObjectIdentifier contentId = new DefaultIdentifier( cURI );
            super.deleteObject( contentId, "deleting content type" );
        }
        catch( IOException ex )
        {
            String error = String.format( "Failed to delete object identified by '%s': %s", identifier, ex.getMessage() );
            Logger.getLogger( RMIRepositoryServer.class.getName() ).log( Level.WARNING, error, ex );
            //wrap and send to RMI client
            throw new RemoteException( error, ex );
        }
        catch( URISyntaxException ex )
        {
            String error = String.format( "Failed to contruct content ID for object identified by '%s': %s", identifier, ex.getMessage() );
            Logger.getLogger( RMIRepositoryServer.class.getName() ).log( Level.WARNING, error, ex );
            //wrap and send to RMI client
            throw new RemoteException( error, ex );
        }
        try
        {
            this.notifyObservers( identifier, null, RepositoryAction.DELETE, contentTypeForObject );
        }
        catch( RepositoryServiceException ex )
        {
            String error = String.format( "Failed to notify on object deletion: %s", ex.getMessage() );
            Logger.getLogger( RMIRepositoryServer.class.getName() ).log( Level.WARNING, error, ex );
            //wrap and send to RMI client
            throw new RemoteException( error, ex );
        }

    }

    private ObjectRepositoryContentType getContentTypeForObject( ObjectIdentifier objectIdentifier ) throws IOException, RepositoryServiceException, URISyntaxException
    {
        String name = objectIdentifier.getName();

        ObjectIdentifier metadataIdentifier = null;
        Log.info( String.format( "Trying to generate uri from '%s' + '%s'", this.metadataStoragePath, name ) );
        URI metaurl = RepositoryUtilities.generateURI( "file", this.metadataStoragePath, name );
        metadataIdentifier = new DefaultIdentifier( metaurl );
        Log.info( String.format( "Constructed identifier for metadata with uri %s", metadataIdentifier.getURI() ) );

        DigitalObject object = super.getObject( metadataIdentifier );

        String contentTypeString = new String( object.getBytes() );

        ObjectRepositoryContentType contentType = BasicContentType.getContentType( contentTypeString );

        return contentType;
    }



    private void registerObservers()
    {
        //read a configuration file for implementations of the ObjectRepositoryService
        // that should be added as observers of actions taken by the RepositoryServer
    }

    @Override
    public synchronized void addObserver( RepositoryObserver observer )
    {
        this.observers.add( observer );
    }

    @Override
    public synchronized void removeObserver( RepositoryObserver observer )
    {
        this.observers.remove( observer );
    }


    @Override
    public void notifyObservers( ObjectIdentifier identifier, DigitalObject object, RepositoryAction action, ObjectRepositoryContentType contentType ) throws RepositoryServiceException
    {
        Log.info( String.format( "Notifying on %s for object %s with contenttype %s", identifier, action, contentType ) );
        synchronized( this.observers )
        {
            for( RepositoryObserver observer : this.observers )
            {
                observer.notifyMe( identifier, object, action, contentType );
            }
        }
    }

    
    @Override
    public List<ObjectRepositoryContentType> registeredContentTypes() throws RemoteException
    {
            registeredContentTypes.addAll( Arrays.asList( BasicContentType.values() ) );
        return registeredContentTypes;
    }

    private void registerContentTypes( ObjectRepositoryContentType[] contentTypeDefinition )
    {
        if( registeredContentTypes.size() < 1 )
        {
            this.registeredContentTypes.addAll( Arrays.asList( contentTypeDefinition ) );
        }
    }

}
