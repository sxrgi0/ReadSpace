package com.example.readspace.activities

import android.os.Bundle
import android.text.Html
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.readspace.R
import com.example.readspace.data.Book
import com.example.readspace.databinding.ActivityBookDetailBinding
import com.example.readspace.utils.BookService
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

        val id = intent.getStringExtra(BOOK_ID)!!
        getBookId(id)

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
    }
}