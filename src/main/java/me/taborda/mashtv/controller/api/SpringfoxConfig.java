package me.taborda.mashtv.controller.api;

import static springfox.documentation.builders.PathSelectors.regex;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SpringfoxConfig {

    @Bean
    public Docket swaggerSpringMvcPlugin() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("mashtv")
                .apiInfo(apiInfo())
                .select()
                .paths(regex("/api/.*"))
                .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("MashTV")
//                .description("")
//                .termsOfServiceUrl("http://springfox.io")
                .contact(new Contact("Pedro Taborda", "https://github.com/ptab", "pedro@taborda.me"))
                .license("MIT")
                .licenseUrl("http://opensource.org/licenses/MIT")
                .version("1.0")
                .build();
    }

}
