# Book Service

## Prerequisites

- JDK 1.8
- Docker

## Setup MySQL Database via docker-compose

docker-compose.yml file contained mysql service that your Spring Boot Application
can access

```yaml
services:
  mysql:
    container_name: mysql
    image: mysql:9.2.0
    restart: always
    ports:
      - "3306:3306"
    environment:
      MYSQL_ROOT_PASSWORD: /rootPwd
      MYSQL_DATABASE: bookdb
      MYSQL_USER: bookusr
      MYSQL_PASSWORD: /BookAdm4546
    volumes:
      - type: volume
        source: mysql-data
        target: /var/lib/mysql

volumes:
  mysql-data:
```

1. Start mysql service using docker-compose

```shell
docker compose up -d
```

2. Create database schema and initial data with `create-schema.sh`

```shell
./create-schema.sh
```

`create-schema.sh` will use DDL from `schema.sql` file and data from `data.sql`

3. If you want to drop the schema you can use `drop-schema.sh` that will use script located in `drop-schema.sql`

```shell
./drop-schema.sh
```

## Running the server

via Maven

```shell
./mvnw spring-boot:run
```

## Test

Unit Test

```shell
./mvnw test
```

Integration Test

```shell
./mvnw failsafe:integration-test
```

## Example API Request/Response

### Find all books by author

GET /books?author={author}

Request Parameters

- author = Name of the book's author

Request

```http request
GET http://localhost:8080/books?author=Colleen Hoover
```

Response (200 OK)

```json
[
  {
    "id": 14,
    "title": "It Ends With Us",
    "author": "Colleen Hoover",
    "publishedDate": "2559-08-02"
  },
  {
    "id": 15,
    "title": "Verity",
    "author": "Colleen Hoover",
    "publishedDate": "2561-12-07"
  }
]
```

### Add book

POST /books

Request Body

- title = Book's title
- author = Book's author
- publishedDate = Book's published date

Request

```http request
POST http://localhost:8080/books
Content-Type: application/json

{
  "title": "Lord of the ring",
  "author": "Pat",
  "publishedDate": "2564-06-07"
}
```

Response (201 Created)

```json
{
  "id": 65,
  "title": "Lord of the ring",
  "author": "Pat",
  "publishedDate": "2564-06-07"
}
```

### Validation failed

Request

```http request
POST http://localhost:8080/books
Content-Type: application/json

{
  "title": "",
  "author": "Pat",
  "publishedDate": "2564-06-07"
}
```

Response (400 Bad Request)

```json
{
  "fieldErrors": [
    {
      "field": "title",
      "rejectedValue": "",
      "message": "title must not be empty"
    }
  ]
}
```