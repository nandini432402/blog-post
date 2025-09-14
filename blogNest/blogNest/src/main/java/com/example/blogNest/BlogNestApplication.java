package com.example.blogNest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.annotation.PostConstruct;
import java.util.TimeZone;

/**
 * BlogNest - Social Media Blogging Platform
 * Main Spring Boot Application Class
 *
 * This is the entry point of the BlogNest application, a comprehensive social media
 * blogging platform where users can create, share, and interact with blog posts.
 *
 * Features enabled:
 * - JPA Auditing for automatic createdAt/updatedAt timestamps
 * - Async processing for non-blocking operations (email, notifications)
 * - Scheduling for background tasks (cleanup, analytics)
 * - Transaction management for data consistency
 *
 * @author BlogNest Team
 * @version 1.0
 * @since 2024
 */
@SpringBootApplication
@EnableJpaAuditing(auditorAwareRef = "auditorAware")
@EnableAsync
@EnableScheduling
@EnableTransactionManagement
public class BlogNestApplication {

	/**
	 * Initialize application with default timezone
	 * This ensures consistent date/time handling across different server environments
	 */
	@PostConstruct
	void init() {
		// Set default timezone to UTC for consistent date/time handling
		TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
		System.out.println("BlogNest Application starting with UTC timezone...");
	}

	/**
	 * Main method - Entry point of the Spring Boot application
	 *
	 * @param args Command line arguments
	 */
	public static void main(String[] args) {
		// Print application banner
		System.out.println("========================================");
		System.out.println("    Starting BlogNest Application      ");
		System.out.println("  Social Media Blogging Platform       ");
		System.out.println("========================================");

		try {
			// Start the Spring Boot application
			SpringApplication.run(BlogNestApplication.class, args);

			System.out.println("========================================");
			System.out.println("  BlogNest Application Started Successfully!");
			System.out.println("  Ready to accept requests...          ");
			System.out.println("========================================");

		} catch (Exception e) {
			System.err.println("Failed to start BlogNest Application: " + e.getMessage());
			e.printStackTrace();
			System.exit(1);
		}
	}
}