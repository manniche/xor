/*
 *  This file is part of RMIObjectRepository.
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

package net.manniche.orep.server;

import net.manniche.orep.storage.StorageProvider;


/**
 *
 * @author stm
 */
public class FileBasedLogMessageHandler implements LogMessageHandler{

    private final StorageProvider storageProvider;
    public FileBasedLogMessageHandler( StorageProvider store )
    {
        this.storageProvider = store;
    }

    @Override
    public void commitLogMessage( String className, String methodName, String logMessage )
    {
        throw new UnsupportedOperationException( "Not supported yet." );
    }

}
