/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.manniche.server;

import java.io.InputStream;
import java.rmi.Remote;
import java.rmi.RemoteException;
import net.manniche.types.ObjectIdentifier;

/**
 * Provides the server interface to the object repository connection handler.
 *
 * @author stm
 */
public interface ObjectManagement extends Remote{

    ObjectIdentifier storeObject( DigitalObject data, DigitalObjectMeta metadata, String message ) throws RemoteException;

    InputStream getObject( ObjectIdentifier identifier ) throws RemoteException;

    boolean deleteObject( ObjectIdentifier identifier ) throws RemoteException;

    boolean addDataToObject( ObjectIdentifier identifier, InputStream data, String message ) throws RemoteException;

    InputStream getDataFromObject( ObjectIdentifier objectIdentifier, ObjectIdentifier dataIdentifier ) throws RemoteException;

    boolean deleteDataFromObject( ObjectIdentifier objectIdentifier, ObjectIdentifier dataIdentifier ) throws RemoteException;
}
