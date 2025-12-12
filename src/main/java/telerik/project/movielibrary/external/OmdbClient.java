package telerik.project.movielibrary.external;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
@RequiredArgsConstructor
public class OmdbClient {

    private final RestClient omdbRestClient;

    public OmdbMovieResponse fetchByTitle(String title) {
        return omdbRestClient.get()
                .uri(uriBuilder -> uriBuilder
                        .queryParam("i", "tt3896198")
                        .queryParam("apikey", "f986b0bc")
                        .queryParam("t", title)
                        .build())
                .retrieve()
                .body(OmdbMovieResponse.class);
    }
}
