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

import java.rmi.Remote;
import java.rmi.RemoteException;
import net.manniche.orep.server.ObjectRepository;
import net.manniche.orep.server.RepositoryObserver;
import net.manniche.orep.types.DigitalObject;
import net.manniche.orep.types.ObjectIdentifier;


/**
 * Interface that defines the RMI operations that can be invoked. For RMI 
 * operations, the  methods defined in this interface are used as  wrappers for
 * the methods defined in the {@link ObjectRepository} interface, which in turn
 * defines the general operations for the object repository.
 * An example implementation of a (naive) RMI client can be found in the
 * {@link RMIObjectRepository} implementation.
 * @author stm
 */
public interface RMIObjectManagement extends ObjectRepository, Remote{


    /**
     * Retrieves and returns an object identified by {@code identifier} from the
     * object repository.
     *
     * @param identifier that uniquely identifies the object within the scope of this server
     * @return a DigitalObject with the identifier {@code identifier}
     * @throws RemoteException
     */
    public DigitalObject getRepositoryObject( ObjectIdentifier identifier ) throws RemoteException;

    /**
     * Stores a DigitalObject into the object repository annotating the process
     * with {@code logmessage}. The method returns an ObjectIdentifier uniquely 
     * identifying the object within the object repository
     *
     * @param data the DigitalObject to be stored
     * @param logmessage a message describing the action
     * @return an ObjectIdentifier that uniquely identifies the object within the scope of this server
     * @throws RemoteException
     */
    public ObjectIdentifier storeRepositoryObject( DigitalObject data , String logmessage ) throws RemoteException;

    /**
     * Deletes an object identified by {@code identifier} using {@code logmessage} 
     * to describe the action.
     *
     * @param identifier
     * @param logmessage
     * @throws RemoteException
     */
    public void deleteRepositoryObject( ObjectIdentifier identifier, String logmessage ) throws RemoteException;

    /**
     * Observers who wishes to be notified on repository actions (ie. all the
     * effects of the methods listed in this interface) can register through
     * this method.
     *
     * @param observer the {@link RepositoryObserver} implementation that
     * wishes to recieve updates
     */
    void addObserver( RepositoryObserver observer ) throws RemoteException;
}
