package telerik.project.movielibrary.external;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClient;
import telerik.project.movielibrary.exceptions.ExternalApiServiceException;

@Component
@RequiredArgsConstructor
public class OmdbClient {

    private final RestClient omdbRestClient;

    @Value("${omdb.api.key}")
    private String apiKey;

    public OmdbMovieResponse fetchByTitle(String title) {
        try {
            OmdbMovieResponse response = omdbRestClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .queryParam("apikey", apiKey)
                            .queryParam("t", title)
                            .build())
                    .retrieve()
                    .body(OmdbMovieResponse.class);

            if (response.getResponse() != null && response.getResponse().equals(Boolean.TRUE)) {
                throw new ExternalApiServiceException(response.getError());
            }

            return response;
        } catch (HttpClientErrorException e) {
            throw new ExternalApiServiceException("Invalid request to external service.");
        } catch (HttpServerErrorException e) {
            throw new ExternalApiServiceException("External service is currently unavailable.");
        } catch (ResourceAccessException e) {
            throw new ExternalApiServiceException("Cannot connect to external service.");
        }
    }
}
