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

    public DigitalObject getRepositoryObject( ObjectIdentifier identifier ) throws RemoteException;

    public ObjectIdentifier storeRepositoryObject( DigitalObject data , String logmessage ) throws RemoteException;

    public void deleteRepositoryObject( ObjectIdentifier identifier, String logmessage ) throws RemoteException;

}
