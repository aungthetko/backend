package com.demo.springjwt.config;

import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.Collections;
import java.util.List;

import static com.demo.springjwt.constant.SwaggerConstant.*;
import static java.util.Collections.singletonList;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Configuration
public class SwaggerConfig {

    @Bean
    public Docket apiDocket(){
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .forCodeGeneration(true)
                .securityContexts(Collections.singletonList(securityContext()))
                .securitySchemes(singletonList(apiKey()))
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.demo.springjwt.controller"))
                .paths(PathSelectors.regex("/*/.*"))
                .build()
                .tags(new Tag(API_TAG, "All api relating to users"));
    }

    private ApiInfo apiInfo(){
        return new ApiInfo(
                API_TITLE,
                API_DESCRIPTION,
                API_VERSION,
                TERM_OF_SERVICE,
                contact(),
                API_LICENSE,
                LICENSE_URL,
                Collections.emptyList()
        );
    }

    private Contact contact(){
        return new Contact(
                CONTACT_NAME,
                CONTACT_URL,
                CONTACT_EMAIL
        );
    }

    private SecurityContext securityContext(){
        return SecurityContext.builder().securityReferences(securityReference()).build();
    }

    private List<SecurityReference> securityReference(){
        AuthorizationScope[] scopes = { new AuthorizationScope(AUTHORIZATION_SCOPE ,AUTHORIZATION_DESCRIPTION) };
        return Collections.singletonList(new SecurityReference(SECURITY_REFERENCE, scopes));
    }

    private ApiKey apiKey(){
        return new ApiKey(SECURITY_REFERENCE, AUTHORIZATION, SecurityScheme.In.HEADER.name());
    }
}
