/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.manniche.xor.metadata;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Date;
import java.util.EnumMap;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Steen Manniche
 */
public class DublinCoreTest {

    private DublinCore simple_dc;
    private static final long setTimeInMillis = 1279657804865L;
    private static final String setTimeAsString = "2010-07-20T22:30:04.865";
    private static final String dc_with_identifier = "<?xml version=\"1.0\" ?><dc xmlns:dc=\"http://purl.org/dc/elements/1.1\"><dc:identifier>1234567890</dc:identifier></dc>";
    private static final String dc_full = "<?xml version=\"1.0\" ?><dc xmlns:dc=\"http://purl.org/dc/elements/1.1\"><dc:title>title</dc:title><dc:creator>creator</dc:creator><dc:subject>subject</dc:subject><dc:description>description</dc:description><dc:publisher>publisher</dc:publisher><dc:contributor>contributor</dc:contributor><dc:date>2010-07-20T22:30:04.865</dc:date><dc:type>type</dc:type><dc:type>second_type</dc:type><dc:format>format</dc:format><dc:identifier>90</dc:identifier><dc:source>source</dc:source><dc:language>language</dc:language><dc:relation>relation</dc:relation><dc:coverage>coverage</dc:coverage><dc:rights>rights</dc:rights></dc>";
    public DublinCoreTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception
    {
    }


    @AfterClass
    public static void tearDownClass() throws Exception
    {
    }

    @Before
    public void setUp() 
    {
        simple_dc = new DublinCore( "1234567890" );
    }

    @Test
    public void simpleConstructorHasIdentifier() throws Exception
    {
        assertTrue( new String( simple_dc.getBytes() ).equals( dc_with_identifier ) );
    }

    @Test
    public void constructorCarriesvalues() throws Exception
    {
        Map<DublinCore.DublinCoreElement, List<String>> map = new EnumMap<DublinCore.DublinCoreElement, List<String>>( DublinCore.DublinCoreElement.class );
        List<String> ls = new ArrayList<String>();
        ls.add( "contributor" );
        map.put( DublinCore.DublinCoreElement.ELEMENT_CONTRIBUTOR, ls );
        ls = new ArrayList<String>();
        ls.add( "coverage" );
        map.put( DublinCore.DublinCoreElement.ELEMENT_COVERAGE, ls );
        ls = new ArrayList<String>();
        ls.add( "creator" );
        map.put( DublinCore.DublinCoreElement.ELEMENT_CREATOR, ls );
        ls = new ArrayList<String>();
        ls.add( setTimeAsString );
        map.put( DublinCore.DublinCoreElement.ELEMENT_DATE, ls );
        ls = new ArrayList<String>();
        ls.add( "description" );
        map.put( DublinCore.DublinCoreElement.ELEMENT_DESCRIPTION, ls );
        ls = new ArrayList<String>();
        ls.add( "format" );
        map.put( DublinCore.DublinCoreElement.ELEMENT_FORMAT, ls );
        ls = new ArrayList<String>();
        ls.add( "language" );
        map.put( DublinCore.DublinCoreElement.ELEMENT_LANGUAGE, ls );
        ls = new ArrayList<String>();
        ls.add( "publisher" );
        map.put( DublinCore.DublinCoreElement.ELEMENT_PUBLISHER, ls );
        ls = new ArrayList<String>();
        ls.add( "relation" );
        map.put( DublinCore.DublinCoreElement.ELEMENT_RELATION, ls );
        ls = new ArrayList<String>();
        ls.add( "rights" );
        map.put( DublinCore.DublinCoreElement.ELEMENT_RIGHTS, ls );
        ls = new ArrayList<String>();
        ls.add( "source" );
        map.put( DublinCore.DublinCoreElement.ELEMENT_SOURCE, ls );
        ls = new ArrayList<String>();
        ls.add( "subject" );
        map.put( DublinCore.DublinCoreElement.ELEMENT_SUBJECT, ls );
        ls = new ArrayList<String>();
        ls.add( "title" );
        map.put( DublinCore.DublinCoreElement.ELEMENT_TITLE, ls );
        ls = new ArrayList<String>();
        ls.add( "type" );
        ls.add( "second_type" );
        map.put( DublinCore.DublinCoreElement.ELEMENT_TYPE, ls );

        simple_dc = new DublinCore( "90", map );
        assertTrue( new String( simple_dc.getBytes() ).equals( dc_full ) );
    }

    @Test
    public void mutatorsPreservesValues() throws Exception
    {
        simple_dc = new DublinCore( "90" );

        simple_dc.putContributor( "contributor" );
        simple_dc.putCoverage( "coverage" );
        simple_dc.putCreator( "creator" );
        simple_dc.putDate( new Date( setTimeInMillis ) );
        simple_dc.putDescription( "description" );
        simple_dc.putFormat( "format" );
        simple_dc.putLanguage( "language" );
        simple_dc.putPublisher( "publisher" );
        simple_dc.putRelation( "relation" );
        simple_dc.putRights( "rights" );
        simple_dc.putSource( "source" );
        simple_dc.putSubject( "subject" );
        simple_dc.putTitle( "title" );
        simple_dc.putType( "type" );
        simple_dc.putType( "second_type" );

        assertTrue( new String( simple_dc.getBytes() ).equals( dc_full ) );
    }

    @Test( expected=IllegalArgumentException.class)
    public void unparsableDateFailsInConstructor()
    {
        Map<DublinCore.DublinCoreElement, List<String>> map = new EnumMap<DublinCore.DublinCoreElement, List<String>>( DublinCore.DublinCoreElement.class );
        List<String> ls = new ArrayList<String>();
        ls.add( "date" );
        map.put( DublinCore.DublinCoreElement.ELEMENT_DATE, ls );
        simple_dc = new DublinCore( "90", map );
    }



    /**
     * Test of getBytes method, of class DublinCore.
     */
    @Test
    public void testGetBytes() throws Exception
    {
        simple_dc = new DublinCore( "1234567890" );
        byte[] expResult = dc_with_identifier.getBytes();
        byte[] result = simple_dc.getBytes();
        assertTrue( Arrays.equals( expResult, result) );
    }

}