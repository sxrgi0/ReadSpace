package com.example.readspace.activities

import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.readspace.R
import com.example.readspace.data.Book
import com.example.readspace.data.BookDAO
import com.example.readspace.data.BookEntity
import com.example.readspace.databinding.ActivityLibraryBinding
import com.squareup.picasso.Picasso

class LibraryActivity : AppCompatActivity() {

    lateinit var binding: ActivityLibraryBinding

    lateinit var book: Book
    var bookEntity: BookEntity? = null
    lateinit var bookDAO: BookDAO

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityLibraryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        bookDAO = BookDAO(this)

        val wantToReadImages = listOf(binding.wantToReadImageView1, binding.wantToReadImageView2, binding.wantToReadImageView3)
        val readingImages = listOf(binding.readingImageView1, binding.readingImageView2, binding.readingImageView3)
        val finishedImages = listOf(binding.finishedImageView1, binding.finishedImageView2, binding.finishedImageView3)
        val notFinished = listOf(binding.notFinishedImageView1, binding.notFinishedImageView2, binding.notFinishedImageView3)
        loadCovers("Want to read", wantToReadImages)
        loadCovers("Reading", readingImages)
        loadCovers("Finished", finishedImages)
        loadCovers("Not finished", notFinished)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
        }

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            android.R.id.home -> {
                finish()
                return true
            } else -> {
            return super.onOptionsItemSelected(item)
        }
        }

    }

    fun loadCovers(status: String, imageViews: List<ImageView>){
        val allBooks = bookDAO.findByStatus(status).take(3)

//        val allBooks = bookDAO.findAll()

        for(i in imageViews.indices){
            if(i < allBooks.size){
                val image = allBooks[i]?.thumbnail?.replace("http://", "https://")

                Picasso.get().load(image).into(imageViews[i])
            } else {
                imageViews[i].setImageResource(R.drawable.ic_image_error)
            }
        }



        for (book in allBooks) {
            Log.i("COVER", "Book: ${book?.title}, status: ${book?.status}, thumbnail: ${book?.thumbnail}")
        }
    }
}