package edu.msudenver.cs3013.movielist

class Movie (val title: String, val year: String, val genre: String, val rating: String) {
    fun convertOut(): String {
        return title + "," + year + "," + genre + "," + rating
    }
}