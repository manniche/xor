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


import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.util.logging.Logger;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class XMLUtils
{
    private static final Logger Log = Logger.getLogger( XMLUtils.class.getName() );

    public static byte[] serializeDocument( Document root ) throws TransformerException, UnsupportedEncodingException
    {
        Source source = new DOMSource( (Node) root );
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();

        return serializeXML( transformer, source );
    }

    public static byte[] serializeFragment( Document root ) throws TransformerException, UnsupportedEncodingException
    {
        Source source = new DOMSource( (Node) root );
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        transformer.setOutputProperty( javax.xml.transform.OutputKeys.OMIT_XML_DECLARATION, "yes" );

        return serializeXML( transformer, source );
    }

    private static byte[] serializeXML( Transformer transformer, Source input ) throws UnsupportedEncodingException, TransformerException
    {
        StringWriter stringWriter = new StringWriter();
        Result result = new StreamResult( stringWriter );
        transformer.transform( input, result );

        String streamString = stringWriter.getBuffer().toString();
        byte[] byteArray = streamString.getBytes( "UTF-8" );

        return byteArray;
    }


    public static String documentToString( Document document ) throws TransformerException
    {
        Node rootNode = (Node) document.getDocumentElement();
        Source source = new DOMSource( rootNode );

        StringWriter stringWriter = new StringWriter();
        Result result = new StreamResult( stringWriter );
        TransformerFactory factory = TransformerFactory.newInstance();
        Transformer transformer = factory.newTransformer();
        transformer.transform( source, result );

        return stringWriter.getBuffer().toString().replace( "\n", "");
    }

    public static Document stringToDocument( String assumedXml ) throws ParserConfigurationException, SAXException, IOException
    {
        Document doc = null;
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        doc = builder.parse( new InputSource( new ByteArrayInputStream( assumedXml.getBytes() ) ) );

        return doc;
    }
}
