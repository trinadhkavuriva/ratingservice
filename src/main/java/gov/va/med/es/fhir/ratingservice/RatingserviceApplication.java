package gov.va.med.es.fhir.ratingservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import de.codecentric.boot.admin.server.config.EnableAdminServer;

//@EnableAdminServer
@SpringBootApplication
@ComponentScan(basePackages = {"gov.va.med.es.fhir.ratingservice", "gov.va.med.es.fhir.ratingservice.config",
		"gov.va.med.es.fhir.ratingservice.controller"})
public class RatingserviceApplication {

	public static void main(String[] args) {
		SpringApplication.run(RatingserviceApplication.class, args);
	}
}
