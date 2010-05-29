/*
 *  This file is part of OREP.
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

package net.manniche.orep.server.jms;

import javax.jms.ServerSession;
import net.manniche.orep.server.LogMessageHandler;
import net.manniche.orep.server.RepositoryObserver;
import net.manniche.orep.server.RepositoryServer;
import net.manniche.orep.storage.StorageProvider;
/**
 *
 * @author stm
 */
public class JMSService extends RepositoryServer {

    JMSService(StorageProvider storage, LogMessageHandler logMessageHandler )
    {
        super( storage, logMessageHandler );
    }

    @Override
    public void addObserver( RepositoryObserver observer )
    {
        throw new UnsupportedOperationException( "Not supported yet." );
    }

}
