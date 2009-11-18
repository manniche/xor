ORep Object Repository
======================

ORep aims at a minimalistic approach to an Object Repository
implementation. An Object Repository is here understood as a
service-based distributable management of objects. It is designed to
handle objects containg either binary or text.


Primary design goals
--------------------

# It must be simple to use #

This means simple interfaces and thereby simple interaction with the
system.

# It must be modular #

There should be no implementation bindings. The initial implementation
uses RMI technology to expose the system as a service, but
implementors should be free to determine whether they would rather use
webservice technology, CORBA or some third form of exposing the
system. Alike with the storage strategy of the system: It could be
file-based, db-backed or some other implementation, freely chosen by
implementors.

# It must be fast #

Implementors should be allowed to cut away all uneccesary operations
and just have the system perform the actions specified in the
interface directly to achieve optimum performance. But the system
should also facilitate the possibility of plugging in layers of
operations to be performed in conjunction with the basic operations.


Technology
----------

ORep is implemented primarily in java but is targeting the jvm in
general.


Requirements and dependencies
-----------------------------

java 6
