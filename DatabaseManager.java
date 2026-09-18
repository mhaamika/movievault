package com.movievault;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Manages the SQLite database used by MovieVault.
 *
 * <p>This class creates the movie table and provides methods
 * for saving, retrieving, updating, and deleting movies.</p>
 */
public class DatabaseManager {

    /** URL used to connect to the SQLite database. */
    private static final String DATABASE_URL = "jdbc:sqlite:movies.db";

    /**
     * Creates a DatabaseManager and makes sure the movie table exists.
     */
    public DatabaseManager() {
        createTable();
    }

    /**
     * Creates the movies table if it does not already exist.
     */
    private void createTable() {

        String sql =
                "CREATE TABLE IF NOT EXISTS movies ("
                        + "id TEXT PRIMARY KEY, "
                        + "title TEXT NOT NULL, "
                        + "year TEXT, "
                        + "imdb_rating REAL, "
                        + "plot TEXT, "
                        + "personal_rating REAL)";

        try (
                Connection connection = DriverManager.getConnection(DATABASE_URL);

                Statement statement = connection.createStatement()
        ) {

            statement.execute(sql);

        } catch (SQLException e) {

            System.out.println("Database error: " + e.getMessage());
        }
    }

    /**
     * Adds a movie to the database.
     *
     * @param movie the movie to save
     * @return true if the movie was added successfully, otherwise false
     */
    public boolean addMovie(Movie movie) {

        String sql = "INSERT INTO movies " + "(id, title, year, imdb_rating, plot, personal_rating) "
                        + "VALUES (?, ?, ?, ?, ?, ?)";

        try (
                Connection connection = DriverManager.getConnection(DATABASE_URL);

                PreparedStatement statement = connection.prepareStatement(sql)
        ) {

            statement.setString(1, movie.getId());
            statement.setString(2, movie.getTitle());
            statement.setString(3, movie.getYear());
            statement.setDouble(4, movie.getImdbRating());
            statement.setString(5, movie.getPlot());
            statement.setDouble(6, movie.getPersonalRating());

            statement.executeUpdate();

            return true;

        } catch (SQLException e) {

            if (e.getMessage().contains("UNIQUE") || e.getMessage().contains("PRIMARY KEY")) {

                return false;
            }

            System.out.println("Save error: " + e.getMessage());

            return false;
        }
    }

    /**
     * Retrieves all movies saved in the database.
     *
     * <p>The movies are returned in alphabetical order by title.</p>
     *
     * @return a list containing all saved movies
     */
    public List<Movie> getAllMovies() {

        List<Movie> movies = new ArrayList<>();

        String sql = "SELECT * FROM movies ORDER BY title";

        try (
                Connection connection = DriverManager.getConnection(DATABASE_URL);

                Statement statement = connection.createStatement();

                ResultSet result = statement.executeQuery(sql)
        ) {

            while (result.next()) {

                Movie movie = new Movie(
                        result.getString("id"),
                        result.getString("title"),
                        result.getString("year"),
                        result.getDouble("imdb_rating"),
                        result.getString("plot"),
                        result.getDouble("personal_rating")
                );

                movies.add(movie);
            }

        } catch (SQLException e) {

            System.out.println("Load error: " + e.getMessage());
        }

        return movies;
    }

    /**
     * Deletes a movie from the database.
     *
     * @param id the ID of the movie to delete
     */
    public void deleteMovie(String id) {

        String sql = "DELETE FROM movies WHERE id = ?";

        try (
                Connection connection = DriverManager.getConnection(DATABASE_URL);

                PreparedStatement statement = connection.prepareStatement(sql)
        ) {

            statement.setString(1, id);

            statement.executeUpdate();

        } catch (SQLException e) {

            System.out.println("Delete error: " + e.getMessage());
        }
    }

    /**
     * Updates the personal rating for a saved movie.
     *
     * @param id the ID of the movie to update
     * @param rating the new personal rating
     */
    public void updateRating(String id, double rating) {

        String sql = "UPDATE movies "
                + "SET personal_rating = ? "
                + "WHERE id = ?";

        try (
                Connection connection = DriverManager.getConnection(DATABASE_URL);

                PreparedStatement statement = connection.prepareStatement(sql)
        ) {

            statement.setDouble(1, rating);
            statement.setString(2, id);

            statement.executeUpdate();

        } catch (SQLException e) {

            System.out.println("Rating error: " + e.getMessage());
        }
    }

    /**
     * Calculates the average personal rating of saved movies.
     *
     * <p>Movies with a personal rating of 0 are not included
     * in the calculation.</p>
     *
     * @return the average personal rating, or 0.0 if there are no ratings
     */
    public double getAverageRating() {

        String sql = "SELECT AVG(personal_rating) "
                + "FROM movies "
                + "WHERE personal_rating > 0";

        try (
                Connection connection = DriverManager.getConnection(DATABASE_URL);

                Statement statement = connection.createStatement();

                ResultSet result = statement.executeQuery(sql)
        ) {

            if (result.next()) {
                return result.getDouble(1);
            }

        } catch (SQLException e) {

            System.out.println("Average error: " + e.getMessage());
        }

        return 0.0;
    }
}

