package com.movievault;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import java.util.List;
import io.github.cdimascio.dotenv.Dotenv;

/** * Main class for the MovieVault application. * *
 * <p>This class creates the JavaFX user interface and handles *
 * searching for movies, viewing movie details, saving movies, *
 * rating movies, and deleting movies from the collection.</p> */

public class Main extends Application {

    private static final Dotenv dotenv = Dotenv.load();

    private static final String API_KEY = dotenv.get("OMDB_API_KEY");

    private final DatabaseManager database = new DatabaseManager();

    private final OMDbService omdb = new OMDbService(API_KEY);

    private final VBox searchResults = new VBox(10);

    private final VBox savedMovies = new VBox(10);

    private final Label statisticsLabel = new Label();


    /** * Starts the MovieVault JavaFX application. *
     * * @param stage the main window of the application */
    @Override
    public void start(Stage stage) {

        Label title =
                new Label("🎬 MovieVault");

        title.setStyle(
                "-fx-font-size: 28px; "
                        + "-fx-font-weight: bold;"
        );

        TextField searchField =
                new TextField();

        searchField.setPromptText(
                "Enter a movie title..."
        );

        searchField.setPrefWidth(400);

        Button searchButton = new Button("Search");

        searchButton.setOnAction(e -> searchMovies(searchField.getText()));

        searchField.setOnAction(e -> searchMovies(searchField.getText()));

        HBox searchBar = new HBox(10, searchField, searchButton);

        searchBar.setAlignment(Pos.CENTER);

        Label searchTitle = new Label("Search Results");

        searchTitle.setStyle("-fx-font-size: 20px; " + "-fx-font-weight: bold;");

        Label collectionTitle = new Label("My Movie Collection");

        collectionTitle.setStyle("-fx-font-size: 20px; " + "-fx-font-weight: bold;");

        ScrollPane searchScroll = new ScrollPane(searchResults);

        searchScroll.setFitToWidth(true);
        searchScroll.setPrefHeight(300);

        ScrollPane savedScroll = new ScrollPane(savedMovies);

        savedScroll.setFitToWidth(true);
        savedScroll.setPrefHeight(300);

        VBox root =
                new VBox(
                        15,
                        title,
                        searchBar,
                        searchTitle,
                        searchScroll,
                        collectionTitle,
                        savedScroll,
                        statisticsLabel
                );

        root.setPadding(
                new Insets(20)
        );

        updateCollection();

        stage.setTitle("MovieVault");

        stage.setScene(new Scene(root, 800, 900));

        stage.show();
    }


    /** * Searches the OMDb API for movies matching the given title. *
     * * @param term the movie title entered by the user */
    private void searchMovies(String term) {

        if (term.isBlank()) {

            showMessage("Please enter a movie title.");

            return;
        }

        searchResults.getChildren().clear();

        try {

            List<Movie> movies =
                    omdb.searchMovies(term);

            if (movies.isEmpty()) {

                searchResults.getChildren().add(new Label("No movies found."));

                return;
            }

            for (Movie movie : movies) {

                searchResults.getChildren().add(createSearchCard(movie));
            }

        } catch (Exception e) {

            showMessage(e.getMessage());
        }
    }



    /** * Creates a user interface card for a movie returned from a search. *
     *  <p>The card contains the movie title, release year,
     *  a button for viewing details, and a button for saving the movie.</p>
     * @param movie the movie to display *
     * @return a VBox containing the movie's search information and buttons */
    private VBox createSearchCard(Movie movie) {

        Label title = new Label(movie.getTitle());
        title.setStyle("-fx-font-size: 18px; " + "-fx-font-weight: bold;"
        );

        Label year = new Label("Year: " + movie.getYear());

        Button details = new Button("View Details");

        Button save = new Button("Save Movie");

        details.setOnAction(e -> {

            try {

                Movie fullMovie = omdb.getMovieDetails(movie.getId());

                showMovieDetails(fullMovie);

            } catch (Exception ex) {

                showMessage(ex.getMessage());
            }
        });

        save.setOnAction(e -> {

            try {

                Movie fullMovie = omdb.getMovieDetails(movie.getId());

                if (database.addMovie(fullMovie)) {

                    showMessage(fullMovie.getTitle() + " was added.");

                    updateCollection();

                } else {

                    showMessage(fullMovie.getTitle() + " is already saved.");
                }

            } catch (Exception ex) {

                showMessage(ex.getMessage());
            }
        });

        HBox buttons = new HBox(8, details, save);

        VBox card = new VBox(7, title, year, buttons);

        card.setPadding(new Insets(12));

        card.setStyle(
                "-fx-border-color: #cccccc; "
                        + "-fx-border-radius: 5;"
        );

        return card;
    }

    /** * Displays detailed information about a selected movie.
     * * @param movie the movie whose details should be displayed */
    private void showMovieDetails(Movie movie) {

        Alert alert =
                new Alert(Alert.AlertType.INFORMATION);

        alert.setTitle("Movie Details");

        alert.setHeaderText(movie.getTitle());

        alert.setContentText("Year: "
                        + movie.getYear()
                        + "\nIMDb Rating: "
                        + String.format(
                                "%.1f",
                                movie.getImdbRating()
                        )
                        + "\n\n"
                        + movie.getPlot()
        );

        alert.showAndWait();
    }

    /** * Updates the movie collection displayed in the application.
     * * * <p>This method retrieves all saved movies from the database,
     * * displays them, and updates the collection statistics.</p> */
    private void updateCollection() {

        savedMovies.getChildren().clear();

        List<Movie> movies = database.getAllMovies();

        if (movies.isEmpty()) {

            savedMovies.getChildren()
                    .add(new Label("Your collection is empty."));
        }

        for (Movie movie : movies) {

            savedMovies.getChildren().add(createSavedCard(movie));
        }

        statisticsLabel.setText(
                "Movies in collection: "
                        + movies.size()
                        + "    Average personal rating: "
                        + String.format(
                                "%.1f",
                                database.getAverageRating()
                        )
        );
    }


    /** * Creates a user interface card for a saved movie.
     * * * <p>The card displays the movie's title, IMDb rating,
     * * personal rating, and controls for rating or deleting the movie.
     * </p> * * @param movie the saved movie to display *
     * @return a VBox containing the movie information and controls */
    private VBox createSavedCard(Movie movie) {

        Label title = new Label(movie.getTitle());

        title.setStyle("-fx-font-size: 17px; " + "-fx-font-weight: bold;");

        Label imdb =
                new Label(
                        "IMDb Rating: "
                                + String.format(
                                        "%.1f",
                                        movie.getImdbRating()
                                )
                );

        Label personal =
                new Label("My Rating: " + (movie.getPersonalRating() == 0
                                        ? "Not rated"
                                        : String.format(
                                        "%.1f / 10",
                                        movie.getPersonalRating()
                                )
                        )
                );

        TextField ratingField = new TextField();

        ratingField.setPromptText("Rating 0-10");

        ratingField.setPrefWidth(120);

        Button rate = new Button("Rate");

        Button delete = new Button("Delete");

        rate.setOnAction(e -> {

            try {

                double rating = Double.parseDouble(ratingField.getText());

                if (rating < 0 || rating > 10) {

                    showMessage("Rating must be between 0 and 10.");

                    return;
                }

                database.updateRating(movie.getId(), rating);

                updateCollection();

            } catch (NumberFormatException ex) {

                showMessage("Please enter a number between 0 and 10.");
            }
        });

        delete.setOnAction(e -> {database.deleteMovie(movie.getId());

            updateCollection();
        });

        HBox buttons = new HBox(8, ratingField, rate, delete);

        VBox card = new VBox(7, title, imdb, personal, buttons);

        card.setPadding(new Insets(12));

        card.setStyle("-fx-border-color: #cccccc; " + "-fx-border-radius: 5;");

        return card;
    }

    /** * Displays an informational message to the user.
     * * * @param message the message to display */
    private void showMessage(String message) {

        Alert alert = new Alert(Alert.AlertType.INFORMATION);

        alert.setTitle("MovieVault");

        alert.setHeaderText(null);

        alert.setContentText(message == null ? "Something went wrong." : message);

        alert.showAndWait();
    }


    /** * Starts the MovieVault application.
     * * * @param args command-line arguments */
    public static void main(String[] args) {
        launch();
    }
}
