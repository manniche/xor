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

package net.manniche.xor.server;

import net.manniche.xor.logger.LogMessageHandler;
import java.io.IOException;
import java.net.URI;
import java.util.Vector;
import net.manniche.xor.types.DefaultDigitalObject;
import net.manniche.xor.storage.StorageProvider;
import net.manniche.xor.types.DefaultIdentifier;
import net.manniche.xor.types.DigitalObject;
import net.manniche.xor.types.ObjectIdentifier;
import net.manniche.xor.types.RepositoryAction;
import net.manniche.xor.exceptions.RepositoryServiceException;


/**
 * The RepositoryServer handles the internal and central logic of the repository
 * Extension classes can expose the functionality of this class indirectly; 
 * through their own technology specific implementations. The RepositoryServer
 * defines the core operations that the object repository must support.
 *
 * Extendors to this class are free (and encouraged) to add functionality such
 * as the ability to index and query the object repository, search-functionality
 * or more advanced operations on objects passing through the repository.
 *
 * @author stm
 */
public abstract class RepositoryServer{

    private final StorageProvider repositoryStorageMechanism;
    private final LogMessageHandler logMessageHandler;
    private final Vector< RepositoryObserver > observers;

    protected RepositoryServer( StorageProvider storage, LogMessageHandler logHandler )
    {
        this.repositoryStorageMechanism = storage;
        this.logMessageHandler = logHandler;
        this.observers = new Vector<RepositoryObserver>();
    }

    /**
     * Given data in the form of a DigitalObject and an optional message to the
     * log, this method tries to store the data in the underlying
     * {@link net.manniche.orep.storage.StorageProvider storage} implementation.
     *
     * @param data the DigitalObject containing data to be stored
     * @param message an optional logmessage describing the action. If null or
     * the empty string is passed, the implementation can decide what to write
     * in the log system, if any.
     * @return an ObjectIdentifier that (globally) uniquely identifies the stored
     * data for later identification
     * @throws IOException if the object cannot be stored for a given reason
     */
    protected ObjectIdentifier storeObject( DigitalObject data, String message ) throws IOException, RepositoryServiceException
    {
        return this.storeObject( data, null, message );
    }


    /**
     * Identical to {@link net.manniche.orep.server.ObjectRepository#storeObject(net.manniche.orep.types.DigitalObject, java.lang.String)},
     * except that this method allows the client to pass an identifier along
     * with the object. If the server cannot deliver the exact same identifier
     * as the client, when the data is stored, an IOException must be thrown and
     * the whole operation rolled back.
     *
     * @param data the DigitalObject containing data to be stored
     * @param identifier the ObjectIdentifier that the object should be stored with
     * @param message an optional logmessage describing the action. If null or
     * the empty string is passed, the implementation can decide what to write
     * in the log system, if any.
     * @return an ObjectIdentifier that is identical to the one delivered by
     * the client
     * data for later identification
     * @throws IOException if the object cannot be stored for a given reason
     * @throws IOException
     */
    protected ObjectIdentifier storeObject( DigitalObject data, ObjectIdentifier identifier, String message ) throws RepositoryServiceException, IOException
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
        this.notifyObservers( identifier, RepositoryAction.ADD );
        return objectID;
    }


    /**
     * Given an ObjectIdentifier this method retrieves the corresponding
     * DigitalObject.
     * @param identifier an ObjectIdentifier identifying the data with this server
     * @return the requested DigitalObject or a IOException if the ObjectIdentifier designates nothing
     * @throws IOException if the DigitalObject can't be retrieved for a given reason
     */
    protected DigitalObject getObject( ObjectIdentifier identifier ) throws IOException
    {
        byte[] object = this.repositoryStorageMechanism.get( identifier.getIdentifierAsURI() );
        this.notifyObservers( identifier, RepositoryAction.REQUEST );
        return new DefaultDigitalObject( object );
    }


    /**
     * Given an ObjectIdentifier this method will try to delete a DigitalObject
     * on the server. An IOException is thrown if the operation can't be carried
     * through.
     *
     * @param identifier an ObjectIdentifier identifying the data with this server
     * @param logmessage an optional logmessage describing the action. If null or
     * the empty string is passed, the implementation can decide what to write
     * in the log system, if any.
     * @throws IOException if the DigitalObject couldn't be deleted
     */
    protected void deleteObject( ObjectIdentifier identifier, String logmessage ) throws IOException
    {
        this.logMessageHandler.commitLogMessage( RepositoryServer.class.getName(), "deleteObject", logmessage );
        this.repositoryStorageMechanism.delete( identifier.getIdentifierAsURI() );
        this.notifyObservers( identifier, RepositoryAction.DELETE );
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
    public void addObserver( RepositoryObserver observer )
    {
        this.observers.add( observer );
    }


    /**
     * Notifies all registered observers of the action taken by the 
     * {@code RepositoryServer}. This method notifies all and only the observers
     * registered at the point of calling the method. Any observer added during
     * the execution of this method will not be called until the method is
     * called again for a subsequent {@link RepositoryAction}
     * 
     * @param identifier the identifier of the object on which the action was performed
     * @param action the action taken
     * @see RepositoryAction
     */
    public void notifyObservers( ObjectIdentifier identifier, RepositoryAction action )
    {
        synchronized( this.observers )
        {
            for( RepositoryObserver observer : this.observers )
            {
                observer.notifyMe( identifier, action );
            }
        }
    }
}
