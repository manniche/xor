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
 * @author stm
 */
public class ServiceLocatorTest {

    /**
     * Test of getImplementation method, of class ServiceLocator.
     */
    @Test
    public void testGetImplementation()
    {
        System.out.println( "getImplementation" );
        ObjectRepositoryServiceType<? extends ObjectRepositoryService> service = new stubServiceType();
        Class<ObjectRepositoryService> expResult = (Class<ObjectRepositoryService>) (Object) stubService.class;
        Class<ObjectRepositoryService> result = ServiceLocator.getImplementation( service );
        assertEquals( expResult, result );
    }

    public static class stubServiceType implements ObjectRepositoryServiceType<ObjectRepositoryService>
    {
        @Override
        @SuppressWarnings( "unchecked" )
        public Class<ObjectRepositoryService> getClassofService()
        {
            return (Class<ObjectRepositoryService>) (Object) stubService.class;
        }
    }
    public static class stubService implements ObjectRepositoryService{}
}