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


package net.manniche.server;

import java.io.InputStream;
import java.rmi.RemoteException;
import net.manniche.types.ObjectIdentifier;
import net.manniche.storage.StorageProvider;
import net.manniche.types.DigitalObject;
import net.manniche.types.DigitalObjectMeta;


/**
 *
 * @author stm
 */
public class RMIObjectRepository implements ObjectManagement{

    private StorageProvider storage;

    public RMIObjectRepository( StorageProvider storage )
    {
        this.storage = storage;
    }

    @Override
    public ObjectIdentifier storeObject( DigitalObject data, DigitalObjectMeta metadata, String message ) throws RemoteException
    {
        throw new UnsupportedOperationException( "Not supported yet." );
    }

    @Override
    public DigitalObject getObject( ObjectIdentifier identifier ) throws RemoteException
    {
        throw new UnsupportedOperationException( "Not supported yet." );
    }


    @Override
    public DigitalObjectMeta getObjectMetadata( ObjectIdentifier identifier ) throws RemoteException
    {
        throw new UnsupportedOperationException( "Not supported yet." );
    }


    @Override
    public boolean deleteObject( ObjectIdentifier identifier ) throws RemoteException
    {
        throw new UnsupportedOperationException( "Not supported yet." );
    }


    @Override
    public boolean addDataToObject( ObjectIdentifier identifier, InputStream data, String message ) throws RemoteException
    {
        throw new UnsupportedOperationException( "Not supported yet." );
    }


    @Override
    public InputStream getDataFromObject( ObjectIdentifier objectIdentifier, ObjectIdentifier dataIdentifier ) throws RemoteException
    {
        throw new UnsupportedOperationException( "Not supported yet." );
    }


    @Override
    public boolean deleteDataFromObject( ObjectIdentifier objectIdentifier, ObjectIdentifier dataIdentifier ) throws RemoteException
    {
        throw new UnsupportedOperationException( "Not supported yet." );
    }
}