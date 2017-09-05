[![License](https://img.shields.io/badge/License-EPL%201.0-red.svg)](https://opensource.org/licenses/EPL-1.0)
[![Build Status](https://travis-ci.org/dellemc-symphony/rcm-capabilities-api.svg?branch=master)](https://travis-ci.org/dellemc-symphony/rcm-capabilities-api)
[![Slack](http://community.codedellemc.com/badge.svg)](https://codecommunity.slack.com/messages/symphony)
[![Codecov](https://img.shields.io/codecov/c/github/dellemc-symphony/rcm-capabilities-api.svg)](https://codecov.io/gh/dellemc-symphony/rcm-capabilities-api)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.dell.cpsd/rcm-capabilities-api/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.dell.cpsd/rcm-capabilities-api)
[![Semver](http://img.shields.io/SemVer/2.0.0.png)](http://semver.org/spec/v2.0.0.html)
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

Then run the following command:-
mvn clean compile

## Testing
Unit tests are run as part of the mvn packaging
Project is auto built in Jenkins with automation tests

## Contributing
Project Symphony is a collection of services and libraries housed at [GitHub][github].
 
Contribute code and make submissions at the relevant GitHub repository level. See [our documentation][contributing] for details on how to contribute.
## Community
Reach out to us on the Slack [#symphony][slack] channel by requesting an invite at [{code}Community][codecommunity].
 
You can also join [Google Groups][googlegroups] and start a discussion.
 
[slack]: https://codecommunity.slack.com/messages/symphony
[googlegroups]: https://groups.google.com/forum/#!forum/dellemc-symphony
[codecommunity]: http://community.codedellemc.com/
[contributing]: http://dellemc-symphony.readthedocs.io/en/latest/contributingtosymphony.html
[github]: https://github.com/dellemc-symphony
[documentation]: https://dellemc-symphony.readthedocs.io/en/latest/
