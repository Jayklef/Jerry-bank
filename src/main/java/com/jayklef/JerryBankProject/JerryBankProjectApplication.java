package com.jayklef.JerryBankProject;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(
		info = @Info(
				title = "The Banking Demo Project",
				description = "The Backend Bank APIs for a Simple banking project",
				contact = @Contact(
						name = "Jerry Arhawho",
						email = "arhawhojerry@gmail.com",
						url = "http://github.com/jayklef"
				),

				license = @License(
						name = "Jerry Arhawho",
						url = "https://github.com/Jayklef/Jerry-bank"
				)
		),

		externalDocs = @ExternalDocumentation(
				description = "Documentation for the Jerry Demo Banking Application",
				url = "https://github.com/Jayklef/Jerry-bank"
		)
)
public class JerryBankProjectApplication {

	public static void main(String[] args) {
		SpringApplication.run(JerryBankProjectApplication.class, args);
	}

}
