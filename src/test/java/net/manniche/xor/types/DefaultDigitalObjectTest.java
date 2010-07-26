/*
 *  This file is part of xor. Copyright © 2009-, Steen Manniche.
 *  Distributed under the GPL license, see the copy of the GNU General Public
 *  License along with xor.  If not, see <http://www.gnu.org/licenses/>.
 */


package net.manniche.xor.types;

import java.util.Arrays;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Steen Manniche
 */
public class DefaultDigitalObjectTest {

    /**
     * Test of getBytes method, of class DefaultDigitalObject.
     */
    @Test
    public void testGetBytes() throws Exception
    {
        byte[] input = "test me: æøåïüöäß".getBytes();
        DigitalObject instance = new DefaultDigitalObject( input, BasicContentType.BINARY_CONTENT );
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
        DefaultDigitalObject instance = new DefaultDigitalObject( "input".getBytes(), BasicContentType.BINARY_CONTENT );
        ObjectRepositoryContentType expResult = BasicContentType.BINARY_CONTENT;
        ObjectRepositoryContentType result = instance.getContentType();
        assertEquals( expResult, result );
    }

}