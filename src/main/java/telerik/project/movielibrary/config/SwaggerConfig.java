package telerik.project.movielibrary.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI movieLibraryOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Movie Library REST API")
                        .description("Secure REST API for managing movies with asynchronous rating enrichment")
                        .version("1.0.0")
                );
    }
}