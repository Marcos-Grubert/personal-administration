package br.com.eventhorizon.personaladminsitration;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class PersonaladminsitrationApplication {
	public static void main(String[] args) {
		SpringApplication.run(PersonaladminsitrationApplication.class, args);
	}
}
