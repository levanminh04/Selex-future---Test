package com.levanminh.car_rental;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.OAuthFlow;
import io.swagger.v3.oas.annotations.security.OAuthFlows;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
@SecurityScheme(
	name = "keycloak",
	type = SecuritySchemeType.OAUTH2,
	bearerFormat = "JWT",
	scheme = "bearer",
	in = SecuritySchemeIn.HEADER,
	flows = @OAuthFlows(
		authorizationCode = @OAuthFlow(
			authorizationUrl = "http://localhost:9090/realms/car_rental/protocol/openid-connect/auth",
			tokenUrl = "http://localhost:9090/realms/car_rental/protocol/openid-connect/token",
			scopes = {
				@io.swagger.v3.oas.annotations.security.OAuthScope(name = "openid", description = "OpenID Connect scope"),
				@io.swagger.v3.oas.annotations.security.OAuthScope(name = "profile", description = "Profile scope"),
				@io.swagger.v3.oas.annotations.security.OAuthScope(name = "email", description = "Email scope"),
				@io.swagger.v3.oas.annotations.security.OAuthScope(name = "roles", description = "Roles scope")
			}
		),
		password = @OAuthFlow(
			tokenUrl = "http://localhost:9090/realms/car_rental/protocol/openid-connect/token",
			scopes = {
				@io.swagger.v3.oas.annotations.security.OAuthScope(name = "openid", description = "OpenID Connect scope"),
				@io.swagger.v3.oas.annotations.security.OAuthScope(name = "profile", description = "Profile scope"),
				@io.swagger.v3.oas.annotations.security.OAuthScope(name = "email", description = "Email scope"),
				@io.swagger.v3.oas.annotations.security.OAuthScope(name = "roles", description = "Roles scope")
			}
		)
	)
)
public class CarRentalApplication {

	public static void main(String[] args) {
		SpringApplication.run(CarRentalApplication.class, args);
	}

}
