FROM postgres:15.2-alpine

# initialize the db with a non-super admin user
COPY init-db.sh /docker-entrypoint-initdb.d/init-db.sh
RUN chmod 777 /docker-entrypoint-initdb.d/init-db.sh

