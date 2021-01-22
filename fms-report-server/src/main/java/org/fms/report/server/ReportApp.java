package org.fms.report.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

/**
 * Hello world!
 *
 */
@EnableEurekaClient
@SpringBootApplication(scanBasePackages = "org.fms.report", exclude = MongoAutoConfiguration.class)
public class ReportApp {
	public static void main(String[] args) {

		SpringApplication.run(ReportApp.class, args);
	}
}
