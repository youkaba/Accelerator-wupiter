#Overview
This is a simple micro-service for creating and managing Todos using
[Java](https://www.java.com/), [Spring Boot](https://spring.io/projects/spring-boot) and [Docker](https://www.docker.com/).

# Database

## Start DB in docker container
Make sure to have `docker` and `docker-compose` installed first.
Run below commands:
```
cd <PROJECT_HOME>/_devops/docker
docker-compose up
```

The database connection details are found in `<PROJECT_HOME>/src/main/resources/application.yml`.

## Database versioning
This project uses [Liquibase](https://www.liquibase.org/) to version the database changes, see here for the liquibase change-sets config file: `<PROJECT_HOME>/src/main/resources/db.changelog/changelog-master.xml`.

## Authentication
The app is using [Keycloak](https://www.keycloak.org/), an open-source Identity and Access Management service for JWT based authentication.
Running the `docker-compose up` command as described above will also run Keycloak in a docker container (called `keycloak`).

You can access the admin console at http://localhost:8403/auth.

The docker container is pre-configured with the admin user with `admin/admin` credentials, and it also has a `demo_realm` realm created with a `users` group and a `user01/pwd123`
user. The realm is imported from the `_devops/docker/keycloak/demo_realm.json` config file.

For further docker configuration for Keycloak, visit https://www.keycloak.org/getting-started/getting-started-docker.

The realm has a client id `demo-app` configured with a user `user01/pwd123`.

The app uses Spring Security and Spring Security OAuth2 Resource Server libraries for authentication / authorization. See the `SecurityConfig` and `MethodSecurityConfig` classes and `application.yml` file (config properties under `spring.security` and `app.jwt`) for more details on the configuration.

Finally, the `TodoController` class is configured to require 'user' role for all endpoints declared within the controller and `TodoControllerTest` class is updated to support role-based authorization. 

## Spring Data JPA
The application communicates via the SQL database using [Spring Data JPA](https://spring.io/projects/spring-data-jpa).

Use the `JPA Buddy` IntelliJ plugin to generate liquibase change-sets from JPA entities

# Kafka integration with Spring Cloud Stream
[Spring Cloud Stream](https://spring.io/projects/spring-cloud-stream) is a Spring framework for building event-driven microservices. Spring Cloud Stream has a [Kafka binder](https://github.com/spring-cloud/spring-cloud-stream-binder-kafka) that we're using in this app.

It uses the `spring-cloud-starter-stream-kafka` dependency, and it's configured using the `StreamingConfig` class and the `spring.cloud.stream.xyz` config properties in the `application.yml` file.

Start local Zookeeper (needed by [Kafka](https://kafka.apache.org/)) using below command:
```
cd <PROJECT_HOME>/_devops/docker
docker-compose up zookeeper
```

Start local Kafka using:
```
cd <PROJECT_HOME>/_devops/docker
docker-compose up kafka
```

Sample code to send/receive messages using Kafka is at `TodoStreamingService` and `TodoController#send()`.

# Lombok
The project uses [Lombok](https://projectlombok.org/) that auto-generates bytecode for getters, setters, logger, etc. 

To use Lombok in IntelliJ install the `Lombok` plugin and enable Settings -> Enable annotation processing. 

See below links for more details:
= https://www.baeldung.com/intro-to-project-lombok
- https://www.baeldung.com/lombok-configuration-system

# Error handling

The app has its own exception `ApiException`, which is handled by and translated to an error response (among with other common Spring exceptions) by the `GlobalErrorHandler` class.

# Automated tests
Automated tests are located under `<PROJECT_HOME>/src/test/java` with unit tests covering the REST, the service, and the repository layers.

# Java Bean Mapping using MapStruct
[MapStruct](https://mapstruct.org/) makes it easy to convert between different java beans (ie: entities and other, simple POJOs).

For more details, see basic usage in the `TodoMapper` interface.

# Code Quality checks
See here for more details: https://developer.okta.com/blog/2019/12/20/five-tools-improve-java.

## Checkstyle
[Checkstyle](https://checkstyle.org/) is an open-source tool available as both Gradle and Maven plugins. It can validate if the code is adhering to our coding standards.

A custom checkstyle configuration file is available at `demo-service/checkstyle.xml`.
Run `./mvnw checkstyle:checkstyle` in the `demo-service` folder to generate the checkstyle report at `demo-service/target/site/checkstyle.html`.

For more details on configuring Checkstyle, see https://www.vogella.com/tutorials/Checkstyle/article.html.
