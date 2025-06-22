#!/usr/bin/env bash

PGPASSWORD=postgres psql -h postgres.minikube -U postgres -d postgres -c "CREATE USER keycloak WITH PASSWORD 'keycloak';"
PGPASSWORD=postgres psql -h postgres.minikube -U postgres -d postgres -c "CREATE DATABASE keycloak OWNER keycloak;"
