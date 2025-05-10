package com.example.movielist

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.io.File
import java.io.FileWriter
import java.io.IOException
import java.util.Scanner


class MainActivity : AppCompatActivity() {
    val movieList: MutableList<Movie> =  ArrayList<Movie>()
    val movieAdapter = MovieAdapter(movieList as MutableList<Movie>)
    var myPlace: String? = null
    // code just below put in to allow additions via separate activity
    private val startForResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { activityResult ->
            val data = activityResult.data
            val title = data?.getStringExtra("title") ?: ""
            val year = data?.getStringExtra("year") ?: ""
            val genre = data?.getStringExtra("genre") ?: ""
            val rating = data?.getStringExtra("rating") ?: ""
            movieList.add(Movie(title, year, genre, rating))
            Log.d("RECYCLE", "MAIN ===== TITLE $title ================")
            movieAdapter.notifyDataSetChanged()
        }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val myDir = this.getFilesDir()
        val myDirName = myDir.getAbsolutePath()
        myPlace = myDirName

        // three recyclerview lines below are main steps to
        val recyclerView = findViewById<RecyclerView?>(R.id.recyclerView)
        recyclerView.setLayoutManager(LinearLayoutManager(this))
        // add lines below to allow swipe delete
        val itemTouchHelper = ItemTouchHelper(movieAdapter.swipeToDeleteCallback)
        itemTouchHelper.attachToRecyclerView(recyclerView)
        recyclerView.setAdapter(movieAdapter)

        readFile()

    }

    fun saveList(v : View) {
        writeFile()
    }
    fun startSecond(v : View) {
        startForResult.launch(Intent(this, AddMovieActivity::class.java))
        // line below put in to check remove ability
        //MovieAdapter.removeItem(0)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.xml, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        Log.d("MOVIELIST", "options menu")
        when (item.itemId) {
            R.id.ratingSort -> {
                Log.d("MOVIELIST", "onOptions: rating sort")
                movieList?.sortBy{ it?.rating }
                movieAdapter.notifyDataSetChanged()
            }
            R.id.yearSort -> {
                Log.d("MOVIELIST", "onOptions: year sort")
                movieList?.sortBy{ it?.year }
                movieAdapter.notifyDataSetChanged()
            }
            R.id.genreSort -> {
                Log.d("MOVIELIST", "onOptions: genre sort")
                movieList?.sortBy{ it?.genre }
                movieAdapter.notifyDataSetChanged()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    fun writeFile() {
        Log.d("MOVIELIST", "writeFile() entered")
        try {
            val f = File(myPlace + "/MOVIELIST.csv")
            if (f.exists()) {
                Log.d("MOVIELIST", "File exists at: ${f.absolutePath}")
            } else {
                Log.d("MOVIELIST", "File does not exist. Creating new file.")
            }
            val fw = FileWriter(f, false)  // do not append =- over write
            val count = movieList.size
            Log.d("MOVIELIST", "Count >>>>> =  " + count + ">>>>>>>>>>>>>>>>>>>>>>>>>>")
            // print the list
            for (i in 0 until count) {
                val s = movieList[i]?.convertOut() ?: ""
                Log.d("MOVIELIST", "Writing line: $s")
                fw.write(s + "\n")
            }
            fw.flush()
            fw.close()
        } catch (iox: IOException) {
            Log.d("MOVIELIST", ">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> EXCEP " + iox)
        }
    }

    // FUNCTION readFile - reads the file MOVIELIST.csv - populating full employee list
    fun readFile() {
        Log.d("MOVIELIST", "readFile() entered")
        try {
            val f = File(myPlace + "/MOVIELIST.csv")
            if (f.exists()) {
                Log.d("MOVIELIST", "File exists at: ${f.absolutePath}")
            } else {
                Log.d("MOVIELIST", "File does not exist. Creating new file.")
                f.createNewFile()
            }

            val myReader = Scanner(f)
            while (myReader.hasNextLine()) {
                val data = myReader.nextLine()
                Log.d("MOVIELIST", "LINE of input data: " + data)
                val parts = data.split(",")
                movieList.add(Movie(parts[0], parts[1], parts[2], parts[3]))
            }
            myReader.close()
            movieList.sortByDescending { it?.rating }
            movieAdapter.notifyDataSetChanged()
        } catch (e: IOException) {
            Log.d("READ", "HIT EXCEP >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>" + e)
            println("An error occurred.")
            e.printStackTrace()
        }
    }
}