package com.example.fail;

import static org.assertj.core.api.Assertions.assertThat;

import eu.rekawek.toxiproxy.Proxy;
import eu.rekawek.toxiproxy.ToxiproxyClient;
import eu.rekawek.toxiproxy.model.ToxicDirection;
import eu.rekawek.toxiproxy.model.toxic.Latency;
import java.io.IOException;
import java.time.Duration;
import java.util.concurrent.TimeoutException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.r2dbc.DataR2dbcTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.Network;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.containers.ToxiproxyContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import reactor.test.StepVerifier;
import reactor.test.StepVerifier.Step;
import reactor.util.retry.Retry;

@Testcontainers
@DataR2dbcTest
class TestApplicationPostgressTests {
  private static final Logger logger = LoggerFactory.getLogger(TestApplicationNetworkFailuresTests.class);

  @Autowired
  private ServerlessServicesRepository serverlessServicesRepository;

  @Container
  static final PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15-alpine");

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

  @BeforeEach
  void setUp() throws IOException {

  }

  @Test
  void normal() {
    StepVerifier.create(this.serverlessServicesRepository.findAll()).expectNextCount(3).verifyComplete();
  }

  @Test
  void addnew() {
    StepVerifier.create(this.serverlessServicesRepository.save(new ServerlessServices(123l, "NextGenService"))).expectNextCount(3);
  }
}