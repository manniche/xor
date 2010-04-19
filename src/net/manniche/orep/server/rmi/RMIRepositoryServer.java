/*
 *  This file is part of OREP
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

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.manniche.orep.server.LogMessageHandler;
import net.manniche.orep.server.RepositoryObserver;
import net.manniche.orep.types.ObjectIdentifier;
import net.manniche.orep.storage.StorageProvider;
import net.manniche.orep.types.DigitalObject;
import net.manniche.orep.server.RepositoryServer;

/**
 * The connection handler for RMI requests. This class represents the RMI
 * server implementation.
 * 
 * @author stm
 */
public final class RMIRepositoryServer extends RepositoryServer implements RMIObjectManagement
{
    static final long serialVersionUID = -8975965744773865183L;
    private List<RepositoryObserver> observers;
    private final static Logger Log = Logger.getLogger( RMIRepositoryServer.class.getName() );

    /**
     * Sets up the RMI server for the object repository.
     *
     * @param storage the StorageProvider that handles storage of objects for
     * this RMI server instance
     * @param logMessageHandler handles the log message storing/notifications
     * @throws RemoteException if the server could not be started
     */
    public RMIRepositoryServer( StorageProvider storage, LogMessageHandler logMessageHandler ) throws RemoteException
    {
        super( storage, logMessageHandler );
        this.observers = Collections.synchronizedList( new ArrayList<RepositoryObserver>() );
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

        return digitalObject;
    }


    /**
     * Stores a {@link DigitalObject} {@code data} into the object repository,
     * returning the {@link ObjectIdentifier identifier} uniquely identifying
     * the object.
     * 
     * @param data the object to be stored
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
        Log.info( String.format( "Storing object with identifier %s", identifier ) );
        ObjectIdentifier oIdentifier = null;
        try
        {
            oIdentifier = super.storeObject( data, identifier, logmessage );

        }
        catch( IOException ex )
        {
            String error = String.format( "Failed to store object: %s", ex.getMessage() );
            Logger.getLogger( RMIRepositoryServer.class.getName() ).log( Level.WARNING, error, ex );
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
     * @throws RemoteException if anything goes wrong in the object deletion.
     * process
     */
    @Override
    public void deleteRepositoryObject( ObjectIdentifier identifier, String logmessage ) throws RemoteException
    {
        try
        {
            super.deleteObject( identifier, logmessage );
        }
        catch( IOException ex )
        {
            String error = String.format( "Failed to delete object identified by '%s': %s", identifier, ex.getMessage() );
            Logger.getLogger( RMIRepositoryServer.class.getName() ).log( Level.WARNING, error, ex );
            //wrap and send to RMI client
            throw new RemoteException( error, ex );
        }
    }


    /**
     * Observers who wishes to be notified on repository actions (ie. all the
     * effects of the methods listed in this interface) can register through
     * this method.
     *
     * Is is possible for observers to register more than one time with this
     * implementation. Each registered observer will recieve a separate
     * notification on actions performed that trigger events.
     *
     * @param observer the {@link RepositoryObserver} implementation that
     * wishes to recieve updates
     */
    @Override
    public void addObserver( RepositoryObserver observer )
    {
        this.observers.add( observer );
    }

}
