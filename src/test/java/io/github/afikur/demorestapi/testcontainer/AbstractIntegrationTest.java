package io.github.afikur.demorestapi.testcontainer;

import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.containers.MySQLContainer;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public abstract class AbstractIntegrationTest {

    static MySQLContainer container = new MySQLContainer("mysql:8");

    @DynamicPropertySource
    public static void overrideProps(DynamicPropertyRegistry registry) {
        container.start();
        registry.add("spring.datasource.url", container::getJdbcUrl);
        registry.add("spring.datasource.username", container::getUsername);
        registry.add("spring.datasource.password", container::getPassword);
    }
}