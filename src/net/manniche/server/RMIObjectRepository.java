/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.manniche.server;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.rmi.RemoteException;
import net.manniche.types.ObjectIdentifier;
import net.manniche.storage.StorageProvider;


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

    public ObjectIdentifier storeObject( InputStream data, String message ) throws RemoteException
    {
        throw new UnsupportedOperationException( "Not supported yet." );
    }


    public InputStream getObject( ObjectIdentifier identifier ) throws RemoteException
    {
        throw new UnsupportedOperationException( "Not supported yet." );
    }


    public boolean deleteObject( ObjectIdentifier identifier ) throws RemoteException
    {
        throw new UnsupportedOperationException( "Not supported yet." );
    }


    public boolean addDataToObject( ObjectIdentifier identifier, InputStream data, String message ) throws RemoteException
    {
        throw new UnsupportedOperationException( "Not supported yet." );
    }


    public InputStream getDataFromObject( ObjectIdentifier objectIdentifier, ObjectIdentifier dataIdentifier ) throws RemoteException
    {
        throw new UnsupportedOperationException( "Not supported yet." );
    }


    public boolean deleteDataFromObject( ObjectIdentifier objectIdentifier, ObjectIdentifier dataIdentifier ) throws RemoteException
    {
        throw new UnsupportedOperationException( "Not supported yet." );
    }

}
