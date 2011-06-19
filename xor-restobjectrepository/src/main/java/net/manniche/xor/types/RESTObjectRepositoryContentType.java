package net.manniche.xor.types;

import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 */
@XmlRootElement( name = "DigitalObject" )
public enum RESTObjectRepositoryContentType implements ObjectRepositoryContentType
{
    /**
     * The default/fallback content type.
     */
    BINARY_CONTENT( "application/octet-stream" ),
    /**
     * Defines the content type DublinCore as specified by the Dublin Core
     * Meta Data standard {@link http://dublincore.org}. It is implicit that
     * the contenttype has the mimetype text/xml
     */
    DUBLIN_CORE( "text/xml" );
    
    String mimeType;
    RESTObjectRepositoryContentType( String mime )
    {
        this.mimeType = mime;
    }
    
    public static ObjectRepositoryContentType getContentType( String contentType ) throws TypeNotPresentException
    {
        for( ObjectRepositoryContentType type: BasicContentType.values() )
        {
            if( contentType.toUpperCase().equals( type.toString() ) )
            {
                return type;
            }
        }
        throw new TypeNotPresentException( contentType, new IllegalArgumentException( String.format( "No content type exists for %s", contentType ) ) );
    }
}
