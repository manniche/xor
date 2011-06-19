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

package net.manniche.xor.rest.server;

import java.util.List;
import net.manniche.xor.types.DigitalObject;
import net.manniche.xor.types.ObjectIdentifier;
import net.manniche.xor.types.RESTObjectIdentifier;
import net.manniche.xor.types.ObjectRepositoryContentType;


/**
 *
 * @author Steen Manniche
 */
interface RESTObjectManagement {

    public DigitalObject getRepositoryObject( RESTObjectIdentifier identifier );
    // public List<DigitalObject> queryRepositoryObjects( String query );
    public ObjectIdentifier storeRepositoryObject( DigitalObject object, ObjectRepositoryContentType contentType, String logmessage );
    public void deleteRepositoryObject( ObjectIdentifier identifier, String logmessage );
}
