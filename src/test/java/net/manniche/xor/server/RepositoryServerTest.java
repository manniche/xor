/*
 *  This file is part of OREP. Copyright © 2009-, Steen Manniche.
 *  Distributed under the GPL license, see the copy of the GNU General Public
 *  License along with OREP.  If not, see <http://www.gnu.org/licenses/>.
 */

package net.manniche.xor.server;

import net.manniche.xor.types.DigitalObject;
import net.manniche.xor.types.ObjectIdentifier;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author stm
 */
public class RepositoryServerTest {

    public RepositoryServerTest() {
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
    public void setUp() {
    }

    /**
     * Test of storeObject method, of class RepositoryServer.
     */
    @Test
    public void testStoreObject_DigitalObject_String() throws Exception
    {
        System.out.println( "storeObject" );
        DigitalObject data = null;
        String message = "";
        RepositoryServer instance = null;
        ObjectIdentifier expResult = null;
        ObjectIdentifier result = instance.storeObject( data, message );
        assertEquals( expResult, result );
        // TODO review the generated test code and remove the default call to fail.
        fail( "The test case is a prototype." );
    }


    /**
     * Test of storeObject method, of class RepositoryServer.
     */
    @Test
    public void testStoreObject_3args() throws Exception
    {
        System.out.println( "storeObject" );
        DigitalObject data = null;
        ObjectIdentifier identifier = null;
        String message = "";
        RepositoryServer instance = null;
        ObjectIdentifier expResult = null;
        ObjectIdentifier result = instance.storeObject( data, identifier, message );
        assertEquals( expResult, result );
        // TODO review the generated test code and remove the default call to fail.
        fail( "The test case is a prototype." );
    }


    /**
     * Test of getObject method, of class RepositoryServer.
     */
    @Test
    public void testGetObject() throws Exception
    {
        System.out.println( "getObject" );
        ObjectIdentifier identifier = null;
        RepositoryServer instance = null;
        DigitalObject expResult = null;
        DigitalObject result = instance.getObject( identifier );
        assertEquals( expResult, result );
        // TODO review the generated test code and remove the default call to fail.
        fail( "The test case is a prototype." );
    }


    /**
     * Test of deleteObject method, of class RepositoryServer.
     */
    @Test
    public void testDeleteObject() throws Exception
    {
        System.out.println( "deleteObject" );
        ObjectIdentifier identifier = null;
        String logmessage = "";
        RepositoryServer instance = null;
        instance.deleteObject( identifier, logmessage );
        // TODO review the generated test code and remove the default call to fail.
        fail( "The test case is a prototype." );
    }
}