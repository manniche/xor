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

package net.manniche.xor.server.rest;

import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.DELETE;
import net.manniche.xor.server.LogMessageHandler;
import net.manniche.xor.server.RepositoryServer;
import net.manniche.xor.storage.StorageProvider;
import net.manniche.xor.types.DigitalObject;
import net.manniche.xor.types.ObjectIdentifier;

import javax.ws.rs.Path;  
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import net.manniche.xor.types.RepositoryServiceException;

/**
 *
 * @author stm
 */
@Path( "orep" )
public final class RESTRepositoryRelayer extends RepositoryServer implements RESTObjectManagement{

    private final StorageProvider storage;
    private final LogMessageHandler logHandler;
    
    public RESTRepositoryRelayer( StorageProvider storage, LogMessageHandler logHandler )
    {
        super( storage, logHandler );
        this.storage = storage;
        this.logHandler = logHandler;
    }

    @GET
    @Override
    public DigitalObject getRepositoryObject( ObjectIdentifier identifier )
    {
        DigitalObject digobj = null;
        try
        {
            digobj = super.getObject( identifier );
        }
        catch( IOException ex )
        {
            String error = String.format( "Failed to retrieve object identified by %s: %s", identifier, ex.getMessage() );
            Logger.getLogger( RESTRepositoryRelayer.class.getName() ).log( Level.WARNING, error, ex );
            /** TODO: review exception type and response */
            throw new WebApplicationException( ex, Response.Status.NOT_FOUND );
        }

        return digobj;
    }

    @GET
    @Override
    public List<DigitalObject> queryRepositoryObjects( String query )
    {
        throw new UnsupportedOperationException( "Not supported yet." );
    }

    @POST
    @Override
    public synchronized ObjectIdentifier storeRepositoryObject( DigitalObject object, String logmessage )
    {
        ObjectIdentifier objectid = null;
        try
        {
            objectid = super.storeObject( object, logmessage );
        }
        catch( IOException ex )
        {
            String error = String.format( "Could not store object: %s", ex.getMessage() );
            Logger.getLogger( RESTRepositoryRelayer.class.getName() ).log( Level.SEVERE, error, ex );
            /** TODO: review exception type and response */
            throw new WebApplicationException( ex, Response.Status.INTERNAL_SERVER_ERROR );
        }
        catch( RepositoryServiceException ex )
        {
            String error = String.format( "Could not store object: %s", ex.getMessage() );
            Logger.getLogger( RESTRepositoryRelayer.class.getName() ).log( Level.SEVERE, error, ex );
            /** TODO: review exception type and response */
            throw new WebApplicationException( ex, Response.Status.INTERNAL_SERVER_ERROR );
        }
        return objectid;
    }

    @DELETE
    @Override
    public synchronized void deleteRepositoryObject( ObjectIdentifier identifier, String logmessage )
    {
        try
        {
            super.deleteObject( identifier, logmessage );
        }
        catch( IOException ex )
        {
            String error = String.format( "Failed to delete object identified by '%s': %s", identifier, ex.getMessage() );
            Logger.getLogger( RESTRepositoryRelayer.class.getName() ).log( Level.WARNING, error, ex );
            /** TODO: review exception type and response */
            throw new WebApplicationException( ex, Response.Status.INTERNAL_SERVER_ERROR );
        }
    }

}
