package com.example.readspace.activities

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.SearchView.OnQueryTextListener
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.readspace.data.Book
import com.example.readspace.adapters.BookAdapter
import com.example.readspace.utils.BookService
import com.example.readspace.R
import com.example.readspace.data.BookEntity
import com.example.readspace.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    val bookAdapterList = listOf(
        BookAdapter(emptyList()) { position -> onBookClicked(position, 0) },
        BookAdapter(emptyList()) { position -> onBookClicked(position, 1) },
        BookAdapter(emptyList()) { position -> onBookClicked(position, 2) },
        BookAdapter(emptyList()) { position -> onBookClicked(position, 3) },
        BookAdapter(emptyList()) { position -> onBookClicked(position, 4) },
        BookAdapter(emptyList()) { position -> onBookClicked(position, 5) },
        BookAdapter(emptyList()) { position -> onBookClicked(position, 6) },
        BookAdapter(emptyList()) { position -> onBookClicked(position, 7) }
    )

    val searchAdapter = BookAdapter(emptyList(), BookAdapter.VIEW_TYPE_DETAIL) { position -> onBookClicked(position) }

    var bookEntity: BookEntity? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        /*ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }*/

        binding.searchView.editText.addTextChangedListener {
            val query = binding.searchView.text.toString()
            if (query.isNotEmpty() && query.length > 7) {
                searchBook(query, searchAdapter)
            }
        }

        setSupportActionBar(binding.toolbar)

        setupRecyclerView()

        searchBook("+subject:Fiction", bookAdapterList[0])
//        searchBook("+subject:Science+fiction", bookAdapterList[1])
//        searchBook("+subject:Fantasy", bookAdapterList[2])
//        searchBook("+subject:Romance", bookAdapterList[3])
//        searchBook("+subject:History", bookAdapterList[4])
//        searchBook("+subject:Thriller", bookAdapterList[5])
//        searchBook("+subject:Mystery", bookAdapterList[6])
//        searchBook("+subject:Horror", bookAdapterList[7])
    }

    /*override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.activity_main_menu, menu)

        val menuItem = menu.findItem(R.id.menu_search)
        val searchView = menuItem.actionView as SearchView

        searchView.setOnQueryTextListener(object : OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }

        })
        return super.onCreateOptionsMenu(menu)
    }*/



    fun searchBook(query: String, adapter: BookAdapter) {
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

    fun onBookClicked(position: Int, adapterIndex: Int) {
        val selectedBook = bookAdapterList[adapterIndex].getItem(position)
        val intent = Intent(this, BookDetailActivity::class.java)
        intent.putExtra(BookDetailActivity.BOOK_ID, selectedBook.apiId)
        startActivity(intent)
    }

    fun onBookClicked(position: Int) {
        val selectedBook = searchAdapter.getItem(position)
        val intent = Intent(this, BookDetailActivity::class.java)
        intent.putExtra(BookDetailActivity.BOOK_ID, selectedBook.apiId)
        startActivity(intent)
    }


    fun setupRecyclerView() {
        val recyclerView = listOf(
            binding.recyclerView,
            binding.recyclerView2,
            binding.recyclerView3,
            binding.recyclerView4,
            binding.recyclerView5,
            binding.recyclerView6,
            binding.recyclerView7,
            binding.recyclerView8
        )

        for (i in recyclerView.indices) {
            recyclerView[i].adapter = bookAdapterList[i]
            recyclerView[i].layoutManager =
                LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
        }

        binding.searchRecyclerView.adapter = searchAdapter
        binding.searchRecyclerView.layoutManager = LinearLayoutManager(this)
    }
}