#!/usr/bin/env python

import os
import subprocess
import sys

classes   = '../../target/classes'
rmiclass  = 'net.manniche.orep.server.rmi.RMIServer'
rmibase   = 'java.rmi.server.codebase=file:///%s'%( os.path.abspath( classes )+'/' )

checkpath = os.path.join( classes, str( "/".join( rmiclass.split( '.' ) ) + '.class' ) )

if not os.path.exists( checkpath ):
    sys.exit( "could not find %s. Have you compiled?"%( checkpath ) )

#first check that the rmiregistry is running
res = subprocess.Popen( [ 'ps a | grep rmiregistry' ], shell=True, stdout=subprocess.PIPE )

res_list = [ process.partition( 'pts' )[0].strip() for process in res.communicate()[0].split( '\n' ) if process.find( 'grep rmiregistry' ) < 0 ]

res_list = [ i for i in res_list if len(i)>0 ]

if len( res_list ) > 1:
    sys.exit( 'cannot automatically determine whether rmiregistry is running. Could you please find out for me?' )
elif len( res_list ) < 1:
    pass
    # subprocess.Popen( [ 'rmiregistry' ], shell=True, stdout=subprocess.PIPE )
    #    sys.exit( 'rmi registry is not running. please start the rmi registry before starting the rmi server' )

java_cmd = 'java -cp %s'%( classes )+' -D'+rmibase+' '+rmiclass

java = [ java_cmd ]

java_proc = subprocess.Popen( java, shell=True, stdout=subprocess.PIPE )

out = java_proc.communicate()[0]
print out

