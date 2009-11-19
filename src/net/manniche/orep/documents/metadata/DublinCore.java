/*
This file is part of opensearch.
Copyright © 2009, Dansk Bibliotekscenter a/s,
Tempovej 7-11, DK-2750 Ballerup, Denmark. CVR: 15149043

opensearch is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

opensearch is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with opensearch.  If not, see <http://www.gnu.org/licenses/>.
 */
/**
 * \file
 * \brief
 */
package net.manniche.orep.documents.metadata;

import java.io.OutputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.namespace.NamespaceContext;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import net.manniche.orep.types.DigitalObjectMeta;
import net.manniche.orep.types.ObjectIdentifier;


/**
 * DublinCore is a class implementation of the Dublin Core Metadata Element Set,
 * Version 1.1 (http://dublincore.org/documents/dces/)
 *
 */
public class DublinCore implements DigitalObjectMeta
{

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat( "yyyy-MM-dd'T'HH:mm:ss.SSS" );
    private Map<DublinCoreElement, String> dcvalues;

    private class DublinCoreIdentifier implements ObjectIdentifier
    {
        private final String identifier;
        DublinCoreIdentifier( String identifier )
        {
            this.identifier = identifier;
        }

        @Override
        public URI getIdentifier()
        {
                return DublinCoreNamespace.DC.getURI( this.identifier );
        }


        @Override
        public String getPrefix()
        {
            throw new UnsupportedOperationException( "Not supported yet." );
        }


    }

    public enum DublinCoreElement
    {

        ELEMENT_TITLE( "title" ),
        ELEMENT_CREATOR( "creator" ),
        ELEMENT_SUBJECT( "subject" ),
        ELEMENT_DESCRIPTION( "description" ),
        ELEMENT_PUBLISHER( "publisher" ),
        ELEMENT_CONTRIBUTOR( "contributor" ),
        ELEMENT_DATE( "date" ),
        ELEMENT_TYPE( "type" ),
        ELEMENT_FORMAT( "format" ),
        ELEMENT_IDENTIFIER( "identifier" ),
        ELEMENT_SOURCE( "source" ),
        ELEMENT_LANGUAGE( "language" ),
        ELEMENT_RELATION( "relation" ),
        ELEMENT_COVERAGE( "coverage" ),
        ELEMENT_RIGHTS( "rights" );
        private String localname;

        DublinCoreElement( String localName )
        {
            this.localname = localName;
        }


        String localName()
        {
            return this.localname;
        }


    }

    enum DublinCoreNamespace implements NamespaceContext
    {
        DC( "dc", "http://purl.org/dc/elements/1.1" ),
        DCMI( "dcmitype", "http://purl.org/dc/dcmitype" ),
        DCTERMS( "dcterms", "http://purl.org/dc/terms" );

        private String prefix;
        private URI uri;

        DublinCoreNamespace( String prefix, String URI )
        {
            this.prefix = prefix;
            try
            {
                this.uri = new URI( URI );
            }
            catch( URISyntaxException ex )
            {
                /** We've constructed the URI String just above, at compile time,
                * so get out of my face with this exception!
                */
            }
        }

        @Override
        public String getPrefix( String URI)
        {
            String retval = null;
            for( DublinCoreNamespace dc : this.values() )
            {
                if( dc.uri.toString().equals( URI ) )
                {
                    retval = dc.prefix;
                }
            }
            return retval;
        }

        @Override
        public String getNamespaceURI( String prefix )
        {
            String retval = null;
            for( DublinCoreNamespace dc : this.values() )
            {
                if( dc.prefix.equals( prefix ) )
                {
                    retval = dc.uri.toString();
                }
            }
            return retval;
        }

        @Override
        public Iterator<String> getPrefixes( String URI )
        {
            List<String> prefixes = new ArrayList<String>();
            for( DublinCoreNamespace dc : this.values() )
            {
                if( dc.uri.toString().equals( URI ) )
                {
                    prefixes.add( dc.prefix );
                }
            }
            return prefixes.iterator();
        }

        public URI getURI( String fragment )
        {
            String scheme = this.uri.getScheme();
            String ssp    = this.uri.getSchemeSpecificPart();
            URI retval = null;
            try
            {
                retval = new URI( scheme, ssp, fragment );
            }
            catch( URISyntaxException ex )
            {
                Logger.getLogger( DublinCore.class.getName() ).log( Level.SEVERE, String.format( "It looks like fragment contains some invalid characters", fragment ), ex );
            }
            return retval;
        }
    }

    /**
     * Initializes an empty Dublin Core element, identified by {@code identifier}
     * @param identifier An unambiguous reference to the resource within a given
     * context. Recommended best practice is to use the digital repository object
     * identifier.
     */
    public DublinCore( String identifier )
    {
        dcvalues = new HashMap<DublinCoreElement, String>();
        dcvalues.put( DublinCoreElement.ELEMENT_IDENTIFIER, identifier );
    }


    /**
     * Initializes a Dublin Core element with values taken from {@code
     * inputValues}, identified by {@code identifier}
     * @param identifier An unambiguous reference to the resource within a given
     * context. Recommended best practice is to use the digital repository object
     * identifier.
     */
    public DublinCore( String identifier, Map<DublinCoreElement, String> inputValues )
    {
        dcvalues = new HashMap<DublinCoreElement, String>( inputValues );
        dcvalues.put( DublinCoreElement.ELEMENT_IDENTIFIER, identifier );
    }


    @Override
    public ObjectIdentifier getIdentifier()
    {
        return new DublinCoreIdentifier( dcvalues.get( DublinCoreElement.ELEMENT_IDENTIFIER ) );
    }


    public void setContributor( String contributor )
    {
        dcvalues.put( DublinCoreElement.ELEMENT_CONTRIBUTOR, contributor );
    }


    public void setCoverage( String coverage )
    {
        dcvalues.put( DublinCoreElement.ELEMENT_COVERAGE, coverage );
    }


    public void setCreator( String creator )
    {
        dcvalues.put( DublinCoreElement.ELEMENT_CREATOR, creator );
    }


    public void setDate( Date date )
    {
        String stringdate = dateFormat.format( date );
        dcvalues.put( DublinCoreElement.ELEMENT_DATE, stringdate );
    }


    public void setDescription( String description )
    {
        dcvalues.put( DublinCoreElement.ELEMENT_DESCRIPTION, description );
    }


    public void setFormat( String format )
    {
        dcvalues.put( DublinCoreElement.ELEMENT_FORMAT, format );
    }


    public void setIdentifier( String identifier )
    {
        dcvalues.put( DublinCoreElement.ELEMENT_IDENTIFIER, identifier );
    }


    public void setLanguage( String language )
    {
        dcvalues.put( DublinCoreElement.ELEMENT_LANGUAGE, language );
    }


    public void setPublisher( String publisher )
    {
        dcvalues.put( DublinCoreElement.ELEMENT_PUBLISHER, publisher );
    }


    public void setRelation( String relation )
    {
        dcvalues.put( DublinCoreElement.ELEMENT_RELATION, relation );
    }


    public void setRights( String rights )
    {
        dcvalues.put( DublinCoreElement.ELEMENT_RIGHTS, rights );
    }


    public void setSource( String source )
    {
        dcvalues.put( DublinCoreElement.ELEMENT_SOURCE, source );
    }


    public void setSubject( String subject )
    {
        dcvalues.put( DublinCoreElement.ELEMENT_SUBJECT, subject );
    }


    public void setTitle( String title )
    {
        dcvalues.put( DublinCoreElement.ELEMENT_TITLE, title );
    }


    public void setType( String type )
    {
        dcvalues.put( DublinCoreElement.ELEMENT_TYPE, type );
    }


    public int elementCount()
    {
        return dcvalues.size();
    }

    /* Example of output:
    <oai_dc:dc xmlns:dc="http://purl.org/dc/elements/1.1/"
    xmlns:oai_dc="http://www.openarchives.org/OAI/2.0/oai_dc/"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xsi:schemaLocation="http://www.openarchives.org/OAI/2.0/oai_dc/
    http://www.openarchives.org/OAI/2.0/oai_dc.xsd">
    <dc:title>Harry Potter og Fønixordenen</dc:title>
    <dc:creator>Joanne K. Rowling</dc:creator>
    <dc:type>Bog</dc:type>
    <dc:identifier>710100:25082427</dc:identifier>
    <dc:source>Harry Potter and the Order of the Phoenix</dc:source>
    <dc:relation/>
    </oai_dc:dc>
     */

    public void serialize( OutputStream out, String identifier ) throws XMLStreamException
    {
        // Create an output factory
        XMLOutputFactory xmlof = XMLOutputFactory.newInstance();
        XMLStreamWriter xmlw;

        xmlw =
        xmlof.createXMLStreamWriter( out );

        String dc_prefix = DublinCoreNamespace.DC.prefix;
        String dc_namesp = DublinCoreNamespace.DC.uri.toString();
        xmlw.setDefaultNamespace( dc_namesp );

        xmlw.writeStartDocument();
        xmlw.writeStartElement( dc_namesp, dc_prefix );
        xmlw.writeNamespace( dc_prefix, dc_namesp );

        for( Entry<DublinCoreElement, String> set : dcvalues.entrySet() )
        {
            xmlw.writeStartElement( dc_namesp, set.getKey().localName() );
            if( set.getValue() != null )
            {
                xmlw.writeCharacters( set.getValue() );
            }

            xmlw.writeEndElement();
        }

        xmlw.writeEndElement();//closes "oai_dc:dc" element
        xmlw.writeEndDocument();//closes document
        xmlw.flush();
    }
}
