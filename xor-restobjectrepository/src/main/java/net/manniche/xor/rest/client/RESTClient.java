package net.manniche.xor.rest.client;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import net.manniche.xor.types.BasicContentType;
import net.manniche.xor.types.RESTDigitalObject;
import net.manniche.xor.types.RESTObjectRepositoryContentType;
import org.apache.cxf.helpers.IOUtils;
import org.apache.cxf.io.CachedOutputStream;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.PutMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.methods.StringRequestEntity;

public class RESTClient
{
    public static void main( String[] args ) throws MalformedURLException, IOException
    {
        RESTClient client = new RESTClient();
        client.putData();
    }

   private static String getStringFromInputStream( InputStream in ) throws IOException 
   {
       CachedOutputStream bos = new CachedOutputStream();
       IOUtils.copy( in, bos );
       in.close();
       bos.close();
       return bos.getOut().toString();
   }
   
   private void putData() throws UnsupportedEncodingException, IOException
   {
       String  data = String.format( "<RESTDigitalObject><bytes>%s</bytes></RESTDigitalObject>", "æøåßüöï".getBytes() );
       RESTDigitalObject digo = new RESTDigitalObject();
       digo.setBytes( data.getBytes() );
       digo.setContentType( RESTObjectRepositoryContentType.DUBLIN_CORE );
       PutMethod put = new PutMethod( "http://localhost:9000/xor/object/" );
       RequestEntity entity = new StringRequestEntity( data, "text/xml", "UTF-8" );
       put.setRequestEntity( entity );
       HttpClient httpclient = new HttpClient();
       
       try {
           int result = httpclient.executeMethod( put );
           System.out.println("Response status code: " + result);
           System.out.println("Response body: ");
           System.out.println( put.getResponseBodyAsString() );
       } finally {
           put.releaseConnection();
       }
       
   }

}
