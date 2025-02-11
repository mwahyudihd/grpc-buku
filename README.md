# grpc-buku

This project uses Quarkus, the Supersonic Subatomic Java Framework.

If you want to learn more about Quarkus, please visit its website: https://quarkus.io/ .

## Related Guides

- Hibernate ORM with Panache ([guide](https://quarkus.io/guides/hibernate-orm-panache)): Simplify your persistence code for Hibernate ORM via the active record or the repository pattern
- JDBC Driver - PostgreSQL ([guide](https://quarkus.io/guides/datasource)): Connect to the PostgreSQL database via JDBC
- Kotlin ([guide](https://quarkus.io/guides/kotlin)): Write your services in Kotlin

## Provided Code

### Hibernate ORM

Create your first JPA entity

[Related guide section...](https://quarkus.io/guides/hibernate-orm)

[Related Hibernate with Panache section...](https://quarkus.io/guides/hibernate-orm-panache)


### gRPC Server

This project implements a gRPC server using Quarkus for Backend Buku with CRUD operations and proxy functionality for clients.

#### Features

- CRUD operations for books
- gRPC server implementation
- Client proxy support
- Database integration with PostgreSQL
- Hibernate ORM with Panache

#### gRPC Service Definition

The service is defined in `src/main/proto/buku.proto`. The service includes:
- CreateBook
- ReadBook
- UpdateBook
- DeleteBook
- ListBooks

#### API Documentation

##### Book Service
```protobuf
service BookService {
    rpc CreateBook (Book) returns (BookResponse);
    rpc GetBook (BookId) returns (Book);
    rpc UpdateBook (Book) returns (BookResponse);
    rpc DeleteBook (BookId) returns (BookResponse);
    rpc ListBooks (Empty) returns (BookList);
}
```

For detailed implementation, check the source code in `src/main/kotlin/service/`.

## Running Project

### Development Mode

Run application in development mode:
```shell script
./mvnw compile quarkus:dev
```
or
```shell script
mvn compile quarkus:dev
```

The application will be available at:
- gRPC: `localhost:50051`
- Dev UI: `http://localhost:8080/q/dev/`
- Proxy Rest: `http://localhost:8080/grpc-proxy/${service}/${method}`

### Production Mode

1. Build the application:
```shell script
./mvnw package
```
or
```shell script
mvn package
```

2. Run with Java:
```shell script
java -jar target/quarkus-app/quarkus-run.jar
```

Or build and run as native executable:
```shell script
./mvnw package -Dnative
./target/grpc-buku-1.0.0-SNAPSHOT-runner
```

The application will be available at:
- gRPC: `localhost:50051`
## Requirements

### Prerequisites
- Java Development Kit (JDK) 21.0.6 or later
- Maven 3.9.0 or later
- PostgreSQL 15.0 or later
- GraalVM (optional, for native builds)

### Database Setup
1. Install PostgreSQL
2. Create a database named `buku`
3. Configure database connection in `application.properties`:
```properties
quarkus.datasource.db-kind=postgresql
quarkus.datasource.username=your_username
quarkus.datasource.password=your_password
quarkus.datasource.jdbc.url=jdbc:postgresql://localhost:5432/buku
```
