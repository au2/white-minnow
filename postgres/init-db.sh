#!/bin/bash
set -e

psql -v ON_ERROR_STOP=1 -U "$POSTGRES_USER" -d postgres -v flyuser="$APP_ADMIN_USER" -v flypass="'$APP_ADMIN_PASSWORD'" -v flydb="$APP_DB" <<-EOSQL
	CREATE ROLE :flyuser PASSWORD :flypass LOGIN CREATEROLE;
	CREATE DATABASE :flydb;
	GRANT ALL PRIVILEGES ON DATABASE :flydb TO :flyuser;
EOSQL
