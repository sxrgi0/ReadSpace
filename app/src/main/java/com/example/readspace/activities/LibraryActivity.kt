package com.example.readspace.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.readspace.R
import com.example.readspace.adapters.BookAdapter
import com.example.readspace.adapters.MyBooksAdapter
import com.example.readspace.data.Book
import com.example.readspace.data.BookDAO
import com.example.readspace.data.BookEntity
import com.example.readspace.databinding.ActivityLibraryBinding
import com.google.android.material.tabs.TabLayout
import com.squareup.picasso.Picasso

class LibraryActivity : AppCompatActivity() {

    lateinit var binding: ActivityLibraryBinding

    var myBooksList: List<BookEntity> = emptyList()
    var bookEntity: BookEntity? = null
    lateinit var bookDAO: BookDAO

    val adapter = MyBooksAdapter(emptyList()) { position -> onBookClicked(position) }

    var currentStatus = ""

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

        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(this)

        loadBooks()

        binding.tabBar.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {

            override fun onTabSelected(tab: TabLayout.Tab?) {
                // Handle tab select
                currentStatus = when(binding.tabBar.selectedTabPosition){
                    0 -> { // ALL
                        ""
                    }
                    1 -> { // WANT TO READ
                        "Want to read"
                    }
                    2 -> { // READING
                        "Reading"
                    }
                    3 -> { // FINISHED
                        "Finished"
                    }
                    4 -> { // NOT FINISHED
                        "Not finished"
                    }
                    else -> ""
                }

                loadBooks()
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
                // Handle tab reselect
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                // Handle tab unselect
            }
        })

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

    fun onBookClicked(position: Int) {
        val selectedBook = adapter.items[position]
        val intent = Intent(this, BookDetailActivity::class.java)
        intent.putExtra(BookDetailActivity.BOOK_ID, selectedBook.apiId)
        startActivity(intent)
    }

    private fun loadBooks() {
        val books = if (currentStatus.isEmpty()) {
            bookDAO.findAllWithStatus() // Si no hay estado seleccionado, mostrar todos los libros
        } else {
            bookDAO.findByStatus(currentStatus) // Filtrar por estado
        }

        // Actualizar el adaptador con la lista de libros obtenida
        adapter.items = books
        adapter.notifyDataSetChanged()
    }

}