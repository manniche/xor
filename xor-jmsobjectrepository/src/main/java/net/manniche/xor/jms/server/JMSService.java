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

package net.manniche.xor.jms.server;

import javax.jms.ServerSession;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.QueueConnectionFactory;
import javax.naming.InitialContext;

import net.manniche.xor.server.RepositoryObserver;
import net.manniche.xor.server.RepositoryServer;
import net.manniche.xor.storage.StorageProvider;
import net.manniche.xor.types.DigitalObject;
import net.manniche.xor.types.ObjectIdentifier;
import net.manniche.xor.types.ObjectRepositoryContentType;
import net.manniche.xor.types.RepositoryAction;

/**
 *
 */
public final class JMSService extends RepositoryServer {

    private final String storagePath;
    private final String metadataStoragePath;

    JMSService( StorageProvider storage, String storagePath, String metadataStoragePath )
    {
        super( storage );
        this.storagePath = storagePath;
        this.metadataStoragePath = metadataStoragePath;
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

    public ObjectIdentifier storeRepositoryObject( DigitalObject object, ObjectRepositoryContentType contentType, String logmessage )
    {
        throw new UnsupportedOperationException( "Not implemented yet" );
    }    
    
    public ObjectIdentifier storeRepositoryObject( DigitalObject object, ObjectRepositoryContentType contentType, ObjectIdentifier identifier, String logmessage )
    {
        throw new UnsupportedOperationException( "Not implemented yet" );
    }    

    public DigitalObject getRepositoryObject( ObjectIdentifier identifier )
    {
        throw new UnsupportedOperationException( "Not implemented yet" );
    }
    
    public void deleteRepositoryObject( ObjectIdentifier identifier, String logmessage )
    {
        throw new UnsupportedOperationException( "Not implemented yet" );
    }
    
    private void createConnection( String id )
    {
        
    }

    @Override
    public void notifyObservers( ObjectIdentifier identifier, DigitalObject object, RepositoryAction action, ObjectRepositoryContentType contentType )
    {
        throw new UnsupportedOperationException( "Not supported yet." );
    }

}
