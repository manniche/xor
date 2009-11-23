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

package net.manniche.orep.types;


/**
 * DigitalObjectMeta defines the metadata for the entire {@link DigitalObject}
 * It is mandatory to provide a {@code DigitalObjectMeta} object whenever a new
 * {@code DigitalObject} is
 * {@link net.manniche.orep.server.ObjectManagement#storeObject stored} in the
 * {@link net.manniche.orep.server.ObjectManagement ObjectRepository}.
 *
 * Implementors must implement this interface when they define their metadata
 * types.
 * @see net.manniche.orep.documents.metadata.DublinCore An example implementation of metadata
 *
 */
public interface DigitalObjectMeta {

    /**
     * The identifier of any DigitalObject in the Object Repository is a 
     * (universally) unique value
     * 
     * @return the identifier of the DigitalObject
     */
    public ObjectIdentifier getIdentifier();

}
