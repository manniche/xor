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

/**
 * Content types defined for metadata in the object repository
 *
 * @see BasicContentType
 */

public enum MetadataContentType implements ObjectRepositoryContentType
{
    /**
     * Defines the content type DublinCore as specified by the Dublin Core
     * Meta Data standard {@link http://dublincore.org}. It is implicit that
     * the contenttype has the mimetype text/xml
     */
    DUBLIN_CORE( "text/xml" );

    final String mimeType;
    MetadataContentType( String mime )
    {
        this.mimeType = mime;
    }

    public static ObjectRepositoryContentType getContentType( String contentType ) throws TypeNotPresentException
    {
        for( MetadataContentType type : MetadataContentType.values() )
        {
            if( contentType.toUpperCase().equals( type.toString() ) )
            {
                return type;
            }
        }
        throw new TypeNotPresentException( contentType, new IllegalArgumentException( String.format( "No content type exists for %s", contentType ) ) );
    }
}
