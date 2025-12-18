package telerik.project.movielibrary.external;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class OmdbMovieResponse {

    @JsonProperty("imdbRating")
    private Double imdbRating;

    @JsonProperty("Response")
    private String response;

    @JsonProperty("Error")
    private String error;
}
