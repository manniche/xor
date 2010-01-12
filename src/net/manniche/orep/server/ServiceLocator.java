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

package net.manniche.orep.server;

import net.manniche.orep.types.ObjectRepositoryService;
import net.manniche.orep.types.ObjectRepositoryServiceType;


/**
 * The ServiceLocator class is responsible for returning implementations of
 * Object repository services. Only services (classes) that are defined as an
 * {@link ObjectRepositoryServiceType} can be requested from the ServiceLocator.
 * @author stm
 */
public final class ServiceLocator {

    /**
     * Given the type definition of an Object Repository Service, this method
     * will return an implementation of the Object Repository Service.
     * @param service the type of the service
     * @return the implementation of the service
     * @see ObjectRepositoryService
     * @see ObjectRepositoryServiceType
     */
    @SuppressWarnings( "unchecked" )
    public static Class<ObjectRepositoryService> getImplementation( ObjectRepositoryServiceType<? extends ObjectRepositoryService> service )
    {
        return (Class<ObjectRepositoryService>) service.getClassofService();
    }
}
