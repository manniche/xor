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

package net.manniche.xor.server;

import net.manniche.xor.types.ObjectRepositoryService;


/**
 *
 * @author stm
 */
public interface LogMessageHandler extends ObjectRepositoryService{

    /**
     * Commits a log message from the object repository implementation to an
     * underlying storage implementation or a listener.
     * 
     * @param className name of the class that commits the log message
     * @param methodName name of the method from which the log commit fired from
     * @param logMessage the log message itself
     */
    public void commitLogMessage( String className, String methodName, String logMessage );
}
