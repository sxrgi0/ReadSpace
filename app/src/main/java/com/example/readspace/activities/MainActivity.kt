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

    val bookAdapterList = listOf(
        BookAdapter(emptyList()) { position -> onBookClicked(position, 0)},
        BookAdapter(emptyList()) { position -> onBookClicked(position, 1)},
        BookAdapter(emptyList()) { position -> onBookClicked(position, 2)},
        BookAdapter(emptyList()) { position -> onBookClicked(position, 3)},
        BookAdapter(emptyList()) { position -> onBookClicked(position, 4)},
        BookAdapter(emptyList()) { position -> onBookClicked(position, 5)},
        BookAdapter(emptyList()) { position -> onBookClicked(position, 6)}
    )

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

        binding.recyclerView.adapter = bookAdapterList[0]
        binding.recyclerView2.adapter = bookAdapterList[1]
        binding.recyclerView3.adapter = bookAdapterList[2]
        binding.recyclerView4.adapter = bookAdapterList[3]
        binding.recyclerView5.adapter = bookAdapterList[4]
        binding.recyclerView6.adapter = bookAdapterList[5]
        binding.recyclerView7.adapter = bookAdapterList[6]
        binding.recyclerView.layoutManager = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
        binding.recyclerView2.layoutManager = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
        binding.recyclerView3.layoutManager = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
        binding.recyclerView4.layoutManager = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
        binding.recyclerView5.layoutManager = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
        binding.recyclerView6.layoutManager = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
        binding.recyclerView7.layoutManager = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)


        searchBook("+subject:Fiction", bookAdapterList[0])
        searchBook("+subject:Science+fiction", bookAdapterList[1])
        searchBook("+subject:Fantasy", bookAdapterList[2])
        searchBook("+subject:Romance", bookAdapterList[3])
        searchBook("+subject:History", bookAdapterList[4])
        searchBook("+subject:Thriller", bookAdapterList[5])
        searchBook("+subject:Mystery", bookAdapterList[6])
    }

    fun searchBook(query: String, adapter: BookAdapter){
        try {
            CoroutineScope(Dispatchers.IO).launch {
                val service = BookService.getInstance()
                //val response = service.findBookbyName("$query+inauthor:$query")
                val response = service.findBookbyName(query)
                val categoryList = response.items

                CoroutineScope(Dispatchers.Main).launch {
                    adapter.updateItems(categoryList)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun onBookClicked(position: Int, adapterIndex: Int){
        val selectedBook = bookAdapterList[adapterIndex].getItem(position)
        val intent = Intent(this, BookDetailActivity::class.java)
            intent.putExtra(BookDetailActivity.BOOK_ID, selectedBook.apiId)
        startActivity(intent)
    }
}