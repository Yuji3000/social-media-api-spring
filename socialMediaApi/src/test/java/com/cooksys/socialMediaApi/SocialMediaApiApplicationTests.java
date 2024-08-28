package com.cooksys.socialMediaApi;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ActiveProfiles("test")
@Testcontainers
class SocialMediaApiApplicationTests {

	@Container
	static final PostgreSQLContainer<?> POSTGRESQL_CONTAINER =
		new PostgreSQLContainer<>(DockerImageName.parse("postgres:16.4"));

	private static String jdbcUrl() {
		return String.format("jdbc:postgresql://%s:%s/%s", POSTGRESQL_CONTAINER.getHost(),
			POSTGRESQL_CONTAINER.getMappedPort(PostgreSQLContainer.POSTGRESQL_PORT),
			POSTGRESQL_CONTAINER.getDatabaseName());
	}

	@DynamicPropertySource
	static void postgresqlProperties(DynamicPropertyRegistry registry) {
		registry.add("spring.datasource.url", SocialMediaApiApplicationTests::jdbcUrl);
		registry.add("spring.datasource.username", POSTGRESQL_CONTAINER::getUsername);
		registry.add("spring.datasource.password", POSTGRESQL_CONTAINER::getPassword);
	}

	@Test
	void contextLoads() {
	}

	@Test
	void postmanTests() {
		ProcessBuilder processBuilder = new ProcessBuilder();

		String os = System.getProperty("os.name").toLowerCase();

		/*
		 * Using process builder on Windows requires that the extension for the command be specified
		 * The extension for the newman command is 'cmd'
		 * For other operating systems, this extension needs to be omitted.
		 */
		String commandExtension = os.contains("win") ? ".cmd" : "";

		// Set the command and its arguments
		processBuilder.command("newman" + commandExtension,
			"run", "../Assessment 1 Test Suite Implemented Tests.postman_collection.json",
			"-e", "../Assessment 1.postman_environment.json");

		processBuilder.inheritIO();

		try {
			Process process = processBuilder.start();

			int exitCode = process.waitFor();
			System.out.println("Exit Code: " + exitCode);

            assertEquals(0, exitCode);
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}
    }
}
