package br.com.memory.projetoavaliacao;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;

@OpenAPIDefinition(
	info = @Info(
		title = "Medicine Management API",
		version = "0.0.1",
		description = "API that supports CRUD operations for medicines, manufacturers and adverse reactions."
	),
	servers = {
		@Server(
			description = "Local Server URL",
			url = "http://localhost:8080"
		)
	}
)
@SpringBootApplication
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

}
