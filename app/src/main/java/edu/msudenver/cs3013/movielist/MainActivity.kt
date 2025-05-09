package edu.msudenver.cs3013.movielist

import android.content.Intent
import android.os.Bundle
import android.util.Log
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
    val movieList: MutableList<Movie?> =  ArrayList<Movie?>()
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
        val myDir = this.getFilesDir()
        val myDirName = myDir.getAbsolutePath()
        myPlace = myDirName
        //TODO: movieList.add(Movie(CSV PARSING)
        // line below moved to top to make it global
        //val MovieAdapter = MovieAdapter(movieList as MutableList<Movie>)

        // three recyclerview lines below are main steps to
        val recyclerView = findViewById<RecyclerView?>(R.id.recyclerView)
        recyclerView.setLayoutManager(LinearLayoutManager(this))

        // add lines below to allow swipe delete
        val itemTouchHelper = ItemTouchHelper(movieAdapter.swipeToDeleteCallback)
        itemTouchHelper.attachToRecyclerView(recyclerView)
        recyclerView.setAdapter(movieAdapter)


    }
    // function called by button press
    fun saveList(v : View) {
        // code to save list to file
       //TODO: implement saveList
    }

    fun startSecond(v : View) {
        startForResult.launch(Intent(this, AddMovieActivity::class.java))
        // line below put in to check remove ability
        //MovieAdapter.removeItem(0)
    }
    fun writeFile() {
        Log.d("MOVIELIST", "writeFile() entered")
        try {
            val f = File(myPlace + "/MOVIELIST.csv")
            if (f.exists()) {
                Log.d("MOVIELIST", "EXISTS >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>")
            } else {
                Log.d("MOVIELIST", "DOES NOT EXIST >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>")
            }
            val fw = FileWriter(f, false)  // do not append =- over write
            val count = movieList.size
            Log.d("MOVIELIST", "Count >>>>> =  " + count + ">>>>>>>>>>>>>>>>>>>>>>>>>>")
            // print the list
            for (i in 0..<count) {
                val s = movieList[i]?.convertOut() as String?
                Log.d("MOVIELIST", s + ">>>>>>>>>>>>>>>>>>>>>>>>>>")
                fw.write(s + "\n")
            }
            fw.flush()
            fw.close()
        } catch (iox: IOException) {
            Log.d("MOVIELIST", "HIT EXCEP >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>")
            Log.d("MOVIELIST", ">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> EXCEP " + iox)
        }
    }
    // FUNCTION readFile - reads the file MOVIELIST.csv - populating full employee list
    fun readFile() {
        Log.d("MOVIELIST", "readFile() entered")
        try {
            val f = File(myPlace + "/MOVIELIST.csv")
            f.createNewFile()
            //Log.d("MOVIELIST", data);
            val myReader = Scanner(f)
            while (myReader.hasNextLine()) {
                val data = myReader.nextLine()
                Log.d("MOVIELIST", "LINE of input data: " + data)
                //println(data)
                val parts = data.split(",")
                if (parts.size == 4) {
                    movieList.add(Movie(parts[0], parts[1], parts[2], parts[3]))
                } else {
                    Log.d("MOVIELIST", "Invalid line format: $data")
                }
            }
            myReader.close()
            movieAdapter.notifyDataSetChanged()
        } catch (e: IOException) {
            Log.d("READ", "HIT EXCEP >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>" + e)
            println("An error occurred.")
            e.printStackTrace()
        }
    }
}