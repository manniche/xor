/*
 *  This file is part of OREP
 *  Copyright © 2009, Steen Manniche.
 * 
 *  RMIObjectRepository is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 * 
 *  RMIObjectRepositoryis distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 * 
 *  You should have received a copy of the GNU General Public License
 *  along with RMIObjectRepository.  If not, see <http://www.gnu.org/licenses/>.
 */

package net.manniche.orep.server.rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;
import net.manniche.orep.server.ObjectRepository;
import net.manniche.orep.types.DigitalObject;
import net.manniche.orep.types.ObjectIdentifier;


/**
 * Interface that defines the RMI operations that can be invoked. To ease the use
 * for Implementors, they are encouraged to use the methods defined in this
 * interface as wrappers for the methods defined in the {@link ObjectRepository}
 * interface, that defines the general operations for the object repository.
 * This behaviour is exemplified in the {@link RMIObjectRepository} implementation.
 * @author stm
 */
public interface RMIObjectManagement extends ObjectRepository, Remote{

    public DigitalObject getRepositoryObject( ObjectIdentifier identifier ) throws RemoteException;

    public ObjectIdentifier storeRepositoryObject( DigitalObject data , String logmessage ) throws RemoteException;

    public void deleteRepositoryObject( ObjectIdentifier identifier, String logmessage ) throws RemoteException;

}
