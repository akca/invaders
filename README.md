# Invaders [![Build Status](https://travis-ci.com/akca/invaders.svg?token=BNTWySpsD8qH5qJ1wqVg&branch=master)](https://travis-ci.com/akca/invaders)

A basic multiplayer space invaders game built with JavaFX and Spring.

## Building
- Building requires JDK 11.
- cd into the root dir and run `mvn install -DskipTests`. This will build client, server and common projects.

## Running

- Running requires JDK 11.
- JAR's are put under [/dist](dist) directory for convenience.
- cd into the [/dist](dist) dir.
- Start server by running `java -jar invaders-server-VERSION.jar`
- Give execute permission to invaders-client.sh by running `chmod u+x invaders-client.sh`
- Update server URL in the invaders-client.sh if server is running in another host.
- Run client by executing `./invaders-client.sh`

## License

This project is licensed under the MIT License - see the [LICENSE.md](LICENSE.md) file for details.
