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


package net.manniche.xor.storage;

import java.io.IOException;
import java.net.URI;
import net.manniche.xor.types.ObjectRepositoryService;


/**
 *Interface describing the operations that must be implemented by the underlying
 * storage implementation.
 *
 * @author stm
 */
public interface StorageProvider extends ObjectRepositoryService{

    /**
     * Stores an object encoded in a byte array, returning an URI
     * that uniquely identifies the object in the storage implementation. The
     * identification encoded in the returned URL should be globally unique, but
     * the system will not rely on this to be true.
     *
     * The underlying storage implementation should not try to interpret the
     * byte array with respect to converting it into a string or other
     * representations involving guessing the format (and subsequently the
     * encoding) of the object given.
     * 
     * @param object the object encoded as a byte array
     * @param storagePath the path to store this object at
     * @return an URI uniquely identifying the object for retrieval
     * @throws IOException if the object cannot be stored
     */
    public abstract URI save( byte[] object, String storagePath ) throws IOException;

    /**
     * Stores an object encoded in a byte array, using the {@code uri}
     * as unique identifier for subsequent retrievals of the object. The
     * identification encoded in the returned URL should be globally unique, but
     * the system will not rely on this to be true.
     *
     * The underlying storage implementation should not try to interpret the
     * byte array with respect to converting it into a string or other
     * representations involving guessing the format (and subsequently the
     * encoding) of the object given.
     *
     * @param object the object encoded as a byte array
     * @param uri the URI that the object should be identified with
     * @throws IOException if the object cannot be stored
     */
    public abstract void save( byte[] object, URI url, String storagePath) throws IOException;


    /**
     * Retrieves an object stored in the storage implementation, identified by
     * {@code identifier}. The object is returned as a byte[]
     *
     * @param identifier uniquely identifying the object to be retrieved
     * @return the object as a byte[]
     * @throws IOException if the object cannot be retrieved or if the identifier does not identifies an object
     */
    public abstract byte[] get( URI identifier) throws IOException;


    /**
     * Deletes an object in in the storage implementation, identified by 
     * {@code identifier}. If the object is found and successfully deleted, 
     * this method will exit silently. If the object is not found or cannot be
     * deleted, an IOException will be raised.
     * 
     * @param identifier uniquely identifying the object to be deleted
     * @throws IOException if the object does not exist, or cannot be deleted
     */
    public abstract void delete( URI identifier) throws IOException;

    
    /**
     * Closes the StorageProvider implementation. This gives the implementors
     * the opportunity to define actions that defines a properly shutdown of the
     * storage. The {@link net.manniche.xor.server.RepositoryServer server}
     * shall call this method once upon encountering a fatal exception or during
     * normal server shutdown.
     */
    public void close();
}
