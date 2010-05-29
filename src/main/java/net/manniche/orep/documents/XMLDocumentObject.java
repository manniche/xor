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


package net.manniche.orep.documents;

import java.io.UnsupportedEncodingException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.transform.TransformerException;
import net.manniche.orep.types.DigitalObject;
import net.manniche.orep.utils.XMLUtils;
import org.w3c.dom.Document;


/**
 *
 * @author stm
 */
public class XMLDocumentObject implements DigitalObject{

    private Document doc;

    XMLDocumentObject()
    {

    }

    XMLDocumentObject( Document doc )
    {
        this.doc = doc;
    }

    @Override
    public byte[] getBytes()
    {
        byte[] serializedDocument = null;
        try
        {
            serializedDocument = XMLUtils.serializeDocument( doc );
        }
        catch( TransformerException ex )
        {
            Logger.getLogger( XMLDocumentObject.class.getName() ).log( Level.SEVERE, null, ex );
        }
        catch( UnsupportedEncodingException ex )
        {
            Logger.getLogger( XMLDocumentObject.class.getName() ).log( Level.SEVERE, null, ex );
        }
        return serializedDocument;
    }
}
