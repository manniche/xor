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


package net.manniche.types;

/**
 * Uniquely identifies objects in the object repository and in CargoContainers
 */
public interface ObjectIdentifier {

    /**
     * Accessor for the string representation of the identifier
     * @return a String containing the identifier in a String representation
     */
    public String getIdentifier();

    /**
     * Accessor for the namespace (if any) on the object identifier.
     * @return a String containing the namespace (if the identifier has one)
     */
    public String getNamespace();
}
