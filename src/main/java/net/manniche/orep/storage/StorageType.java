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
package net.manniche.orep.storage;

import net.manniche.orep.types.ObjectRepositoryService;
import net.manniche.orep.types.ObjectRepositoryServiceType;


/**
 * This type describes the storage implementations available to the
 * ObjectManagement server system. It is also defined by the
 * ObjectRepositoryServiceType that defines the services available to the
 * object repository
 *
 * @see ObjectRepositoryServiceType
 * 
 * @author stm
 */
public enum StorageType implements ObjectRepositoryServiceType<ObjectRepositoryService>
{

    /**
     * Filesystem backed storage implementation
     */
    FileStorage( FileStorage.class ),

    /**
     * Database backed storage implementation
     */
    DBStorage( DBStorage.class );
    private Class<StorageProvider> storage_type;

    @SuppressWarnings( "unchecked" )
    StorageType( Class<? extends StorageProvider> klass )
    {
        //we _know_ this is a ServiceProvider:
        this.storage_type = (Class<StorageProvider>) klass;
    }


    @Override
    @SuppressWarnings( "unchecked" )
    public Class<ObjectRepositoryService> getClassofService()
    {
        return (Class<ObjectRepositoryService>) (Object) this.storage_type;
    }


}
