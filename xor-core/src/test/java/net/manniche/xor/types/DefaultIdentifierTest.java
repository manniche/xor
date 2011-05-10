/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.manniche.xor.types;

import org.junit.Before;
import java.net.URI;
import java.net.URISyntaxException;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author stm
 */
public class DefaultIdentifierTest {
    private DefaultIdentifier instance;

    @Before
    public void setUp() throws URISyntaxException
    {
        instance = new DefaultIdentifier( new URI( "http://localhost/path" ) );
    }

    /**
     * Test of getURI method, of class DefaultIdentifier.
     */
    @Test
    public void testGetURI() throws Exception
    {
        instance = new DefaultIdentifier( new URI( "http://localhost/path" ) );
        URI expResult = new URI( "http://localhost/path" );
        URI result = instance.getURI();
        assertEquals( expResult, result );
    }


    /**
     * Test of getName method, of class DefaultIdentifier.
     */
    @Test
    public void testGetName() throws Exception
    {
        instance = new DefaultIdentifier( new URI( "http://localhost/path" ) );
        String expResult = "path";
        String result = instance.getName();
        assertEquals( expResult, result );
    }


    /**
     * Test of getId method, of class DefaultIdentifier.
     */
    @Test
    public void testGetId() throws Exception
    {
        URI uri = new URI( "http://localhost/path" );
        instance = new DefaultIdentifier( uri );

        String result = instance.getId(); // == "/path"
        assertEquals( uri.getPath(), result );
    }

}