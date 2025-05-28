package com.example.readspace.activities

import android.icu.text.Transliterator.Position
import android.media.Rating
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.drawable.toDrawable
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import com.example.readspace.R
import com.example.readspace.data.Book
import com.example.readspace.data.BookDAO
import com.example.readspace.data.BookEntity
import com.example.readspace.databinding.ActivityBookDetailBinding
import com.example.readspace.utils.BookService
import com.example.readspace.utils.SessionManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class BookDetailActivity : AppCompatActivity() {

    companion object {
        const val BOOK_ID = "BOOK_ID"
    }

    lateinit var binding: ActivityBookDetailBinding

    lateinit var book: Book
    var bookEntity: BookEntity? = null

    //lateinit var status: String

    lateinit var bookDAO: BookDAO

    lateinit var session: SessionManager

    var isRated = false

    var status = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityBookDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        session = SessionManager(this)

        bookDAO = BookDAO(this)

//        isRated = session.getRating() == binding.ratingBar.rating

        val id = intent.getStringExtra(BOOK_ID)!!
        getBookId(id)

        bookEntity = bookDAO.findByApiId(id)

        loadStatus()

        binding.statusButton.setOnClickListener {
            showSaveStatus()
        }

        setSupportActionBar(binding.toolbar)

        supportActionBar?.apply {
            setDisplayShowTitleEnabled(false)
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
        }

        binding.ratingBar.setOnRatingBarChangeListener { ratingBar, rating, fromUser ->
            if (fromUser) {
                session.setRating(bookEntity!!.apiId, rating)
                isRated = true
            }
        }

        binding.statusChip.setOnClickListener {
            deleteStatus()
        }
    }



    override fun onResume() {

        if (bookEntity?.status == "Finished") {
            binding.ratingBar.isVisible = true

            val savedRating = session.getRating(bookEntity!!.apiId)
            binding.ratingBar.rating = savedRating
            isRated = savedRating > 0F

        } else {
            binding.ratingBar.isVisible = false
        }

        super.onResume()
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

    fun getBookId(id: String){
        try {
            CoroutineScope(Dispatchers.IO).launch {
                val service = BookService.getInstance()
                book = service.findBookbyId(id)

                CoroutineScope(Dispatchers.Main).launch {
                    loadData()
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    fun loadData(){
        Picasso.get().load(book.volumeInfo.imageLinks?.thumbnail?.replace("http://", "https://"))
            .placeholder(R.drawable.ic_image_error)
            .into(binding.coverImageView)

        binding.titleTextView.text = book.volumeInfo.title

        if(book.volumeInfo.pageCount != 0){
            binding.pagecountTextView.text = book.volumeInfo.pageCount.toString()
        } else {
            binding.pagecountTextView.text = "Unknown"
        }

        if(book.volumeInfo.authors != null){
            binding.authorTextView.text = book.volumeInfo.authors?.joinToString(", ")
        } else{
            binding.authorTextView.text = "Unknown author"
        }

        if(book.volumeInfo.categories != null){
            binding.categoryTextView.text = book.volumeInfo.categories?.joinToString(", ")
        } else{
            binding.categoryTextView.text = "Unknown"
        }

        if(book.volumeInfo.description != null){
            binding.descriptionTextView.text = Html.fromHtml(book.volumeInfo.description, 0)
        } else{
            binding.descriptionTextView.text = "No data"
        }

        supportActionBar?.apply {
            title = book.volumeInfo.title
            subtitle = book.getAuthors()
            setDisplayShowTitleEnabled(true)
        }
    }

    fun loadStatus() {

        val icon = when(bookEntity?.status){
            "Want to read" -> R.drawable.ic_status_want_to_read
            "Reading" -> R.drawable.ic_status_reading
            "Finished" -> R.drawable.ic_status_finished
            "Not finished" -> R.drawable.ic_status_not_finished
            else -> R.drawable.ic_library_add
        }

        if (bookEntity == null) {
            binding.statusChip.isVisible = false
        } else {
            binding.statusChip.isVisible = true
            binding.statusChip.text = bookEntity!!.status
            binding.statusChip.setChipIconResource(icon)
        }


    }

    fun deleteStatus(){
        MaterialAlertDialogBuilder(this)
            .setTitle("Do you want to delete the book from your library?")
            .setPositiveButton(android.R.string.ok){ dialog, which ->
                if(bookEntity?.status == "Finished"){
                    binding.ratingBar.isVisible = false
                    binding.ratingBar.rating = 0F
                }

                session.removeRating(bookEntity!!.apiId)
                bookDAO.delete(bookEntity!!)
                bookEntity = null

                loadStatus()

            }
            .setCancelable(false)
            .setNegativeButton(android.R.string.cancel, null)
            .setIcon(R.drawable.ic_library_add)
            .show()
    }

    fun showSaveStatus(){
        val items = arrayOf("Want to read", "Reading", "Finished", "Not finished")

        var checkedItems = if (bookEntity == null) {
            -1
        } else {
            items.indexOf(bookEntity!!.status)
        }

        MaterialAlertDialogBuilder(this)
            .setTitle("Add to your library")
            .setPositiveButton(android.R.string.ok) { dialog, which ->
                when(checkedItems){
                    2->{
                        // Añadir el libro a Finished en la base de datos
                        status = "Finished"
                        binding.ratingBar.isVisible = true
                    }
                    else -> {
                        status = items[checkedItems]
                        binding.ratingBar.isVisible = false
                    }

                }

                if (bookEntity != null) {
                    bookEntity!!.status = status
                    bookDAO.update(bookEntity!!)
                } else {
                    bookEntity = BookEntity(-1, book.apiId, book.volumeInfo.title, book.getAuthors(), book.volumeInfo.imageLinks?.thumbnail, status, book.volumeInfo.description, book.volumeInfo.averageRating)
                    bookDAO.insert(bookEntity!!)
                }

                loadStatus()

                Snackbar.make(binding.main, "Book saved. Find it in your library!", Snackbar.LENGTH_SHORT).show()

            }
            .setSingleChoiceItems(items, checkedItems){_, selectedItemIndex ->

                checkedItems = selectedItemIndex

            }
            .setCancelable(false)
            .setNegativeButton(android.R.string.cancel, null)
            .setIcon(R.drawable.ic_library_add)
            .show()
    }

}