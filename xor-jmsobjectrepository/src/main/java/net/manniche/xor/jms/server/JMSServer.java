package net.manniche.xor.jms.server;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.activemq.broker.BrokerFactory;
import org.apache.activemq.broker.BrokerService;
import org.apache.activemq.broker.TransportConnector;

/**
 *
 */
public final class JMSServer {
    
    private final static Logger Log = Logger.getLogger( JMSServer.class.getName() );
    private final String url;
    private final int port;
    private BrokerService broker;
    
    public JMSServer( String url, int port )
    {
        this.url = url;
        this.port = port;
    }
    
    private void start() throws Exception
    {
        String serverUrl = String.format( "%s://%s:%s", "tcp", this.url, this.port );
        this.broker = BrokerFactory.createBroker( serverUrl );
        broker.start();
    }
    
    private void stop() throws Exception
    {
        this.broker.stop();
    }
    
    
    public static void main(String[] args) throws Exception {
        final JMSServer server = new JMSServer( "localhost", 61616 );
        Log.info( "Starting server" );
        server.start();
        Log.info( "Server started" );
        
        Runtime.getRuntime().addShutdownHook(
                new Thread(){
                    @Override
                    public void run()
                    {
                try {
                    server.stop();
                } catch (Exception ex) {
                    Log.severe( String.format( "While trying to close broker serviceL:", ex.getMessage() ) );
                            
                }
                    }
                }
                );
        
    }
}
