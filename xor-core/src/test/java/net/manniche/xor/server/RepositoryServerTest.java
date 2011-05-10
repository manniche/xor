/*
 *  This file is part of xor. Copyright © 2009-, Steen Manniche.
 *  Distributed under the GPL license, see the copy of the GNU General Public
 *  License along with xor.  If not, see <http://www.gnu.org/licenses/>.
 */

package net.manniche.xor.server;

import java.io.IOException;
import mockit.NonStrictExpectations;
import net.manniche.xor.storage.StorageProvider;
import java.net.URI;
import net.manniche.xor.types.DefaultIdentifier;
import net.manniche.xor.types.DigitalObject;
import net.manniche.xor.types.ObjectIdentifier;
import net.manniche.xor.types.ObjectRepositoryContentType;
import net.manniche.xor.types.RepositoryAction;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import mockit.Mocked;
import static org.junit.Assert.*;

/**
 *
 */
public class RepositoryServerTest {

    @Mocked StorageProvider mockStorage;

    static byte[] data = "åøæïüßäö".getBytes();
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
            mockStorage.save( (byte[]) any, anyString );returns( testURI );
        }};

        ObjectIdentifier result = instance.storeObject( data, ".", logMessage );
        assertEquals( predefinedId.getURI(), result.getURI() );
    }


    @Test
    public void storeObjectReturnsPredefinedURI() throws Exception
    {
        ObjectIdentifier result = instance.storeObject( data, ".", predefinedId, logMessage);
        assertEquals( predefinedId.getURI(), result.getURI() );
    }


    @Test
    public void naïveDeleteObjectPasses() throws Exception
    {
        instance.deleteObject( predefinedId, logMessage );
    }


    @Test( expected=IOException.class)
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

        @Override
        protected void addObserver( RepositoryObserver observer )
        {
            throw new UnsupportedOperationException( "Not supported yet." );
        }


        @Override
        protected void removeObserver( RepositoryObserver observer )
        {
            throw new UnsupportedOperationException( "Not supported yet." );
        }


        @Override
        protected void notifyObservers( ObjectIdentifier identifier, DigitalObject object, RepositoryAction action, ObjectRepositoryContentType contentType )
        {
            throw new UnsupportedOperationException( "Not supported yet." );
        }

    }

    public static class ThrowingStorageProviderStub implements StorageProvider
    {
        @Override
        public URI save( byte[] object, String storagePath ) throws IOException
        { throw new IOException(); }


        @Override
        public void save( byte[] object, URI url, String storagePath ) throws IOException
        { throw new IOException(); }


        @Override
        public byte[] get( URI identifier ) throws IOException
        { throw new IOException(); }


        @Override
        public void delete( URI identifier ) throws IOException
        { throw new IOException(); }

        @Override
        public void close()
        {}
    }
}