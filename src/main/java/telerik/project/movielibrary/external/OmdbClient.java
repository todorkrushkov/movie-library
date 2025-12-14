package telerik.project.movielibrary.external;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
@RequiredArgsConstructor
public class OmdbClient {

    private final RestClient omdbRestClient;

    @Value("${omdb.api.key}")
    private String apiKey;

    public OmdbMovieResponse fetchByTitle(String title) {
        return omdbRestClient.get()
                .uri(uriBuilder -> uriBuilder
                        .queryParam("apikey", apiKey)
                        .queryParam("t", title)
                        .build())
                .retrieve()
                .body(OmdbMovieResponse.class);
    }
}
