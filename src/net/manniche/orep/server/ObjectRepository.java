/*
 *  This file is part of OREP
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
package net.manniche.orep.server;

import java.io.IOException;
import net.manniche.orep.types.DigitalObject;
import net.manniche.orep.types.ObjectIdentifier;


/**
 * Provides the server interface to the object repository connection handler.
 * Every server implementations must implement this interface and the methods
 * defined herein should reflect the possible functionality and the limitations
 * of the system. 
 *
 * @author stm
 */
public interface ObjectRepository
{

    /**
     * Given data in the form of a DigitalObject and an optional message to the
     * log, this method tries to store the data in the underlying
     * {@link net.manniche.orep.storage.StorageProvider storage} implementation.
     *
     * @param data the DigitalObject containing data to be stored
     * @param message an optional logmessage describing the action. If null or
     * the empty string is passed, the implementation can decide what to write
     * in the log system, if any.
     * @return an ObjectIdentifier that (globally) uniquely identifies the stored
     * data for later identification
     * @throws IOException if the object cannot be stored for a given reason
     */
    ObjectIdentifier storeObject( DigitalObject data, String message ) throws IOException;


    /**
     * Identical to {@link net.manniche.orep.server.ObjectRepository#storeObject(net.manniche.orep.types.DigitalObject, java.lang.String)},
     * except that this method allows the client to pass an identifier along
     * with the object. If the server cannot deliver the exact same identifier
     * as the client, when the data is stored, an IOException must be thrown and
     * the whole operation rolled back.
     * 
     * @param data the DigitalObject containing data to be stored
     * @param identifier the ObjectIdentifier that the object should be stored with
     * @param message an optional logmessage describing the action. If null or
     * the empty string is passed, the implementation can decide what to write
     * in the log system, if any.
     * @return an ObjectIdentifier that is identical to the one delivered by
     * the client
     * data for later identification
     * @throws IOException if the object cannot be stored for a given reason
     * @throws IOException
     */
    ObjectIdentifier storeObject( DigitalObject data, ObjectIdentifier identifier, String message ) throws IOException;


    /**
     * Given an ObjectIdentifier this method retrieves the corresponding
     * DigitalObject.
     * @param identifier an ObjectIdentifier identifying the data with this server
     * @return the requested DigitalObject or a IOException if the ObjectIdentifier designates nothing
     * @throws IOException if the DigitalObject can't be retrieved for a given reason
     */
    DigitalObject getObject( ObjectIdentifier identifier ) throws IOException;


    /**
     * Given an ObjectIdentifier this method will try to delete a DigitalObject
     * on the server. An IOException is thrown if the operation can't be carried
     * through.
     *
     * @param identifier an ObjectIdentifier identifying the data with this server
     * @param message an optional logmessage describing the action. If null or
     * the empty string is passed, the implementation can decide what to write
     * in the log system, if any.
     * @throws IOException if the DigitalObject couldn't be deleted
     */
    void deleteObject( ObjectIdentifier identifier, String logmessage ) throws IOException;


}
