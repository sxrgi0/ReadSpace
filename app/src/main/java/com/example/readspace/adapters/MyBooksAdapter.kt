package com.example.readspace.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.readspace.R
import com.example.readspace.adapters.BookAdapter.Companion.VIEW_TYPE_SIMPLE
import com.example.readspace.data.Book
import com.example.readspace.data.BookEntity
import com.example.readspace.databinding.ItemBookDetailBinding
import com.squareup.picasso.Picasso

class MyBooksAdapter(
    var items: List<BookEntity>,
    val onItemClick: (position: Int) -> Unit
) : RecyclerView.Adapter<BookViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {
        val binding = ItemBookDetailBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BookViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BookViewHolder, position: Int) {
        val book = items[position]
        holder.render(book)
        holder.itemView.setOnClickListener {
            onItemClick(position)
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }
}

class BookViewHolder(val binding: ItemBookDetailBinding) : ViewHolder(binding.root) {

    fun render(book: BookEntity) {

        if(book.description != null){
            binding.descriptionTextView.text = book.description
        } else{
            binding.descriptionTextView.text = "No description"
        }

        if(book.title != null){
            binding.titleTextView.text = book.title
        } else{
            binding.titleTextView.text = "No data"
        }

        if(book.authors != null){
            binding.authorsTextView.text = book.authors
        } else{
            binding.authorsTextView.text = "Unknown"
        }

        if (book.averageRating != null){
            binding.ratingBar.rating = book.averageRating!!
            binding.ratingTextView.text = "${book.averageRating}/5"
        } else {
            binding.ratingBar.rating = 0F
            binding.ratingTextView.text = "0/5"
        }

        if (book.thumbnail != null) {
            Picasso.get()
                .load(book.thumbnail?.replace("http://", "https://"))
                .into(binding.coverImageView)
        } else {
            Picasso.get()
                .load(book.thumbnail?.replace("http://", "https://"))
                .placeholder(R.drawable.ic_image_error)
                .into(binding.coverImageView)

        }
    }
}