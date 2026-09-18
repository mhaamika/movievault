package com.movievault;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/** * Handles communication between MovieVault and the OMDb API.
 * * * <p>This class sends requests to the OMDb API and converts
 * * the returned JSON data into Movie objects.</p> */
public class OMDbService {

    private final String apiKey;

    private final HttpClient client = HttpClient.newHttpClient();


    /** * Creates an OMDbService using the provided API key.
     * * * @param apiKey the API key used to access the OMDb API */
    public OMDbService(String apiKey) {

        this.apiKey = apiKey;
    }


    /** * Searches the OMDb API for movies matching a search term.
     * * * <p>The method returns up to 10 movies from the search results.</p>
     * * * @param searchTerm the movie title or search term *
     * @return a list of movies matching the search term *
     * @throws IOException if the API request fails or returns an error
     * * @throws InterruptedException if the request is interrupted */

    public List<Movie> searchMovies(String searchTerm)
            throws IOException, InterruptedException {

        List<Movie> movies = new ArrayList<>();

        String encoded = URLEncoder.encode(searchTerm, StandardCharsets.UTF_8);

        String url = "https://www.omdbapi.com/?apikey="
                        + apiKey
                        + "&s="
                        + encoded
                        + "&type=movie";

        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url)).GET().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        JsonObject json = JsonParser.parseString(response.body()).getAsJsonObject();

        if (!json.has("Response")
                || !json.get("Response")
                .getAsString()
                .equals("True")) {

            String error = json.has("Error")
                    ? json.get("Error").getAsString()
                    : "No movies found.";

            throw new IOException(error);
        }

        JsonArray results = json.getAsJsonArray("Search");

        for (int i = 0; i < results.size() && i < 10; i++) {

            JsonObject item = results.get(i).getAsJsonObject();

            Movie movie = new Movie(item.get("imdbID").getAsString(), item.get("Title").getAsString(), item.get("Year").getAsString(), 0.0, "");

            movies.add(movie);
        }

        return movies;
    }


    /** * Retrieves detailed information about a movie. * *
     * <p>The movie is searched using its IMDb ID.
     * The returned * information includes the title, year, IMDb rating, and plot.</p>
     *
     * * * @param imdbId the IMDb ID of the movie *
     * @return a Movie object containing the movie's detailed information
     * * @throws IOException if the API request fails or movie details are not found
     * * @throws InterruptedException if the request is interrupted */
    public Movie getMovieDetails(String imdbId) throws IOException, InterruptedException
    {
        String url = "https://www.omdbapi.com/?apikey="
                        + apiKey
                        + "&i="
                        + imdbId
                        + "&plot=full";

        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url)).GET().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        JsonObject json = JsonParser.parseString(response.body()).getAsJsonObject();

        if (!json.has("Response") || !json.get("Response").getAsString().equals("True")) {

            throw new IOException("Movie details not found.");
        }

        double rating = 0.0;

        String ratingText = json.get("imdbRating").getAsString();

        if (!ratingText.equals("N/A")) {

            try {
                rating = Double.parseDouble(ratingText);
            } catch (NumberFormatException ignored) {
                rating = 0.0;
            }
        }

        String plot = json.has("Plot")
                ? json.get("Plot").getAsString()
                : "No plot available.";

        return new Movie(json.get("imdbID").getAsString(),
                json.get("Title").getAsString(),
                json.get("Year").getAsString(),
                rating,
                plot);
    }
}
