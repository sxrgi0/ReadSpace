package com.example.readspace.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.readspace.R
import com.example.readspace.data.Book
import com.example.readspace.data.BookEntity
import com.example.readspace.databinding.ItemBookBinding
import com.example.readspace.databinding.ItemBookDetailBinding
import com.squareup.picasso.Picasso

class BookAdapter(
    var items: List<Book>,
    val viewType: Int = VIEW_TYPE_SIMPLE,
    val onItemClick: (position: Int) -> Unit
) : RecyclerView.Adapter<ViewHolder>() {

       companion object {
           const val VIEW_TYPE_SIMPLE = 0
           const val VIEW_TYPE_DETAIL = 1
       }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        if (this.viewType == VIEW_TYPE_SIMPLE) {
            val binding =
                ItemBookBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return BookViewHolder(binding)
        } else {
            val binding =
                ItemBookDetailBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return BookDetailViewHolder(binding)
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val book = items[position]
        if (holder is BookViewHolder) {
            holder.render(book)
        } else if (holder is BookDetailViewHolder) {
            holder.render(book)
        }
        holder.itemView.setOnClickListener {
            onItemClick(position)
        }
    }

    fun getItem(position: Int): Book {
        return items[position]
    }

    fun updateItems(items: List<Book>) {
        this.items = items
        notifyDataSetChanged()
    }


    inner class BookViewHolder(val binding: ItemBookBinding) : ViewHolder(binding.root) {

        fun render(book: Book) {

            if (book.volumeInfo.imageLinks?.thumbnail != null) {
                Picasso.get()
                    .load(book.volumeInfo.imageLinks?.thumbnail?.replace("http://", "https://"))
                    .into(binding.coverImageView)
            } else {
                Picasso.get()
                    .load(book.volumeInfo.imageLinks?.thumbnail?.replace("http://", "https://"))
                    .placeholder(R.drawable.ic_image_error)
                    .into(binding.coverImageView)

            }

        }

    }

    inner class BookDetailViewHolder(val binding: ItemBookDetailBinding) : ViewHolder(binding.root) {

        fun render(book: Book) {


            if(book.volumeInfo.description != null){
                binding.descriptionTextView.text = book.volumeInfo.description
            } else{
                binding.descriptionTextView.text = "No description"
            }

            if(book.volumeInfo.title != null){
                binding.titleTextView.text = book.volumeInfo.title
            } else{
                binding.titleTextView.text = "No data"
            }

            if(book.volumeInfo.authors != null){
                binding.authorsTextView.text = book.getAuthors()
            } else{
                binding.authorsTextView.text = "Unknown"
            }

            if (book.volumeInfo.averageRating != null){
                binding.ratingBar.rating = book.volumeInfo.averageRating
                binding.ratingTextView.text = "${book.volumeInfo.averageRating}/5"
            } else {
                binding.ratingBar.rating = 0F
                binding.ratingTextView.text = "0/5"
            }

            if (book.volumeInfo.imageLinks?.thumbnail != null) {
                Picasso.get()
                    .load(book.volumeInfo.imageLinks?.thumbnail?.replace("http://", "https://"))
                    .into(binding.coverImageView)
            } else {
                Picasso.get()
                    .load(book.volumeInfo.imageLinks?.thumbnail?.replace("http://", "https://"))
                    .placeholder(R.drawable.ic_image_error)
                    .into(binding.coverImageView)

            }



        }

    }
}