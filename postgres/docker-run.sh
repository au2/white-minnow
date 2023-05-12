#!/bin/bash

export APP_ADMIN_USER=admin
export APP_ADMIN_PASSWORD=admin_pw
export APP_DB=app

docker run --name wm_postgres -p 5432:5432 -e POSTGRES_USER=postgres -e POSTGRES_PASSWORD=postgres -e APP_ADMIN_USER=admin -e APP_ADMIN_PASSWORD=admin_pw -e APP_DB=app -d wm_postgres
