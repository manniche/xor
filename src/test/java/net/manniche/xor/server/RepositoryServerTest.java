/*
 *  This file is part of xor. Copyright © 2009-, Steen Manniche.
 *  Distributed under the GPL license, see the copy of the GNU General Public
 *  License along with xor.  If not, see <http://www.gnu.org/licenses/>.
 */

package net.manniche.xor.server;

import net.manniche.xor.exceptions.RepositoryServiceException;
import net.manniche.xor.types.BasicContentType;
import java.io.IOException;
import mockit.NonStrictExpectations;
import net.manniche.xor.storage.StorageProvider;
import java.net.URI;
import net.manniche.xor.types.DefaultIdentifier;
import net.manniche.xor.types.DigitalObject;
import net.manniche.xor.types.ObjectIdentifier;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import mockit.Mocked;
import net.manniche.xor.exceptions.StorageProviderException;
import net.manniche.xor.types.DefaultDigitalObject;
import static org.junit.Assert.*;

/**
 *
 * @author Steen Manniche
 */
public class RepositoryServerTest {

    @Mocked StorageProvider mockStorage;

    static byte[] data = "åøæïüßäö".getBytes();
    static byte[] contentType = BasicContentType.BINARY_CONTENT.toString().getBytes();
    static String logMessage = "test log message";
    static URI testURI;
    static URI testContentURI;
    static ObjectIdentifier predefinedId;
    RepositoryServer instance;
    
    public RepositoryServerTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception
    {
        testURI = new URI( "http://localhost/test" );
        testContentURI = new URI( "http://localhost/contenttypes/test" );
        predefinedId = new DefaultIdentifier( testURI );
    }


    @AfterClass
    public static void tearDownClass() throws Exception
    {
    }

    @Before
    public void setUp() {
        instance = new MockRepositoryImpl( mockStorage );
    }


    @Test
    public void storeObjectReturnsValidURI() throws Exception
    {

        new NonStrictExpectations(){{
            mockStorage.save( (byte[]) any );returns( testURI );
        }};

        ObjectIdentifier result = instance.storeObject( data, logMessage );
        assertEquals( predefinedId.getURI(), result.getURI() );
    }


    @Test
    public void storeObjectReturnsPredefinedURI() throws Exception
    {
        ObjectIdentifier result = instance.storeObject( data, predefinedId, logMessage);
        assertEquals( predefinedId.getURI(), result.getURI() );
    }


    @Test
    public void simpleGetObjectPasses() throws Exception
    {
        final DigitalObject expDigObj = new DefaultDigitalObject( data, BasicContentType.BINARY_CONTENT );

        new NonStrictExpectations(){{
           mockStorage.get( testURI );returns( data );
           mockStorage.get( testContentURI );returns( contentType );
        }};

        DigitalObject result = instance.getObject( predefinedId );
        assertEquals( expDigObj.getBytes(), result.getBytes() );
    }


    @Test
    public void naïveDeleteObjectPasses() throws Exception
    {
        instance.deleteObject( predefinedId, logMessage );
    }

    @Test( expected=RepositoryServiceException.class)
    public void deleteNonExistingObjectFails() throws Exception
    {
        RepositoryServer throwingServer = new MockRepositoryImpl( new ThrowingStorageProviderStub() );
        new NonStrictExpectations(){{
                mockStorage.delete( testURI );
        }};
        throwingServer.deleteObject( predefinedId, logMessage );
    }


    ////////////////////////////////////////////////////////////////////////////
    //////////// Below follows mocks and stubs for the test suite //////////////

    public static class MockRepositoryImpl extends RepositoryServer
    {
        MockRepositoryImpl( StorageProvider storage )
        {
            super( storage );
        }
    }

    public static class ThrowingStorageProviderStub implements StorageProvider
    {
        @Override
        public URI save( byte[] object ) throws StorageProviderException
        { throw new StorageProviderException(); }


        @Override
        public void save( byte[] object, URI url ) throws StorageProviderException
        { throw new StorageProviderException(); }


        @Override
        public byte[] get( URI identifier ) throws StorageProviderException
        { throw new StorageProviderException(); }


        @Override
        public void delete( URI identifier ) throws StorageProviderException
        { throw new StorageProviderException(); }

        @Override
        public void close()
        {}
    }
}