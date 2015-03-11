package me.taborda.mashtv.config ;

import org.springframework.beans.factory.annotation.Autowired ;
import org.springframework.context.annotation.Bean ;
import org.springframework.context.annotation.Configuration ;

import com.mangofactory.swagger.configuration.SpringSwaggerConfig ;
import com.mangofactory.swagger.models.dto.ApiInfo ;
import com.mangofactory.swagger.plugin.EnableSwagger ;
import com.mangofactory.swagger.plugin.SwaggerSpringMvcPlugin ;

@Configuration
@EnableSwagger
public class SwaggerConfig {

    private static final String TITLE = "MashTV" ;

    private static final String DESCRIPTION = "" ;

    private static final String CONTACT_EMAIL = "pedro@taborda.me" ;

    private static final String LICENSE_TYPE = "MIT" ;

    private static final String LICENSE_URL = "http://opensource.org/licenses/MIT" ;

    @Autowired
    private SpringSwaggerConfig springSwaggerConfig ;

    @Bean
    public SwaggerSpringMvcPlugin customImplementation() {
        ApiInfo apiInfo = new ApiInfo(TITLE, DESCRIPTION, "", CONTACT_EMAIL, LICENSE_TYPE, LICENSE_URL) ;
        return new SwaggerSpringMvcPlugin(springSwaggerConfig).apiInfo(apiInfo).includePatterns("/api.*") ;
    }
}
