/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.manniche.storage;

import java.io.IOException;
import net.manniche.types.ObjectIdentifier;


/**
 * Abstraction of the operations supported by the underlying storage 
 * implementation.
 *
 *
 * @author stm
 */
public interface StorageProvider{
    public void save( byte[] object ) throws IOException;
    public QueryResult query( Query query ) throws IOException;
    public byte[] get( ObjectIdentifier identifier) throws IOException;
}
