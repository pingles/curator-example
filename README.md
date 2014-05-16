# Curator Example

Sample application for the [Curator](https://github.com/pingles/curator) library. Uses Apache Curator to build an application that behaves like a good citizen in a distributed world.

## Usage

The application requires a running ZooKeeper instance, it will connect to it and hold a leadership election amongst the connected nodes. Once a leader is elected it will start a dummy task that only the leader needs to run. Start multiple instances to see the election occur amongst the different processes.

    $ lein run

## License

Copyright © 2014 Paul Ingles

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
