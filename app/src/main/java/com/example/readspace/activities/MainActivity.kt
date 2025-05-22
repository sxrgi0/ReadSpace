package com.example.readspace.activities

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.readspace.data.Book
import com.example.readspace.adapters.BookAdapter
import com.example.readspace.utils.BookService
import com.example.readspace.R
import com.example.readspace.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    lateinit var adapterFiction: BookAdapter
    lateinit var adapterFantasy: BookAdapter
    lateinit var adapterRomance: BookAdapter

    var bookList: List<Book> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

//        adapter = BookAdapter(bookList) {
//            val book = bookList[it]
//
//            val intent = Intent(this, BookDetailActivity::class.java)
//            intent.putExtra(BookDetailActivity.BOOK_ID, book.apiId)
//            startActivity(intent)
//        }

        binding.recyclerView.adapter = adapterFiction
        binding.recyclerView2.adapter = adapterFantasy
        binding.recyclerView3.adapter = adapterRomance
        binding.recyclerView.layoutManager = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
        binding.recyclerView2.layoutManager = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
        binding.recyclerView3.layoutManager = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)

    }

    fun searchBook(query: String){
        try {
            CoroutineScope(Dispatchers.IO).launch {
                val service = BookService.getInstance()
                //val response = service.findBookbyName("$query+inauthor:$query")
                val response = service.findBookbyName(query)
                bookList = response.items

                CoroutineScope(Dispatchers.Main).launch {
                    //adapter.updateItems(bookList)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}