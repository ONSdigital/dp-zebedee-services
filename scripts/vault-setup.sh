#!/bin/bash -e

export VAULT_ADDR='http://127.0.0.1:8200'

vault auth-enable userpass
vault mount transit

vault policy-write admin adminPolicy.hcl
vault policy-write publisher publisherPolicy.hcl
vault policy-write viewer viewPolicy.hcl
vault policy-write visualisationPublisher visualisationPublisherPolicy.hcl

vault policy-write team.economy team.economy.hcl
vault policy-write team.methodolgy team.methodolgy.hcl

vault write "auth/userpass/users/admin-at-test.com" password=admin policies=admin,team.methodolgy
