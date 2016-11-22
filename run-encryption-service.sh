#!/bin/sh -e

# Build the project using maven and copy the dependencies into the target folder.
mvn package dependency:copy-dependencies


# Set the default web container to run on set port
export SERVER_PORT=8100

# Run the encryption service.
java -cp "target/dependency/*:target/dp-zebedee-services-0.0.1-SNAPSHOT.jar" encryptionservice.EncryptionApi
