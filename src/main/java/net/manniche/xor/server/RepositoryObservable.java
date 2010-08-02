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

package net.manniche.xor.server;

import net.manniche.xor.exceptions.RepositoryServiceException;
import net.manniche.xor.types.DigitalObject;
import net.manniche.xor.types.ObjectIdentifier;
import net.manniche.xor.types.ObjectRepositoryContentType;
import net.manniche.xor.types.RepositoryAction;


/**
 *
 * @author Steen Manniche
 */
public interface RepositoryObservable {


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
    void addObserver( RepositoryObserver observer );

    /**
     * Removes an observer registered with the repository server. If an
     * observer that is not registered is requested removed, this method will
     * silently fail to removed the observer.
     *
     * @param observer the {@link RepositoryObserver} implementation that
     * wishes to be removed from the list of observers
     */
    void removeObserver( RepositoryObserver observer );

    /**
     * Notifies all registered observers of the action taken by the
     * {@code RepositoryServer}. This method notifies all and only the observers
     * registered at the point of calling the method. Any observer added during
     * the execution of this method will not be called until the method is
     * called again for a subsequent {@link RepositoryAction}
     *
     * @param identifier the identifier of the object on which the action was performed
     * @param object the object on which the action was performed (can be null, e.g. if the action is 'deleted')
     * @param action the action taken
     * @param contentType the content type of the object
     *
     * @throws RepositoryServiceException if data cannot be retrieved from the {@link DigitalObject}
     *
     * @see RepositoryAction
     * @see ObjectRepositoryContentType
     */
    void notifyObservers( ObjectIdentifier identifier, DigitalObject object, RepositoryAction action, ObjectRepositoryContentType contentType ) throws RepositoryServiceException;


}
