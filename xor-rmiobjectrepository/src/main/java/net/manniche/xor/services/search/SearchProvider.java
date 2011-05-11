/*
 *  This file is part of xor
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

package net.manniche.xor.services.search;

import java.util.List;
import net.manniche.xor.types.ObjectRepositoryService;


/**
 * Interface describing the operations that must be provided by a search
 * service implementation.
 *
 * @author Steen Manniche
 */
public interface SearchProvider extends ObjectRepositoryService{

    /**
     * Performs a search on whatever data has been indexed or in other ways
     * stored and organized by the implementing service. This search is composed
     * of a {@link Query} object that represents a complex query expression and
     * an integer specifying the maximum number of results that should be in the
     * {@link List} that is returned.
     *
     * @param query a query object representing the query
     * @param maximumResults the maximum number of results returned
     * @return a {@link List} containing the search results
     */
    public List<String> search( Query query, int maximumResults );

}
