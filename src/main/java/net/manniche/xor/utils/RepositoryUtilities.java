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

package net.manniche.xor.utils;


import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.logging.Logger;

/**
 *
 * @author stm
 */
public class RepositoryUtilities {

    private static final Logger Log = Logger.getLogger( RepositoryUtilities.class.getName() );

    public static URI generateURI( String scheme, String path, String hash ) throws IOException, URISyntaxException
    {
        URL id = null;
        if( hash.startsWith( "-" ) )
        {
            hash = hash.substring( 1 );
        }


        id = new URL( scheme, path, hash );
        Log.info( String.format( "Generated url '%s' from %s, %s and %s", id, scheme, path, hash ) );
        return id.toURI();
    }

}
