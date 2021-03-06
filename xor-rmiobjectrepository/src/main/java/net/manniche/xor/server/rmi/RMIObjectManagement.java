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

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;
import net.manniche.xor.types.DigitalObject;
import net.manniche.xor.types.ObjectIdentifier;
import net.manniche.xor.types.ObjectRepositoryContentType;


/**
 * Interface that defines the RMI operations that can be invoked. For RMI 
 * operations, the  methods defined in this interface are used as  wrappers for
 * the methods defined in the {@link ObjectRepository} interface, which in turn
 * defines the general operations for the object repository.
 * An example implementation of a (naive) RMI client can be found in the
 * {@link RMIObjectRepository} implementation.
 * @author Steen Manniche
 */
public interface RMIObjectManagement extends Remote{


    /**
     * Retrieves and returns an object identified by {@code identifier} from the
     * object repository.
     *
     * @see ObjectRepository
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
     * @param contentType the type of content being stored, as defined by the server
     * @param logmessage a message describing the action
     * @return an ObjectIdentifier that uniquely identifies the object within the scope of this server
     * @throws RemoteException
     */
    public ObjectIdentifier storeRepositoryObject( DigitalObject data, ObjectRepositoryContentType contentType, String logmessage ) throws RemoteException;

    /**
     * Deletes an object identified by {@code identifier} using {@code logmessage} 
     * to describe the action.
     *
     * @param identifier Identifier that uniquely defines the object within the server
     * @param contentType the type of content being deleted
     * @param logmessage a String describing the action
     * @throws RemoteException
     */
    public void deleteRepositoryObject( ObjectIdentifier identifier, String logmessage ) throws RemoteException;

    /**
     * Retrieves a list of {@link ObjectRepositoryContentType content types}
     * registered with the RMI server
     *
     * @return a list of registered ObjectRepositoryContentType
     * @throws RemoteException
     */
    public List<ObjectRepositoryContentType> registeredContentTypes() throws RemoteException;
}
