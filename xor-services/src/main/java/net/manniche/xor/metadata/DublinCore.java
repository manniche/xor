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

package net.manniche.xor.metadata;

import java.io.ByteArrayOutputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.EnumMap;
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
import net.manniche.xor.metadata.DublinCore.DublinCoreElement;
import net.manniche.xor.types.ObjectIdentifier;

/**
 * DublinCore is a class implementation of the Dublin Core Metadata Element Set,
 * Version 1.1 (http://dublincore.org/documents/dces/)
 *
 */
public class DublinCore implements DigitalObject
{

    /** DC standard dataformatting: */
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat( "yyyy-MM-dd'T'HH:mm:ss.SSS" );
    /** The map to keep all dc values:*/
    private Map<DublinCoreElement, List<String>> dcvalues;

    public enum DublinCoreElement
    {

        TITLE( "title" ),
        CREATOR( "creator" ),
        SUBJECT( "subject" ),
        DESCRIPTION( "description" ),
        PUBLISHER( "publisher" ),
        CONTRIBUTOR( "contributor" ),
        DATE( "date" ),
        TYPE( "type" ),
        FORMAT( "format" ),
        IDENTIFIER( "identifier" ),
        SOURCE( "source" ),
        LANGUAGE( "language" ),
        RELATION( "relation" ),
        COVERAGE( "coverage" ),
        RIGHTS( "rights" );
        private String localname;

        DublinCoreElement( String localName )
        {
            this.localname = localName;
        }


        String localName()
        {
            return this.localname;
        }

        
        public static boolean hasLocalName( String name )
        {
            for( DublinCoreElement dcelem: DublinCoreElement.values() )
            {
                if( dcelem.localName().equals( name ) )
                {
                    return true;
                }
            }
            return false;
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
                //this is highly unlikely, and is probably caused by something
                //other than the above string being passed
                throw new IllegalArgumentException( String.format( "%s is not a valid URI for DublinCore", URI ) );
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
    }

    /**
     * Initializes an empty Dublin Core element, identified by {@code identifier}
     * @param identifier An unambiguous reference to the resource within a given
     * context. Recommended best practice is to use the digital repository object
     * identifier ({@link ObjectIdentifier}).
     */
    public DublinCore( String identifier )
    {
        List<String> valuelist = new ArrayList<String>();
        dcvalues = new EnumMap<DublinCoreElement, List<String>>( DublinCoreElement.class );
        valuelist.add( identifier );
        dcvalues.put( DublinCoreElement.IDENTIFIER, valuelist );
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
        if( inputValues.containsKey( DublinCoreElement.DATE ) )
        {
            List<String> dates = inputValues.get( DublinCoreElement.DATE );
            List<String> validatedDates = new ArrayList<String>( dates.size() );
            for( String date: dates )
            {
                try
                {
                    Date d = dateFormat.parse( date );
                    String stringDate = dateFormat.format( d );
                    validatedDates.add( stringDate );
                }
                catch( ParseException ex )
                {
                    throw new IllegalArgumentException( String.format( "The string %s could not be parsed as a Date: %s", date, ex.getMessage() ) );
                }
            }
            inputValues.remove( DublinCoreElement.DATE );
            inputValues.put( DublinCoreElement.DATE, validatedDates );
        }
        dcvalues = new EnumMap<DublinCoreElement, List<String>>( inputValues );
        List<String> valuelist = new ArrayList<String>();
        valuelist.add( identifier);

        dcvalues.put( DublinCoreElement.IDENTIFIER, valuelist );
    }


    public void putContributor( String contributor )
    {
        putValueForElement( DublinCoreElement.CONTRIBUTOR, contributor);
    }


    public void putCoverage( String coverage )
    {
        putValueForElement( DublinCoreElement.COVERAGE, coverage);
    }


    public void putCreator( String creator )
    {
        putValueForElement( DublinCoreElement.CREATOR, creator);
    }


    public void putDate( Date date )
    {
        String stringDate = dateFormat.format( date );
        putValueForElement( DublinCoreElement.DATE, stringDate);
    }


    public void putDescription( String description )
    {
        putValueForElement( DublinCoreElement.DESCRIPTION, description);
    }


    public void putFormat( String format )
    {
        putValueForElement( DublinCoreElement.FORMAT, format);
    }


    public void putIdentifier( String identifier )
    {
        putValueForElement( DublinCoreElement.IDENTIFIER, identifier);
    }


    public void putLanguage( String language )
    {
        putValueForElement( DublinCoreElement.LANGUAGE, language);
    }


    public void putPublisher( String publisher )
    {
        putValueForElement( DublinCoreElement.PUBLISHER, publisher);
    }


    public void putRelation( String relation )
    {
        putValueForElement( DublinCoreElement.RELATION, relation );
    }


    public void putRights( String rights )
    {
        putValueForElement( DublinCoreElement.RIGHTS, rights);
    }


    public void putSource( String source )
    {
        putValueForElement( DublinCoreElement.SOURCE, source);
    }


    public void putSubject( String subject )
    {
        putValueForElement( DublinCoreElement.SUBJECT, subject);
    }


    public void putTitle( String title )
    {
        putValueForElement( DublinCoreElement.TITLE, title);
    }


    public void putType( String type )
    {
        putValueForElement( DublinCoreElement.TYPE, type);
    }

    public List< String > getValueForElement( DublinCoreElement elem )
    {
        List<String> vals = dcvalues.get( elem );
        return vals != null ? vals : new ArrayList< String >( 0 );
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
                    for( String val: set.getValue() )
                    {
                        xmlw.writeStartElement( dc_namesp, set.getKey().localName() );
                        xmlw.writeCharacters( val );
                        xmlw.writeEndElement();
                    }
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

    private void putValueForElement( DublinCoreElement elem, String elemValue )
    {
        List<String> vals = dcvalues.get( elem );
        if( null == vals )
        {
            vals = new ArrayList<String>();
        }
        vals.add( elemValue );
        dcvalues.put( elem, vals );
    }
}
