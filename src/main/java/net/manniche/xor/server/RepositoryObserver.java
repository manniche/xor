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

import net.manniche.xor.types.ObjectIdentifier;
import net.manniche.xor.types.ObjectRepositoryContentType;
import net.manniche.xor.types.RepositoryAction;


/**
 * Standard Observer interface. Choosen over {@link java.util.Observer} so
 * that implementing classes are free to use extension capabilities.
 *
 * Clients should add their RepositoryObserver implementations to the
 * ObjectRepository Server implementation in order to be notified about events,
 * as defined in {@link net.manniche.xor.types.RepositoryAction.Action}
 *
 * @author stm
 */
public interface RepositoryObserver {
    public abstract void notifyMe(  ObjectIdentifier identifier, RepositoryAction action, ObjectRepositoryContentType contentType );
}
