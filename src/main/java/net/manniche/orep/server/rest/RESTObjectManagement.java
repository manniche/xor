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

package net.manniche.orep.server.rest;

import java.util.List;
import net.manniche.orep.types.DigitalObject;
import net.manniche.orep.types.ObjectIdentifier;


/**
 *
 * @author stm
 */
interface RESTObjectManagement {

    public DigitalObject getRepositoryObject( ObjectIdentifier identifier );
    public List<DigitalObject> queryRepositoryObjects( String query );
    public ObjectIdentifier storeRepositoryObject( DigitalObject object, String logmessage );
    public void deleteRepositoryObject( ObjectIdentifier identifier, String logmessage );
}
