# grpc-buku

This project uses Quarkus, the Supersonic Subatomic Java Framework.

If you want to learn more about Quarkus, please visit its website: https://quarkus.io/ .

## Running the application in dev mode

You can run your application in dev mode that enables live coding using:
```shell script
./mvnw compile quarkus:dev
```

> **_NOTE:_**  Quarkus now ships with a Dev UI, which is available in dev mode only at http://localhost:8080/q/dev/.

## Packaging and running the application

The application can be packaged using:
```shell script
./mvnw package
```
It produces the `quarkus-run.jar` file in the `target/quarkus-app/` directory.
Be aware that it’s not an _über-jar_ as the dependencies are copied into the `target/quarkus-app/lib/` directory.

The application is now runnable using `java -jar target/quarkus-app/quarkus-run.jar`.

If you want to build an _über-jar_, execute the following command:
```shell script
./mvnw package -Dquarkus.package.type=uber-jar
```

The application, packaged as an _über-jar_, is now runnable using `java -jar target/*-runner.jar`.

## Creating a native executable

You can create a native executable using: 
```shell script
./mvnw package -Dnative
```

Or, if you don't have GraalVM installed, you can run the native executable build in a container using: 
```shell script
./mvnw package -Dnative -Dquarkus.native.container-build=true
```

You can then execute your native executable with: `./target/grpc-buku-1.0.0-SNAPSHOT-runner`

If you want to learn more about building native executables, please consult https://quarkus.io/guides/maven-tooling.

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

