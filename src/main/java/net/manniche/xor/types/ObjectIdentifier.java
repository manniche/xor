/*
 *  This file is part of xor
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


package net.manniche.xor.types;

import java.net.URI;


/**
 * Uniquely identifies objects in the object repository and in CargoContainers
 */
public interface ObjectIdentifier {

    /**
     * Accessor for the identification of the objects, expressed as an URI
     *
     * @return an URI identifying the location of the object
     */
    public URI getURI();

    /**
     * Accessor for the prefix (if any) on the object identifier.
     * @return a String containing the prefix (if the implementors provide one)
     */
    //public String getPrefix();

    public String getId();

    /**
     * Accessor for the 'name' of the object. Implementors are free to
     * specify what constitutes a name of an object, but the name of an object
     * must be consistent with the URL of the identifier. Specifically,
     * if the scheme is 'file:', the path is '/path', the object name '123',
     * then the URL of the identifier must be 'file:/path/123' and getName()
     * should return '123'.
     *
     * @return the name of the object
     */
    public String getName();
}
