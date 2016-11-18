#!/bin/bash -e

export VAULT_ADDR='http://127.0.0.1:8200'

vault auth-enable userpass

vault policy-write admin adminPolicy.json
vault policy-write publisher publisherPolicy.json
vault policy-write viewer viewPolicy.json
vault policy-write visualisationPublisher visualisationPublisherPolicy.json

vault write "auth/userpass/users/admin-at-test.com" password=admin policies=admin
