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


package net.manniche.xor.services.search;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.EnumMap;

import net.manniche.xor.exceptions.RepositoryServiceException;
import net.manniche.xor.metadata.DublinCore;
import net.manniche.xor.metadata.DublinCore.DublinCoreElement;
import net.manniche.xor.server.RepositoryObserver;
import net.manniche.xor.types.BasicContentType;
import net.manniche.xor.types.DigitalObject;
import net.manniche.xor.types.ObjectIdentifier;
import net.manniche.xor.types.ObjectRepositoryContentType;
import net.manniche.xor.types.RepositoryAction;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

/**
 *
 * @author Steen Manniche
 */
public final class DublinCoreIndexService implements RepositoryObserver, SearchProvider{

    private final File index = new File( "index" );
    private static final Logger Log = Logger.getLogger( DublinCoreIndexService.class.getName() );
    public DublinCoreIndexService(  )
    {
    }

    @Override
    public void notifyMe( ObjectIdentifier identifier, DigitalObject object, RepositoryAction action, ObjectRepositoryContentType contentType )
    {
        if( contentType.equals( BasicContentType.DUBLIN_CORE ) )
        {
            if( action.equals( RepositoryAction.ADD ) )
            {
                final FileOutputStream fos;
                try
                {
                    DublinCore dc = new DublinCore( identifier.getId(), parseDCXml( object ) );
                    fos = new FileOutputStream( index, true );
                    fos.write( identifier.getURI().toString().getBytes() );
                    fos.write( " : ".getBytes() );
                    for( String value : dc.getValueForElement( DublinCoreElement.TITLE ) )
                    {
                        fos.write( dc.getBytes() );
                        //fos.write( value.getBytes() );
                    }
                    fos.write( "\n".getBytes() );
                    fos.flush();
                    fos.close();
                }
                catch( FileNotFoundException ex )
                {
                    Log.log( Level.WARNING, String.format( "Could not index object '%s': %s", identifier.getURI(), ex.getMessage() ) );
                }
                catch( IOException ex )
                {
                    Log.log( Level.WARNING, String.format( "Could not index object '%s': %s", identifier.getURI(), ex.getMessage() ) );
                }
                catch( XMLStreamException ex )
                {
                    Log.log( Level.WARNING, String.format( "Could not index object '%s': %s", identifier.getURI(), ex.getMessage() ) );
                }
                catch( RepositoryServiceException ex )
                {
                    Log.log( Level.WARNING, String.format( "Could not index object '%s': %s", identifier.getURI(), ex.getMessage() ) );
                }
            }
            else if( action.equals( RepositoryAction.MODIFY ) )
            {

            }
            else if( action.equals( RepositoryAction.DELETE ) )
            {
            }
        }
    }

    private Map< DublinCoreElement, List< String > > parseDCXml( DigitalObject object ) throws XMLStreamException, RepositoryServiceException
    {
        Map< DublinCoreElement, List< String > > dcvalues = new EnumMap< DublinCoreElement, List< String > >( DublinCoreElement.class );
        
        XMLInputFactory infac = XMLInputFactory.newInstance();
        XMLEventReader eventReader = infac.createXMLEventReader( new ByteArrayInputStream( object.getBytes() ) );

        StartElement element;
        Characters chars;

        DublinCoreElement elementName = null;
        XMLEvent event = null;
        while ( eventReader.hasNext())
        {
            try
            {
                event = (XMLEvent) eventReader.next();
            }
            catch( NoSuchElementException ex )
            {
                String error = String.format( "Could not parse incoming data, previously correctly parsed content from stream was: %s", event.toString() );
                throw new IllegalStateException( error, ex );
            }
            
            switch ( event.getEventType() )
            {
                case XMLStreamConstants.START_ELEMENT:
                    element = event.asStartElement();
                    // TODO: hardcoded prefix value, fix with value from NamespaceContext enum
                    if ( element.getName().getPrefix().equals( "dc" ) )
                    {
                        if( DublinCoreElement.hasLocalName( element.getName().getLocalPart() ) )
                        {
                            elementName = DublinCoreElement.valueOf( element.getName().getLocalPart().toUpperCase() );
                            Logger.getLogger( DublinCoreIndexService.class.getName() ).log( Level.INFO, element.getName().getPrefix() + elementName );
                        }
                    }
                    break;
                case XMLStreamConstants.CHARACTERS:
                    chars = event.asCharacters();
                    if( ! chars.getData().trim().isEmpty() )
                    {
                        List<String> dcelem = null;
                        if( null != elementName )
                        {
                            if( dcvalues.containsKey( elementName ) )
                            {
                                dcelem = dcvalues.get( elementName );
                            }
                            else
                            {
                                dcelem = new ArrayList<String>();
                            }

                            dcelem.add( chars.getData() );
                            dcvalues.put( elementName, dcelem );
                            elementName = null;
                        }
                    }
                    break;
            }
        }

        eventReader.close();

        return dcvalues;
    }

    @Override
    public List<String> search( Query query, int maximumResults )
    {
        throw new UnsupportedOperationException( "Not supported yet." );
    }

}
