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

import java.util.Map;


/**
 * represents a free-form String search query. The storage implementation
 * defines which fields are searchable in the storage model. Implementors are
 * required to implement the {@link ObjectFields} interface for specifying to
 * the client(s) which fields are searchable
 * @author stm
 */
public interface Query {

    /**
     * Gives the client the opportunity to save the Query for later use. 
     * 
     * @return an identifier for the saved Query
     */
    public int saveQuery();

    /**
     * Lists all saved {@link Query Queries}
     *
     * @return a Map containing identifiers and their corresponding saved queries
     */
    public Map<Integer, Query> getSavedQueries();
    
}
