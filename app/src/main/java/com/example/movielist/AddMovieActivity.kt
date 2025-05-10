package com.example.movielist
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class AddMovieActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_add_movie)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
    fun backToFirst(v : View) {

        Log.d("RECYCLE", "AddmovieAct backToFirst ===== ENTERED  ================")
        var textTitle = findViewById<EditText>(R.id.title)
        var textYear = findViewById<EditText>(R.id.year)
        var textGenre = findViewById<EditText>(R.id.genre)
        var textRating = findViewById<EditText>(R.id.rating)

        var title = textTitle.getText().toString()
        var year = textYear.getText().toString()
        var genre = textGenre.getText().toString()
        var rating = textRating.getText().toString()
        Log.d("RECYCLE", "AddmovieAct backToFirst ===== TITLE $title ================")
        setMovieInfo(title, year, genre, rating)
    }
    private fun setMovieInfo(title: String, year: String, genre: String, rating: String) {
        var movieInfoIntent = Intent()
        Log.d("RECYCLE", "AddmovieAct setMovieInfo ===== TITLE $title ================")
        movieInfoIntent.putExtra("title", title)
        movieInfoIntent.putExtra("year", year)
        movieInfoIntent.putExtra("genre", genre)
        movieInfoIntent.putExtra("rating", rating)
        setResult(Activity.RESULT_OK, movieInfoIntent)
        finish()
    }
}