package com.example.readspace

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.GridLayoutManager
import com.example.readspace.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    lateinit var adapter: BookAdapter

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

        adapter = BookAdapter(bookList) {
            val book = bookList[it]
        }

        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = GridLayoutManager(this,1)

        searchBook("The")
    }

    fun searchBook(query: String){
        try {
            CoroutineScope(Dispatchers.IO).launch {
                val service = BookService.getInstance()
                //val response = service.findBookbyName("$query+inauthor:$query")
                val response = service.findBookbyName(query)
                bookList = response.items

                CoroutineScope(Dispatchers.Main).launch {
                    adapter.updateItems(bookList)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}