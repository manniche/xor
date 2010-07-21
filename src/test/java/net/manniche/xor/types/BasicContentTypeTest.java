/*
 *  This file is part of xor. Copyright © 2009-, Steen Manniche.
 *  Distributed under the GPL license, see the copy of the GNU General Public
 *  License along with xor.  If not, see <http://www.gnu.org/licenses/>.
 */

package net.manniche.xor.types;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author stm
 */
public class BasicContentTypeTest {

    /**
     * Test of getContentType method, of class BasicContentType.
     */
    @Test
    public void testGetContentType()
    {
        String contentType = "BINARY_CONTENT";
        ObjectRepositoryContentType expResult = BasicContentType.BINARY_CONTENT;
        ObjectRepositoryContentType result = BasicContentType.getContentType( contentType );
        assertEquals( expResult, result );
    }

}