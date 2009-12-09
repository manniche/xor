/*
 *  This file is part of RMIObjectRepository.
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


package net.manniche.orep.storage;

import net.manniche.orep.search.QueryResult;
import net.manniche.orep.search.Query;
import java.io.IOException;
import java.net.URI;
import net.manniche.orep.types.ObjectIdentifier;
import net.manniche.orep.types.ObjectRepositoryService;


/**
 * Abstraction of the operations supported by the underlying storage 
 * implementation.
 *
 *
 * @author stm
 */
public interface StorageProvider extends ObjectRepositoryService{
    public URI save( byte[] object, byte[] metadata ) throws IOException;
    public void save( byte[] object, byte[] metadatam, URI uri ) throws IOException;
    public QueryResult query( Query query ) throws IOException;
    public byte[] get( ObjectIdentifier identifier) throws IOException;
    public void close();
}
