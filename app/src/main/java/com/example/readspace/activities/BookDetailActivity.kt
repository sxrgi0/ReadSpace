package com.example.readspace.activities

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.readspace.R
import com.example.readspace.data.Book
import com.example.readspace.databinding.ActivityBookDetailBinding
import com.example.readspace.utils.BookService
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

    }

    fun getBookId(id: String){
        CoroutineScope(Dispatchers.IO).launch {
            val service = BookService.getInstance()
            //book = service.findBookbyId(id)
        }

    }
}