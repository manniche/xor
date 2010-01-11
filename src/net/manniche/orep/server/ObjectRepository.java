/*
 *  This file is part of OREP
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

import java.io.IOException;
import javax.xml.stream.XMLStreamException;
import net.manniche.orep.types.DigitalObject;
import net.manniche.orep.types.ObjectIdentifier;

/**
 * Provides the server interface to the object repository connection handler.
 *
 * @author stm
 */
public interface ObjectRepository{

    ObjectIdentifier storeObject( DigitalObject data, String message )throws IOException;

    ObjectIdentifier storeObject( DigitalObject data, ObjectIdentifier identifier, String message )throws IOException;

    DigitalObject getObject( ObjectIdentifier identifier )throws IOException;

    boolean deleteObject( ObjectIdentifier identifier, String logmessage )throws IOException;

}
