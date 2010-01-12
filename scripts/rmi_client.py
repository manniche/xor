#!/usr/bin/env python

import os
import subprocess

rmiclass  = 'net.manniche.orep.client.RMIObjectRepositoryClient'
classes   = '../build/classes'
classpath = '%s:../'%( classes )

rmic = 'rmic'+' -classpath %s'%( classpath )+' -d %s'%( os.path.abspath( classes ) )+' '+rmiclass

rmic_proc = subprocess.Popen( rmic, shell=True, stdout=subprocess.PIPE )

print rmic_proc.communicate()[0]

findjava = [ 'which' ]
findjava.append( 'java' )

java_find = subprocess.Popen( findjava, shell=False, stdout=subprocess.PIPE )

java_cmd = java_find.communicate()[0].strip()
java_cmd = java_cmd+' -cp %s'%( classpath )+' '+rmiclass

java = [ java_cmd ]

java_proc = subprocess.Popen( java, shell=True, stdout=subprocess.PIPE )

out = java_proc.communicate()[0]
print out
