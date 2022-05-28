package com.springboot.rafael.config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import com.mysql.cj.x.protobuf.MysqlxDatatypes.Array;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import io.swagger.models.Contact;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class Swagger {

    private Contact contact(){
        return new Contact()
            .email("meireles200@hotmail.com")
            .name("Rafael")
            .url("http://github.com/rafael-wassoaski");
    }

    private ApiInfo apiInfo(){
        return new ApiInfoBuilder()
            .title("Vendas API")
            .description("API do projeto de vendas")
            .version("1.0")
            // .contact(this.contact())
            .build();
    }

    @Bean
    public Docket docked(){
        return new Docket(DocumentationType.SWAGGER_2)
            .useDefaultResponseMessages(false)
            .select()
            .apis(RequestHandlerSelectors.basePackage("com.springboot.rafael.api.controller"))
            .paths(PathSelectors.any())
            .build()
            .securityContexts(Arrays.asList(this.securityContext()))
            .securitySchemes(Arrays.asList(this.apikey()))
            .apiInfo(this.apiInfo());
    }

    public ApiKey apikey(){
        return new ApiKey("JTW", "Authorization", "header");
    }

    private SecurityContext securityContext(){
        return SecurityContext.builder()
        .securityReferences(this.defaultAuth())
        .forPaths(PathSelectors.any())
        .build();
    }

    private List<SecurityReference> defaultAuth(){
        AuthorizationScope scope = new AuthorizationScope("global", "Access everything");
        AuthorizationScope[] scopes = new AuthorizationScope[1];
        scopes[0] = scope;

        SecurityReference reference = new SecurityReference("JWT", scopes);
        List<SecurityReference> references = new ArrayList<>();

        references.add(reference);

        return references;
    }

    
}
