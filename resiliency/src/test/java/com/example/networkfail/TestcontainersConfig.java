package com.example.networkfail;

import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;

@TestConfiguration(proxyBeanMethods = false)
public class TestcontainersConfig {
  // @Autowired
  // private ServerlessServicesRepository serverlessServicesRepository;
  @Container
  static final PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15-alpine");

  // @Bean
  // // @ServiceConnection
  // public PostgreSQLContainer<?> postgreSQLContainer() {
  //   return new PostgreSQLContainer<>("postgres:15.2-alpine");
  // }

  @DynamicPropertySource
  static void sqlserverProperties(DynamicPropertyRegistry registry) throws IOException {
    String url = String.format("r2dbc:postgresql://%s:%d/%s", postgres.getHost(),
        postgres.getFirstMappedPort(),
        postgres.getDatabaseName());

    registry.add("spring.r2dbc.url", () -> url);
    registry.add("spring.r2dbc.username", postgres::getUsername);
    registry.add("spring.r2dbc.password", postgres::getPassword);

    registry.add("spring.flyway.url", postgres::getJdbcUrl);
    registry.add("spring.flyway.user", postgres::getUsername);
    registry.add("spring.flyway.password", postgres::getPassword);
  }
}
