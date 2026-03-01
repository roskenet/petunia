kubectl port-forward svc/postgres 6543:5432

PGPASSWORD=postgres psql -h localhost -p 6543 -U postgres -d postgres -c "CREATE USER keycloak WITH PASSWORD 'keycloak';"  
PGPASSWORD=postgres psql -h localhost -p 6543 -U postgres -d postgres -c "CREATE DATABASE keycloak OWNER keycloak;"