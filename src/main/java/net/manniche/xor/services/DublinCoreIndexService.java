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


package net.manniche.xor.services;

import net.manniche.xor.server.RepositoryObserver;
import net.manniche.xor.types.BasicContentType;
import net.manniche.xor.types.ObjectIdentifier;
import net.manniche.xor.types.ObjectRepositoryContentType;
import net.manniche.xor.types.ObjectRepositoryService;
import net.manniche.xor.types.RepositoryAction;


/**
 *
 * @author stm
 */
public final class DublinCoreIndexService extends MetadataIndexService implements RepositoryObserver{

    @Override
    public Class<ObjectRepositoryService> getClassofService()
    {
        throw new UnsupportedOperationException( "Not supported yet." );
    }

    @Override
    public void notifyMe( ObjectIdentifier identifier, RepositoryAction action, ObjectRepositoryContentType contentType )
    {
        if( contentType.equals( BasicContentType.DUBLIN_CORE ) )
        {
            if( action.equals( RepositoryAction.ADD ) )
            {
            }
            else if( action.equals( RepositoryAction.MODIFY ) )
            {
            }
            else if( action.equals( RepositoryAction.DELETE ) )
            {
            }
        }
    }

}
