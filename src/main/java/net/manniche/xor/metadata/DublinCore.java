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
package net.manniche.xor.metadata;

import java.io.ByteArrayOutputStream;
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

import net.manniche.xor.types.DigitalObject;
import net.manniche.xor.exceptions.RepositoryServiceException;

/**
 * DublinCore is a class implementation of the Dublin Core Metadata Element Set,
 * Version 1.1 (http://dublincore.org/documents/dces/)
 *
 */
public class DublinCore implements DigitalObject
{

    /** DC standard dataformatting: */
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat( "yyyy-MM-dd'T'HH:mm:ss.SSS" );
    /** The map to keep all our dc values:*/
    private Map<DublinCoreElement, List<String>> dcvalues;

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
                /** We've constructed the URI String just above,
                 * so get out of my face with this exception!
                 */
            }
        }


        @Override
        public String getPrefix( String URI )
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
            String ssp = this.uri.getSchemeSpecificPart();
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
        List<String> valuelist = new ArrayList<String>();
        dcvalues = new HashMap<DublinCoreElement, List<String>>();
        valuelist.add( identifier );
        dcvalues.put( DublinCoreElement.ELEMENT_IDENTIFIER, valuelist );
    }


    /**
     * Initializes a Dublin Core element with values taken from {@code
     * inputValues}, identified by {@code identifier}
     * @param identifier An unambiguous reference to the resource within a given
     * context. Recommended best practice is to use the digital repository object
     * identifier.
     */
    public DublinCore( final String identifier, final Map<DublinCoreElement, List<String>> inputValues )
    {
        dcvalues = new HashMap<DublinCoreElement, List<String>>( inputValues );
        List<String> valuelist = new ArrayList<String>();
        valuelist.add( identifier);

        dcvalues.put( DublinCoreElement.ELEMENT_IDENTIFIER, valuelist );
    }


    public void putContributor( String contributor )
    {
        List<String> vals = dcvalues.get( DublinCoreElement.ELEMENT_CONTRIBUTOR );
        vals.add( contributor );
        dcvalues.put( DublinCoreElement.ELEMENT_CONTRIBUTOR, vals );
    }


    public void putCoverage( String coverage )
    {
        List<String> vals = dcvalues.get( DublinCoreElement.ELEMENT_COVERAGE );
        vals.add( coverage );
        dcvalues.put( DublinCoreElement.ELEMENT_COVERAGE, vals );
    }


    public void putCreator( String creator )
    {
        List<String> vals = dcvalues.get( DublinCoreElement.ELEMENT_CREATOR );
        vals.add( creator );
        dcvalues.put( DublinCoreElement.ELEMENT_CREATOR, vals );
    }


    public void putDate( Date date )
    {
        List<String> vals = dcvalues.get( DublinCoreElement.ELEMENT_DATE );
        String stringdate = dateFormat.format( date );
        vals.add( stringdate );
        dcvalues.put( DublinCoreElement.ELEMENT_DATE, vals );
    }


    public void putDescription( String description )
    {
        List<String> vals = dcvalues.get( DublinCoreElement.ELEMENT_DESCRIPTION );
        vals.add( description );
        dcvalues.put( DublinCoreElement.ELEMENT_DESCRIPTION, vals );
    }


    public void putFormat( String format )
    {
        List<String> vals = dcvalues.get( DublinCoreElement.ELEMENT_FORMAT );
        vals.add( format );
        dcvalues.put( DublinCoreElement.ELEMENT_FORMAT, vals );
    }


    public void putIdentifier( String identifier )
    {
        List<String> vals = dcvalues.get( DublinCoreElement.ELEMENT_IDENTIFIER );
        vals.add( identifier );
        dcvalues.put( DublinCoreElement.ELEMENT_IDENTIFIER, vals );
    }


    public void putLanguage( String language )
    {
        List<String> vals = dcvalues.get( DublinCoreElement.ELEMENT_LANGUAGE );
        vals.add( language );
        dcvalues.put( DublinCoreElement.ELEMENT_LANGUAGE, vals );
    }


    public void putPublisher( String publisher )
    {
        List<String> vals = dcvalues.get( DublinCoreElement.ELEMENT_PUBLISHER );
        vals.add( publisher );
        dcvalues.put( DublinCoreElement.ELEMENT_PUBLISHER, vals );
    }


    public void putRelation( String relation )
    {
        List<String> vals = dcvalues.get( DublinCoreElement.ELEMENT_RELATION );
        vals.add( relation );
        dcvalues.put( DublinCoreElement.ELEMENT_RELATION, vals );
    }


    public void putRights( String rights )
    {
        List<String> vals = dcvalues.get( DublinCoreElement.ELEMENT_RIGHTS );
        vals.add( rights );
        dcvalues.put( DublinCoreElement.ELEMENT_RIGHTS, vals );
    }


    public void putSource( String source )
    {
        List<String> vals = dcvalues.get( DublinCoreElement.ELEMENT_SOURCE );
        vals.add( source );
        dcvalues.put( DublinCoreElement.ELEMENT_SOURCE, vals );
    }


    public void putSubject( String subject )
    {
        List<String> vals = dcvalues.get( DublinCoreElement.ELEMENT_SUBJECT );
        vals.add( subject );
        dcvalues.put( DublinCoreElement.ELEMENT_SUBJECT, vals );
    }


    public void putTitle( String title )
    {
        List<String> vals = dcvalues.get( DublinCoreElement.ELEMENT_TITLE );
        vals.add( title );
        dcvalues.put( DublinCoreElement.ELEMENT_TITLE, vals );
    }


    public void putType( String type )
    {
        List<String> vals = dcvalues.get( DublinCoreElement.ELEMENT_TYPE );
        vals.add( type );
        dcvalues.put( DublinCoreElement.ELEMENT_TYPE, vals );
    }


    public int elementCount()
    {
        return dcvalues.size();
    }


    @Override
    public byte[] getBytes() throws RepositoryServiceException
    {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try
        {
            // Create an output factory
            XMLOutputFactory xmlof = XMLOutputFactory.newInstance();
            XMLStreamWriter xmlw;
            xmlw = xmlof.createXMLStreamWriter( out );
            String dc_prefix = DublinCoreNamespace.DC.prefix;
            String dc_namesp = DublinCoreNamespace.DC.uri.toString();
            xmlw.setDefaultNamespace( dc_namesp );
            xmlw.writeStartDocument();
            xmlw.writeStartElement( dc_namesp, dc_prefix );
            xmlw.writeNamespace( dc_prefix, dc_namesp );
            for( Entry<DublinCoreElement, List<String>> set : dcvalues.entrySet() )
            {
                if( set.getValue() != null )
                {
                    xmlw.writeStartElement( dc_namesp, set.getKey().localName() );
                    for( String val: set.getValue() )
                    {
                        xmlw.writeCharacters( val );
                    }
                    xmlw.writeEndElement();
                }
            }
            xmlw.writeEndElement();
            xmlw.writeEndDocument();
            xmlw.flush();
        }
        catch( XMLStreamException ex )
        {
            String error = String.format( "Failed to serialize DublinCore data: %s", ex.getMessage() );
            Logger.getLogger( DublinCore.class.getName() ).log( Level.SEVERE, error, ex );
        }
        return out.toByteArray();
    }


}
