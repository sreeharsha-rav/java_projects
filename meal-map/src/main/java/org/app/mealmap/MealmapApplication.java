package org.app.mealmap;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.reactive.config.EnableWebFlux;

@SpringBootApplication
@EnableWebFlux
@OpenAPIDefinition(
		info = @io.swagger.v3.oas.annotations.info.Info(
				title = "MealMap API",
				version = "1.0",
				description = "API for MealMap Application"
		)
)
public class MealmapApplication {

	public static void main(String[] args) {
		SpringApplication.run(MealmapApplication.class, args);
	}

}
