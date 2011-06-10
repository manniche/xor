/*
 *  This file is part of xor.
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
package net.manniche.xor.types;


/**
 * Types of actions that the {@link net.manniche.xor.server.ObjectRepository}
 * can notify Observers about.
 * 
 * @author Steen Manniche
 */
public enum RepositoryAction
{

    /**
     * Indicates that an object has been added to the repository. The repository,
     * by sending this action to {@link net.manniche.xor.server.RepositoryObserver Observers} guarantees
     * that the object has been stored in the {@link net.manniche.xor.storage.StorageProvider} implementation.
     */
    ADD,
    /**
     * Indicates that an existing object has in some way been modified
     */
    MODIFY,
    /**
     * Indicates that an object has been deleted. The repository, by sending
     * this action to Observers guarantees that the object has been
     * permanently deleted from the StorageProvider implementation.
     */
    DELETE,

    /**
     * Indicates that an object was requested by a client. The repository sends
     * notifications on this RepositoryAction immidiately before returning the
     * request. If an exception is thrown in the course of serving the request,
     * no notification will be sent.
     */
    REQUEST;
}
