/*
 *  This file is part of RMIObjectRepository.
 *  Copyright © 2009, Steen Manniche.
 * 
 *  RMIObjectRepository is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 * 
 *  RMIObjectRepositoryis distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 * 
 *  You should have received a copy of the GNU General Public License
 *  along with RMIObjectRepository.  If not, see <http://www.gnu.org/licenses/>.
 */
package net.manniche.orep.client;

import java.io.IOException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import net.manniche.orep.documents.DefaultDigitalObject;
import net.manniche.orep.server.rmi.RMIObjectManagement;
import net.manniche.orep.server.rmi.RMIObjectRepository;
import net.manniche.orep.types.DigitalObject;
import net.manniche.orep.types.ObjectIdentifier;


/**
 *
 * @author stm
 */
public class RMIObjectRepositoryClient implements RMIObjectManagementClient
{

    private RMIObjectManagement server;

    protected void connect( String sName )
    {
        try
        {
            UnicastRemoteObject.exportObject( this );
            String serverName = "//" + sName + "/orep";//rmi://localhost/orep
            server = (RMIObjectManagement) Naming.lookup( serverName );
        }
        catch( java.rmi.ConnectException ce )
        {
            System.err.println( "Error: server not started" );
        }
        catch( java.rmi.ConnectIOException cioe )
        {
            System.err.println( "Error: Cannot connect to server at" + sName );
        }
        catch( Exception e )
        {
            e.printStackTrace();
        }
    }


    public static void main( String[] args ) throws IOException
    {

        RMIObjectRepositoryClient client = new RMIObjectRepositoryClient();

        client.connect( "localhost" );

        client.saveObject( "hej".getBytes() );

    }


    @Override
    public void saveObject( byte[] data ) throws RemoteException
    {
        DigitalObject digo = new DefaultDigitalObject( data );
        ObjectIdentifier storeObject = null;
        try
        {
             storeObject = server.storeObject( digo, "saving object" );

        }
        catch( IOException ex )
        {
            System.out.println( String.format( "Bullocks!: %s", ex.getMessage() ) );
        }

        System.out.println( String.format( "Data stored at %s", storeObject.getIdentifierAsURI().getPath() ) );
        System.exit( 0 );
    }


}
