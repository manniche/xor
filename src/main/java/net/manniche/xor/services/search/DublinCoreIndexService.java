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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.manniche.xor.exceptions.RepositoryServiceException;
import net.manniche.xor.server.RepositoryObserver;
import net.manniche.xor.types.BasicContentType;
import net.manniche.xor.types.DigitalObject;
import net.manniche.xor.types.ObjectIdentifier;
import net.manniche.xor.types.ObjectRepositoryContentType;
import net.manniche.xor.types.RepositoryAction;


/**
 *
 * @author Steen Manniche
 */
public final class DublinCoreIndexService implements RepositoryObserver, SearchProvider{

    private final File index = new File( "index" );
    private static final Logger Log = Logger.getLogger( DublinCoreIndexService.class.getName() );
    public DublinCoreIndexService(  )
    {
    }

    @Override
    public void notifyMe( ObjectIdentifier identifier, DigitalObject object, RepositoryAction action, ObjectRepositoryContentType contentType )
    {
        if( contentType.equals( BasicContentType.DUBLIN_CORE ) )
        {
            if( action.equals( RepositoryAction.ADD ) )
            {
                final FileOutputStream fos;
                try
                {
                    fos = new FileOutputStream( index, true );
                    fos.write( object.getBytes() );
                    fos.flush();
                    fos.close();
                }
                catch( RepositoryServiceException ex )
                {
                    Log.log( Level.WARNING, String.format( "Could not index object '%s': %s", identifier.getURI(), ex.getMessage() ) );
                }
                catch( FileNotFoundException ex )
                {
                    Log.log( Level.WARNING, String.format( "Could not index object '%s': %s", identifier.getURI(), ex.getMessage() ) );
                }
                catch( IOException ex )
                {
                    Log.log( Level.WARNING, String.format( "Could not index object '%s': %s", identifier.getURI(), ex.getMessage() ) );
                }
            }
            else if( action.equals( RepositoryAction.MODIFY ) )
            {

            }
            else if( action.equals( RepositoryAction.DELETE ) )
            {
            }
        }
    }

    @Override
    public List<String> search( Query query, int maximumResults )
    {
        throw new UnsupportedOperationException( "Not supported yet." );
    }

}
