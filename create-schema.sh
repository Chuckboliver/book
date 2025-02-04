#!/bin/sh

MYSQL_USER=bookusr
MYSQL_PASSWORD=/BookAdm4546
MYSQL_DATABASE=bookdb

if ! docker exec -i mysql mysql -u${MYSQL_USER} -p${MYSQL_PASSWORD} ${MYSQL_DATABASE} < schema.sql ; then
  echo "failed to create database schema"
  exit 1
fi

echo "create database schema successful"

if ! docker exec -i mysql mysql -u${MYSQL_USER} -p${MYSQL_PASSWORD} ${MYSQL_DATABASE} < data.sql ; then
  echo "failed to initialize data"
  exit 1
fi

echo "initialize data successful"
