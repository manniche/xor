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

import net.manniche.xor.types.ObjectRepositoryService;
import net.manniche.xor.types.ObjectRepositoryServiceType;


/**
 * This type describes the search service available to the
 * ObjectManagement server system. It is also defined by the
 * ObjectRepositoryServiceType that defines the services available to the
 * object repository
 *
 * @see ObjectRepositoryServiceType
 *
 * @author Steen Manniche
 */
public enum SearchType implements ObjectRepositoryServiceType<ObjectRepositoryService>
{

    DublinCoreIndexService( DublinCoreIndexService.class );


    private final Class<? extends ObjectRepositoryService> search_service;

    SearchType( Class<? extends SearchProvider> classOfService )
    {
        this.search_service = classOfService;
    }


    @Override
    public final Class<? extends ObjectRepositoryService> getClassofService()
    {
        return this.search_service;
    }


}
