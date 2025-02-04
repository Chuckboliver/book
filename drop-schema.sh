#!/bin/sh

MYSQL_USER=bookusr
MYSQL_PASSWORD=/BookAdm4546
MYSQL_DATABASE=bookdb

if ! docker exec -i mysql mysql -u${MYSQL_USER} -p${MYSQL_PASSWORD} ${MYSQL_DATABASE} < drop-schema.sql ; then
  echo "failed to drop schema"
fi