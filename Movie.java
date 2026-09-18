package com.movievault;

public class Movie {

    private final String id;
    private final String title;
    private final String year;
    private final double imdbRating;
    private final String plot;
    private final double personalRating;

    public Movie(String id, String title, String year, double imdbRating, String plot) {

        this(id, title, year, imdbRating, plot, 0.0);
    }

    public Movie(String id, String title, String year, double imdbRating, String plot, double personalRating) {

        this.id = id;
        this.title = title;
        this.year = year;
        this.imdbRating = imdbRating;
        this.plot = plot;
        this.personalRating = personalRating;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getYear() {
        return year;
    }

    public double getImdbRating() {
        return imdbRating;
    }

    public String getPlot() {
        return plot;
    }

    public double getPersonalRating() {
        return personalRating;
    }
}
