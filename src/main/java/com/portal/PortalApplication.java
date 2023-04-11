package com.portal;

import java.io.IOException;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;
import io.swagger.v3.oas.models.parameters.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.media.StringSchema;
import org.json.JSONObject;
import org.springdoc.core.customizers.OperationCustomizer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.portal.flow.FileLoader;
import org.springframework.web.method.HandlerMethod;

@SpringBootApplication
@OpenAPIDefinition(info = @Info(title = "Recruitment Portal API",
		version = "v1",
		description = "These are APIs for Recruitment Portal"),
		servers = @Server(url = "/"))
public class PortalApplication {

	public static void main(String[] args) {
		SpringApplication.run(PortalApplication.class, args);
	}
	
	@Bean(name = "workflow")
	public JSONObject WorkFlowJson() throws IOException {
		FileLoader fileloader = new FileLoader();
		return fileloader.readWorkFlowJson();
	}

	@Bean
	public OperationCustomizer customGlobalHeaders() {
		return (Operation operation, HandlerMethod handlerMethod) -> {
			Parameter token = new Parameter().in(ParameterIn.HEADER.toString()).schema(new StringSchema())
					.name("token").description("Auth token").required(true);
			operation.addParametersItem(token);
			return operation;
		};
	}
}
