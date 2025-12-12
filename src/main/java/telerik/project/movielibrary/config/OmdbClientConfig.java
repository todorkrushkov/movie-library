package telerik.project.movielibrary.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

@Configuration
public class OmdbClientConfig {

    @Bean
    public RestClient omdbRestClient() {
        HttpComponentsClientHttpRequestFactory factory =
                new HttpComponentsClientHttpRequestFactory();

        factory.setConnectionRequestTimeout(3000);
        factory.setReadTimeout(5000);

        return RestClient.builder()
                .baseUrl("https://www.omdbapi.com")
                .requestFactory(factory)
                .build();
    }
}
