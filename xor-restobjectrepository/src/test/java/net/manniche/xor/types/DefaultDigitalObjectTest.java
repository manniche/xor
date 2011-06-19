/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.manniche.xor.types;

import java.util.Arrays;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 */
public class DefaultDigitalObjectTest {

    /**
     * Test of getBytes method, of class DefaultDigitalObject.
     */
    @Test
    public void testGetBytes() throws Exception
    {
        byte[] input = "test me: æøåïüöäß".getBytes();
        RESTDigitalObject instance = new RESTDigitalObject();
        instance.setBytes( input );
        instance.setContentType( BasicContentType.BINARY_CONTENT );
        byte[] expResult = "test me: æøåïüöäß".getBytes();
        byte[] result = instance.getBytes();
        assertTrue( Arrays.equals( expResult, result ) );
    }


    /**
     * Test of getContentType method, of class DefaultDigitalObject.
     */
    @Test
    public void testGetContentType()
    {
        RESTDigitalObject instance = new RESTDigitalObject();
        instance.setBytes( "input".getBytes() );
        instance.setContentType( BasicContentType.BINARY_CONTENT );
        ObjectRepositoryContentType expResult = BasicContentType.BINARY_CONTENT;
        ObjectRepositoryContentType result = instance.getContentType();
        assertEquals( expResult, result );
    }

}