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

package net.manniche.xor.server.rest;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.DELETE;
import net.manniche.xor.server.RepositoryObserver;
import net.manniche.xor.server.RepositoryServer;
import net.manniche.xor.storage.StorageProvider;
import net.manniche.xor.types.DigitalObject;
import net.manniche.xor.types.ObjectIdentifier;

import javax.ws.rs.Path;  
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import net.manniche.xor.exceptions.RepositoryServiceException;
import net.manniche.xor.server.RepositoryObservable;
import net.manniche.xor.types.DefaultIdentifier;
import net.manniche.xor.types.ObjectRepositoryContentType;
import net.manniche.xor.types.RepositoryAction;
import net.manniche.xor.utils.RepositoryUtilities;

/**
 * REST implementation of the ObjectRepository.
 *
 * Retrieval operations:
 * GET: http://localhost/xor/objects/1234567890
 *
 * will retrive object with uri name 1234567890
 *
 * Mutating operations:
 * POST: http://localhost/xor/objects/1234567890
 *
 * will overwrite object with uri name 1234567890
 *
 * Creating operations:
 * PUT: http://localhost/xor/objects/1234567890
 *
 * will put an object that will be identified with uri name 1234567890
 *
 * Deleting operations:
 * DELETE: http://localhost/xor/objects/1234567890
 *
 * will delete object identified by uri name 1234567890
 *
 * @author Steen Manniche
 */
@Path( "/xor" )
public final class RESTRepositoryRelayer extends RepositoryServer implements RESTObjectManagement, RepositoryObservable
{

    private final StorageProvider storage;
    private final String storagePath;
    private final String metadataStoragePath;
    
    public RESTRepositoryRelayer( StorageProvider storage, String storagePath, String metadataStoragePath )
    {
        super( storage );
        this.storage = storage;
        this.storagePath = storagePath;
        this.metadataStoragePath = metadataStoragePath;
    }

    @Path( "{identifier}" )
    @GET
    @Produces( MediaType.APPLICATION_OCTET_STREAM )
    @Override
    public DigitalObject getRepositoryObject( @PathParam( "identifier" ) ObjectIdentifier identifier )
    {
        DigitalObject digobj = null;
        try
        {
            digobj = super.getObject( identifier );
        }
        catch( RepositoryServiceException ex )
        {
            String error = String.format( "Failed to retrieve object identified by %s: %s", identifier, ex.getMessage() );
            Logger.getLogger( RESTRepositoryRelayer.class.getName() ).log( Level.WARNING, error, ex );
            /** TODO: review exception type and response */
            throw new WebApplicationException( ex, Response.Status.NOT_FOUND );
        }

        return digobj;
    }

    @Path( "{query}" )
    @Override
    public List<DigitalObject> queryRepositoryObjects( @PathParam( "query" ) String query )
    {
        throw new UnsupportedOperationException( "Not supported yet." );
    }

    @POST
    @Override
    public synchronized ObjectIdentifier storeRepositoryObject( DigitalObject object, ObjectRepositoryContentType contentType, String logmessage )
    {
        ObjectIdentifier objectid = null;
        try
        {
            objectid = super.storeObject( object.getBytes(), logmessage );
        }
        catch( RepositoryServiceException ex )
        {
            String error = String.format( "Could not store object: %s", ex.getMessage() );
            Logger.getLogger( RESTRepositoryRelayer.class.getName() ).log( Level.SEVERE, error, ex );
            /** TODO: review exception type and response */
            throw new WebApplicationException( ex, Response.Status.INTERNAL_SERVER_ERROR );
        }
        //store the contenttype
        try
        {
            URI contentURI = RepositoryUtilities.generateURI( "file", this.metadataStoragePath, objectid.getName() );
            ObjectIdentifier contentIdentifier = new DefaultIdentifier( contentURI );

            super.storeObject( contentType.toString().getBytes(), contentIdentifier, "Storing content type" );
        }
        catch( URISyntaxException ex )
        {
            String error = String.format( "Failed to store object content type: %s", ex.getMessage() );
            Logger.getLogger( RESTRepositoryRelayer.class.getName() ).log( Level.WARNING, error, ex );
            /** TODO: review exception type and response */
            throw new WebApplicationException( ex, Response.Status.INTERNAL_SERVER_ERROR );
        }
        catch( RepositoryServiceException ex )
        {
            String error = String.format( "Failed to store object content type: %s", ex.getMessage() );
            Logger.getLogger( RESTRepositoryRelayer.class.getName() ).log( Level.WARNING, error, ex );
            /** TODO: review exception type and response */
            throw new WebApplicationException( ex, Response.Status.INTERNAL_SERVER_ERROR );
        }
        catch( IOException ex )
        {
            String error = String.format( "Failed to store object content type: %s", ex.getMessage() );
            Logger.getLogger( RESTRepositoryRelayer.class.getName() ).log( Level.WARNING, error, ex );
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
        catch( RepositoryServiceException ex )
        {
            String error = String.format( "Failed to delete object identified by '%s': %s", identifier, ex.getMessage() );
            Logger.getLogger( RESTRepositoryRelayer.class.getName() ).log( Level.WARNING, error, ex );
            /** TODO: review exception type and response */
            throw new WebApplicationException( ex, Response.Status.INTERNAL_SERVER_ERROR );
        }
    }

    @Override
    public void addObserver( RepositoryObserver observer )
    {
        throw new UnsupportedOperationException( "Not supported yet." );
    }


    @Override
    public void removeObserver( RepositoryObserver observer )
    {
        throw new UnsupportedOperationException( "Not supported yet." );
    }


    @Override
    public void notifyObservers( ObjectIdentifier identifier, DigitalObject object, RepositoryAction action, ObjectRepositoryContentType contentType )
    {
        throw new UnsupportedOperationException( "Not supported yet." );
    }

}
