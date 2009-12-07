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

package net.manniche.orep.server;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.rmi.RemoteException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.stream.XMLStreamException;
import net.manniche.orep.storage.StorageProvider;
import net.manniche.orep.types.DefaultIdentifier;
import net.manniche.orep.types.DigitalObject;
import net.manniche.orep.types.DigitalObjectMeta;
import net.manniche.orep.types.ObjectIdentifier;


/**
 * This (package-private) abstract class ensures correct implementation of the
 * common actions for
 * all implementors of the ObjectManagement interface. Implementors should
 * ideally only implement the communication layer towards the clients and call
 * this class for the central, common, operations.
 *
 * The class can be used only by implementations in the same package (ie. on the
 * server side) 
 * 
 * @author stm
 */
abstract class ObjectRepository implements ObjectManagement {

    private final StorageProvider storage;

    public ObjectRepository( StorageProvider storage )
    {
        this.storage = storage;
    }


    @Override
    public boolean addDataToObject( ObjectIdentifier identifier, InputStream data, String message ) throws RemoteException
    {
        throw new UnsupportedOperationException( "Not supported yet." );
    }


    @Override
    public boolean deleteDataFromObject( ObjectIdentifier objectIdentifier, ObjectIdentifier dataIdentifier ) throws RemoteException
    {
        throw new UnsupportedOperationException( "Not supported yet." );
    }


    @Override
    public boolean deleteObject( ObjectIdentifier identifier ) throws RemoteException
    {
        throw new UnsupportedOperationException( "Not supported yet." );
    }


    @Override
    public InputStream getDataFromObject( ObjectIdentifier objectIdentifier, ObjectIdentifier dataIdentifier ) throws RemoteException
    {
        throw new UnsupportedOperationException( "Not supported yet." );
    }


    @Override
    public DigitalObject getObject( ObjectIdentifier identifier ) throws RemoteException
    {
        try
        {
            byte[] get = this.storage.get( identifier );
        }
        catch( IOException ex )
        {
            String error = String.format( "Failed to retrieve object identified by %s: %s", identifier, ex.getMessage() );
            Logger.getLogger( RMIObjectRepository.class.getName() ).log( Level.SEVERE, error, ex );
            //wrap and send to RMI client
            throw new RemoteException( error, ex );
        }
        throw new UnsupportedOperationException( "Not supported yet." );
    }


    @Override
    public DigitalObjectMeta getObjectMetadata( ObjectIdentifier identifier ) throws RemoteException
    {
        throw new UnsupportedOperationException( "Not supported yet." );
    }


    @Override
    public ObjectIdentifier storeObject( DigitalObject data, DigitalObjectMeta metadata, String message ) throws RemoteException
    {
        try
        {
            ObjectIdentifier objectID = null;
            URI id = null;
            if( null == metadata.getIdentifier() )
            {
                String hash = Integer.toString( data.hashCode() );

                if( hash.startsWith( "-" ) )
                {
                    hash = hash.substring( 1 );
                }

                String fragment = new Long( System.currentTimeMillis() ).toString() + hash;
                // should construct something along the lines of uri://{Long}
                id = new URI( "uri", "", "", fragment );
                objectID = new DefaultIdentifier( id );
            }
            else
            {
                id = metadata.getIdentifier().getIdentifierAsURI();
                objectID = metadata.getIdentifier();
            }
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            metadata.serialize( baos, id.toString() );
            storage.save( data.getBytes(), baos.toByteArray() );
            return objectID;
        }
        catch( URISyntaxException ex )
        {
            String error = String.format( "Could not construct storage location: %s", ex.getMessage() );
            Logger.getLogger( RMIObjectRepository.class.getName() ).log( Level.SEVERE, error, ex );
            //wrap and send to RMI client
            throw new RemoteException( error, ex );
        }
        catch( XMLStreamException ex )
        {
            String error = String.format( "Could not construct metadata: %s", ex.getMessage() );
            Logger.getLogger( RMIObjectRepository.class.getName() ).log( Level.SEVERE, error, ex );
            //wrap and send to RMI client
            throw new RemoteException( error, ex );
        }
        catch( IOException ex )
        {
            String error = String.format( "Failed to store object: %s", ex.getMessage() );
            Logger.getLogger( RMIObjectRepository.class.getName() ).log( Level.SEVERE, error, ex );
            //wrap and send to RMI client
            throw new RemoteException( error, ex );
        }
        finally
        {
            /**
             * If there should be any cleanup todo, we'll let the storage
             * implementation do it.
             */
            this.storage.close();
        }
    }

}
