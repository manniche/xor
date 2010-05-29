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


package net.manniche.xor.documents;

import java.io.UnsupportedEncodingException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.transform.TransformerException;
import net.manniche.xor.types.DigitalObject;
import net.manniche.xor.types.RepositoryServiceException;
import net.manniche.xor.utils.XMLUtils;
import org.w3c.dom.Document;


/**
 *
 * @author stm
 */
public class XMLDocumentObject implements DigitalObject{

    private Document doc;


    XMLDocumentObject( Document doc )
    {
        this.doc = doc;
    }

    @Override
    public byte[] getBytes() throws RepositoryServiceException
    {
        byte[] serializedDocument = null;
        try
        {
            serializedDocument = XMLUtils.serializeDocument( doc );
        }
        catch( TransformerException ex )
        {
            String error = String.format( "Could not serialize XML Document: %s", ex.getMessage() );
            Logger.getLogger( XMLDocumentObject.class.getName() ).log( Level.SEVERE, error, ex );
            throw new RepositoryServiceException( error, ex );
        }
        catch( UnsupportedEncodingException ex )
        {
            String error = String.format( "Could not serialize XML Document: %s", ex.getMessage() );
            Logger.getLogger( XMLDocumentObject.class.getName() ).log( Level.SEVERE, null, ex );
            throw new RepositoryServiceException( error, ex );
        }
        return serializedDocument;
    }
}
