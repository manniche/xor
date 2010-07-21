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