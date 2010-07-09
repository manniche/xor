The core of xor is the abstract class RepositoryServer. This class
defines the basic operations for alle implementations of the object
repository. The server implementations exposes the object repository
to clients and additionally defines the semantics of the object
repository.

A server implementation defines a number of content types that it
recognizes and each (core) operation performed on the server
implementation will be subject for a notification to observers
registered with the server implementation.

A server implementation additionally registeres a number of
ObjectRepositoryServices (ORS) as Observers to the server
implementation. The ORS handles data that passes through the object
repository wrt. to the content type and repository action.