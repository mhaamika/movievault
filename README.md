#  MovieVault

MovieVault is a Java desktop application that allows users to search for movies, view detailed information, save movies to a personal collection, and manage personal ratings.

The application integrates the **OMDb REST API** for movie information and uses **SQLite with JDBC** for persistent local storage.


## Technologies

* **Java 21**
* **JavaFX** for the graphical user interface
* **SQLite** for local database storage
* **JDBC** for database connectivity
* **OMDb API** for movie data
* **Gson** for JSON parsing
* **Maven** for dependency management
* **Git/GitHub** for version control


## Features

* Search for movies by title
* View movie details, including year, IMDb rating, and plot
* Save movies to a personal collection
* Add and update personal movie ratings
* Remove movies from the collection
* View collection statistics and average personal rating
* Handle invalid searches and API errors
* Store saved movies locally using SQLite



## Project Structure
```text
movievault/
├── src/
│   └── main/
│       └── java/
│           └── com/
│               └── movievault/
│                   ├── Main.java
│                   ├── Movie.java
│                   ├── OMDbService.java
│                   └── DatabaseManager.java
│
├── pom.xml
├── .gitignore
└── README.md
```


### Main Components

**Main.java**
Creates the JavaFX interface and handles user interactions such as searching, saving, rating, and deleting movies.

**Movie.java**
Represents a movie and stores information such as its IMDb ID, title, year, IMDb rating, plot, and personal rating.

**OMDbService.java**
Communicates with the OMDb API, sends movie search requests, retrieves movie details, and processes JSON responses.

**DatabaseManager.java**
Manages the SQLite database using JDBC, including creating the movie table and performing insert, retrieve, update, and delete operations.


## API Configuration

MovieVault uses the [OMDb API](https://www.omdbapi.com/) to retrieve movie information.

For security, the API key should **not** be stored directly in the source code or committed to GitHub.

Create a local `.env` file containing:

```text
OMDB_API_KEY=your_api_key_here
```
The `.env` file is excluded from version control using `.gitignore`.


### Running the Project

1. Clone the repository:

```bash
git clone https://github.com/YOUR-USERNAME/movievault.git
```

2. Open the project in IntelliJ IDEA.

3. Make sure Maven has downloaded the project dependencies.

4. Get you own API key at [OMDb API](https://www.omdbapi.com/)

6. Add your OMDb API key to the local `.env` file.

7. Run `Main.java` through IntelliJ or Maven.

> An OMDb API key is required for live movie searches.


## Database

MovieVault uses a local SQLite database named `movies.db`.

The database is created automatically when the application starts, and the `movies` table is created if it does not already exist.

The local database is excluded from GitHub through `.gitignore`.


## Screenshots

## Demo Video
Please watch a few seconds video for a quick demo on how the project runs

[![MovieVault Demo](https://img.youtube.com/vi/wiy595Lo_TQ/maxresdefault.jpg)](https://www.youtube.com/watch?v=wiy595Lo_TQ)

### Movie Search

1. Handles invalid search results

<img width="1414" height="801" alt="Screenshot 2026-09-19 at 3 09 27 PM" src="https://github.com/user-attachments/assets/dc8b2528-880f-45af-9cfa-bbce8f6731c7" />

2. Search results

<img width="1409" height="803" alt="Screenshot 2026-09-19 at 3 10 19 PM" src="https://github.com/user-attachments/assets/acd2a413-a065-492e-a48f-8c8d28ef7dd7" />


3. View details button

<img width="1409" height="809" alt="Screenshot 2026-09-19 at 3 10 52 PM" src="https://github.com/user-attachments/assets/ed12fad8-c0ef-4825-9e48-4a0908191554" />

4. Save Movie button

<img width="1411" height="801" alt="Screenshot 2026-09-19 at 3 11 18 PM" src="https://github.com/user-attachments/assets/52985e20-fc23-42d7-96c2-95aa5d2d6d91" />


<img width="1416" height="802" alt="Screenshot 2026-09-19 at 3 11 49 PM" src="https://github.com/user-attachments/assets/25e8871b-c054-4157-989c-9def7e2ac7f8" />

5. Rating

<img width="1411" height="804" alt="Screenshot 2026-09-19 at 3 12 45 PM" src="https://github.com/user-attachments/assets/d4ae3f80-8d97-4326-b306-82563d11dacb" />

6. Delete Movie

<img width="1400" height="809" alt="Screenshot 2026-09-19 at 3 13 27 PM" src="https://github.com/user-attachments/assets/4bb5fc6e-429a-4508-b6e4-246184a9f345" />


## What I Learned

This project gave me hands-on experience with:

* Building a graphical user interface with JavaFX
* Working with REST APIs and HTTP requests
* Parsing JSON data
* Connecting Java applications to a relational database using JDBC
* Performing CRUD operations with SQLite
* Handling exceptions and invalid user input
* Managing API credentials securely
* Organizing a Java project using Maven


## Future Improvements

Potential future improvements include:

* Movie posters and visual thumbnails
* Genre and release-date filtering
* Sorting saved movies
* Additional collection statistics
* Improved UI styling
* Movie recommendations
* User profiles and multiple collections

