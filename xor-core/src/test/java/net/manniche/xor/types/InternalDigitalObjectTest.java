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
 * @author stm
 */
public class InternalDigitalObjectTest {

    private static final String testInput = "æøåöïüäß";

    @Test
    public void testGetBytes()
    {
        InternalDigitalObject instance = new InternalDigitalObject( testInput.getBytes() );
        byte[] expResult = testInput.getBytes();
        byte[] result = instance.getBytes();
        assertTrue( Arrays.equals( expResult, result ) );
    }
}