#!/bin/sh -e

# Build the project using maven and copy the dependencies into the target folder.
mvn package dependency:copy-dependencies

# Run the user service.
java -cp "target/dependency/*:target/dp-zebedee-services-0.0.1-SNAPSHOT.jar" userservice.UserServiceApi
