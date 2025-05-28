package com.example.readspace.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.readspace.R
import com.example.readspace.data.Book
import com.example.readspace.data.BookDAO
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
           const val VIEW_TYPE_DETAIL_V2 = 2
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
//        val book = items[position]
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
            loadStatus(book.apiId)
        }

        fun loadStatus(id: String){
            val context = itemView.context
            val book = BookDAO(context).findByApiId(id)
            if (book != null) {
                val icon = when(book.status) {
                    "Finished" -> R.drawable.ic_status_finished
                    "Reading" -> R.drawable.ic_status_reading
                    "Want to read" -> R.drawable.ic_status_want_to_read
                    "Not finished" -> R.drawable.ic_status_not_finished
                    else -> R.drawable.ic_library_add
                }
                binding.statusChip.isVisible = true
                binding.statusChip.setChipIconResource(icon)
            } else {
                binding.statusChip.isVisible = false
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

    inner class BookDetail2ViewHolder(val binding: ItemBookDetailBinding) : ViewHolder(binding.root) {

        fun render(book: BookEntity) {

//            if(book.volumeInfo.description != null){
//                binding.descriptionTextView.text = book.volumeInfo.description
//            } else{
//                binding.descriptionTextView.text = "No description"
//            }

            if(book.title!= null){
                binding.titleTextView.text = book.title
            } else{
                binding.titleTextView.text = "No data"
            }

            if(book.authors != null){
                binding.authorsTextView.text = book.authors
            } else{
                binding.authorsTextView.text = "Unknown"
            }

//            if (book.averageRating != null){
//                binding.ratingBar.rating = book.volumeInfo.averageRating
//                binding.ratingTextView.text = "${book.volumeInfo.averageRating}/5"
//            } else {
//                binding.ratingBar.rating = 0F
//                binding.ratingTextView.text = "0/5"
//            }

            if (book.thumbnail != null) {
                Picasso.get()
                    .load(book.thumbnail.replace("http://", "https://"))
                    .into(binding.coverImageView)
            } else {
                Picasso.get()
                    .load(book.thumbnail?.replace("http://", "https://"))
                    .placeholder(R.drawable.ic_image_error)
                    .into(binding.coverImageView)

            }



        }

    }

}

