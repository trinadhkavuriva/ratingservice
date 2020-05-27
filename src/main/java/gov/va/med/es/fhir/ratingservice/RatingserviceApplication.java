package gov.va.med.es.fhir.ratingservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

//@EnableAdminServer
//CrossOrigin(origins = "http://localhost:8000", maxAge = 3600, methods = RequestMethod.DELETE, )
@SpringBootApplication
@ComponentScan(basePackages = {"gov.va.med.es.fhir.ratingservice", "gov.va.med.es.fhir.ratingservice.config",
		"gov.va.med.es.fhir.ratingservice.controller"})
public class RatingserviceApplication {

	public static void main(String[] args) {
		SpringApplication.run(RatingserviceApplication.class, args);
	}
	
	
	
	@Bean
	public WebMvcConfigurer corsConfigurer() {
		return new WebMvcConfigurer() {
			@Override
			public void addCorsMappings(CorsRegistry registry) {
				registry.addMapping("/rating-service").allowedOrigins("http://localhost:8000")
				.allowedMethods("GET", "OPTIONS");
			}
		};
	}
	
	
}
