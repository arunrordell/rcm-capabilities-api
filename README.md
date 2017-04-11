[![License](https://img.shields.io/badge/License-EPL%201.0-red.svg)](https://opensource.org/licenses/EPL-1.0)
# rcm-capabilities-api
## Description
The rcm-capabilities-api is used for providing all entry and response messages for rcm capabilities
examples include:
Upgrading of firmware for Dell iDRAC.
Upgrading of firmware for Dell r730 server.

## Documentation

## API overview
See resources folder of project for json messages, these are built automatically via Maven into Java source.

## Before you begin
The system should have Java Runtime Environment installed.
The system should have RabbitMQ installed.

## Building
For compilation Java and Maven should be installed.
Preferred Java version oracle: "1.8.0_121".
Preferred Maven version: Apache Maven 3.3.9 or greater·
http://repo.vmo.lab:8080/artifactory/settings.xml of maven which is in .m2 directory should contain path to artifactory.

Then run the following command:-
mvn clean compile

## Packaging
For packaging Java and Maven should be installed.
The system should be able to ping the following URL i.e. generally should be inside the EMC network.
http://repo.vmo.lab:8080/artifactory/
settings.xml of maven which is in .m2 directory should contain path to artifactory.

Then run the following command:-
mvn clean install

## Deploying
The output after packaging is a jar.
It should not be run in standalone.
It is used generally with services which need to consume this api.

Command Usage:-
No main Program currently this project is in use in an internal DellEMC project based on RackHD.

## Testing
Unit tests are run as part of the mvn packaging
Project is auto built in Jenkins with automation tests

## Contributing
## Community
## Licensing
