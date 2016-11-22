### Zebedee Refactoring prototype

#### About
This git repo contains a collection of prototype services which are from
the zebedee project.

#### Setup

* Install JDK 8+  
  
Assuming brew is installed on your Mac run;
* ```brew install vault```
* ```brew install maven```

#### Git repos needed
* florence branch : refactoring-work
* zebedee branch : refactoring-work
* sixteen branch : develop
 
#### Running the prototype
##### Vault
* Start vault in development mode ```vault server --dev```
* Run the script in scripts/vault-setup.sh

Take note of the root token as it is needed (server prints this), eg
```
Root Token: d949ec0e-189b-c244-393d-4d396424ac8c
```

#### zebedee + flourence
* Run flourence using the bash script
* Set up a vault token using ```export VAULT_TOKEN="Root token from vault server"``` then run zebedee using the
bash script
 
 
#### user service
* ```export VAULT_TOKEN="Root token from vault server"```
* Run ```run-user-service.sh```

#### team service
* ```export VAULT_TOKEN="Root token from vault server"```
* Run ```run-team-service.sh```

#### encryption service
* ```export VAULT_TOKEN="Root token from vault server"```
* Run ```run-encryption-service.sh```

### Notes
* This breaks some features in zebedee
* User names are only kept in memory
* Some endpoints are mocked
* Ideally the services should not be using the root token
* Vault is in development, so tokens are passed using HTTP not HTTPS!
