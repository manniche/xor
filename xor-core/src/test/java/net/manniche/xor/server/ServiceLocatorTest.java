/*
 *  This file is part of xor. Copyright © 2009-, Steen Manniche.
 *  Distributed under the GPL license, see the copy of the GNU General Public
 *  License along with xor.  If not, see <http://www.gnu.org/licenses/>.
 */


package net.manniche.xor.server;

import net.manniche.xor.types.ObjectRepositoryService;
import net.manniche.xor.types.ObjectRepositoryServiceType;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test of the ServiceLocator. This test also demonstrates the link between the
 * ObjectRepositoryServiceType and ObjectRepositoryService interfaces.
 *
 * @author Steen Manniche
 */
public class ServiceLocatorTest {

    /**
     * Test of getImplementation method, of class ServiceLocator.
     */
    @Test
    public void testGetImplementation() throws Exception
    {

        ObjectRepositoryServiceType<? extends ObjectRepositoryService> service = new stubServiceType();
        ObjectRepositoryService expResult = new StubService();
        ObjectRepositoryService result = ServiceLocator.getService( service.getClassofService() );
        assertEquals( expResult.getClass(), result.getClass() );
    }

    public static class stubServiceType implements ObjectRepositoryServiceType<ObjectRepositoryService>
    {
        @Override
        public final Class<? extends ObjectRepositoryService> getClassofService()
        {
            return StubService.class;
        }
    }
    public static class StubService implements ObjectRepositoryService{}
}