package scot.oskar.securedoc;

import org.testcontainers.containers.PostgreSQLContainer;

public class TestContainersSingleton {
  private static final PostgreSQLContainer<?> POSTGRES;

  static {
    POSTGRES = new PostgreSQLContainer<>("postgres:14.3")
        .withDatabaseName("test_db")
        .withUsername("test")
        .withPassword("test");
    POSTGRES.start();
  }

  public static PostgreSQLContainer<?> getPostgresContainer() {
    return POSTGRES;
  }
}
