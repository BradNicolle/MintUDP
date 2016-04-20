# MintUDP
A lightweight, event-driven, object-oriented Java UDP library.

Clients register with a central server, upon which they can then send UDP messages to each other (routed via the server). The library is object-oriented in that the messages themselves can be any object that implements the inbuilt `Marshallable` interface, and event-driven in that listeners can be registered to be fired whenever a message of a specified type is received.

**Please note this is an active work in progress, and is not yet ready for a release!**

## Usage
A UDP server can be started by specifying the port to listen on:
```java
UDPServer server = new UDPServer(4445);
server.start();
```

A UDP client can be created by specifying the remote MintUDP server address and port:
```java
UDPClient client = new UDPClient("localhost", 4445);
```

To receive messages, register a listener on this client. Using the inbuilt `StringMessage` message type:
```java
client.registerListener(StringMessage.class,
    (message) -> System.out.println("Client received message: " + ((StringMessage) message).getMessage()));
```
The listener (in this case a lambda) will be called whenever a message of the specified type is received.

The client then registers with the server by specifying its name:
```java
client.sendConnectMessage("Client1");
```
Note that the server's registry clears automatically every couple of seconds. To continue to be registered, this method will need to be called repeatedly.

Finally, to send a message to another client, you specify its registered name and the message itself:
```java
client.sendMessage("Client2", new StringMessage("Hello Client2!!"));
```
