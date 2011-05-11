/*
 *  This file is part of xor. Copyright © 2009-, Steen Manniche.
 *  Distributed under the GPL license, see the copy of the GNU General Public
 *  License along with xor.  If not, see <http://www.gnu.org/licenses/>.
 */

package net.manniche.xor.services.search;

import org.junit.Ignore;
import java.util.List;
import net.manniche.xor.types.DigitalObject;
import net.manniche.xor.types.ObjectIdentifier;
import net.manniche.xor.types.ObjectRepositoryContentType;
import net.manniche.xor.types.RepositoryAction;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Steen Manniche
 */
public class DublinCoreIndexServiceTest {

    public DublinCoreIndexServiceTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception
    {
    }


    @AfterClass
    public static void tearDownClass() throws Exception
    {
    }


    /**
     * Test of notifyMe method, of class DublinCoreIndexService.
     */
    @Ignore @Test
    public void testNotifyMe()
    {
        System.out.println( "notifyMe" );
        ObjectIdentifier identifier = null;
        DigitalObject object = null;
        RepositoryAction action = null;
        ObjectRepositoryContentType contentType = null;
        DublinCoreIndexService instance = new DublinCoreIndexService();
        instance.notifyMe( identifier, object, action, contentType );
        // TODO review the generated test code and remove the default call to fail.
        fail( "The test case is a prototype." );
    }


    /**
     * Test of search method, of class DublinCoreIndexService.
     */
    @Ignore @Test
    public void testSearch()
    {
        System.out.println( "search" );
        Query query = null;
        int maximumResults = 0;
        DublinCoreIndexService instance = new DublinCoreIndexService();
        List expResult = null;
        List result = instance.search( query, maximumResults );
        assertEquals( expResult, result );
        // TODO review the generated test code and remove the default call to fail.
        fail( "The test case is a prototype." );
    }

    @Test
    public void testWithEmptyElementValues()
    {

    }

    @Test
    public void testWithMisspelledDCElementNames()
    {

    }

    @Test
    public void testWithMultipleValuesForOneElement()
    {
        
    }

}