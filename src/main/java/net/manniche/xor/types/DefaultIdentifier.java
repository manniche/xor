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

import java.io.Serializable;
import java.net.URI;


/**
 * Fallback identifier if the clients have not or cannot provide identifiers
 * @author stm
 * @see ObjectIdentifier
 */
public class DefaultIdentifier implements ObjectIdentifier, Serializable{
    static final long serialVersionUID = 4760792436042998437L;
    private final URI uri;
    private final String path;

    /**
     * Creates a DefaultIdentifier identified by the {@code uri}
     * @param uri defining this identifier
     */
    public DefaultIdentifier( URI uri )
    {
        this.uri = uri;
        this.path = this.uri.getPath();
    }

    /**
     * @return the default identifier as an URI
     */
//    @Override
//    public URI getIdentifierAsURI() throws URISyntaxException
//    {
//        return uri.toURI();
//    }
//
    @Override
    public URI getURI()
    {
        return this.uri;
    }

    @Override
    public String getName()
    {
        return this.path.substring( this.path.lastIndexOf( "/" ) + 1 );
    }

    @Override
    public String getId()
    {
        return this.path;
    }


    /**
     * @return the string "uri" as a prefix for the default identifier.
     */
//    @Override
//    public String getPrefix()
//    {
//        return "uri";
//    }
}
