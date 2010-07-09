xor; Object Repository
======================

xor aims at a minimalistic approach to an Object Repository
implementation. An Object Repository is here understood as a
service-based distributable management of objects. It is designed to
handle objects containg either binary or text.


Primary design goals
--------------------

1. It must be simple to use

This means simple interfaces and thereby simple interaction with the
system.

2. It must be modular

There should be no implementation bindings. The initial implementation
uses RMI technology to expose the system as a service, but
implementors should be free to determine whether they would rather use
webservice technology, CORBA or some third form of exposing the
system. Alike with the storage strategy of the system: It could be
file-based, db-backed or some other implementation, freely chosen by
implementors.

3. It must be fast

Implementors should be allowed to cut away all uneccesary operations
and just have the system perform the actions specified in the
interface directly to achieve optimum performance. But the system
should also facilitate the possibility of plugging in layers of
operations to be performed in conjunction with the basic operations.


Technology
----------

xor is implemented primarily in java but is targeting the jvm in
general.


Requirements and dependencies
-----------------------------

java 6 SE

Besides the J6SE, all dependencies are managed through maven

Running the example implementations
-----------------------------------

in the scripts/ directory are python scripts to run the included
examples scripts ending in _server.py should be started before the
corresponding _client.py scripts, as clients will need a server to
communicate with.

Notes on running the RMI example
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

If the rmi_server.py script causes java to generate an error message
containing something like:

java.rmi.ConnectException: Connection refused to host: localhost; nested exception is:
    [...]

A likely reason is that the /etc/hosts file is not giving enough
information on the qualified name of the (localhost) server.

By adding

127.0.0.1               localhost.localdomain   localhost {hostname}

to the top of the /etc/hosts file, where {hostname} is the name
assined to the server. See
http://java.sun.com/j2se/1.4.2/docs/guide/rmi/faq.html#nethostname for
further information on how the JDK allows for passing a hostname to
the JVM.
