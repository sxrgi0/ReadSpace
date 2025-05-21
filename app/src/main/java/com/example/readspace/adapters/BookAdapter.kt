package com.example.readspace.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.readspace.R
import com.example.readspace.data.Book
import com.example.readspace.databinding.ItemBookBinding
import com.squareup.picasso.Picasso

class BookAdapter(var items: List<Book>, val onItemClick: (position: Int)-> Unit) : RecyclerView.Adapter<BookViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {
        val binding = ItemBookBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BookViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: BookViewHolder, position: Int) {
        val book = items[position]
        holder.render(book)
        holder.itemView.setOnClickListener {
            onItemClick (position)
        }
    }

    fun updateItems(items: List<Book>) {
        this.items = items
        notifyDataSetChanged()
    }
}

class BookViewHolder(val binding : ItemBookBinding) : ViewHolder(binding.root){

    fun render(book: Book){
        binding.titleTextView.text = book.volumeInfo.title

        if(book.volumeInfo.publishedDate != null){
            binding.dateTextView.text = book.volumeInfo.publishedDate
        } else {
            binding.dateTextView.text = "No published date"
        }

        if(book.volumeInfo.pageCount != 0){
            binding.pagesTextView.text = book.volumeInfo.pageCount.toString()
        } else {
            binding.dateTextView.text = "Unknown"
        }

        if(book.volumeInfo.authors != null){
            binding.authorTextView.text = book.volumeInfo.authors?.joinToString(", ")
        } else{
            binding.authorTextView.text = "Unknown author"
        }

        Picasso.get().load(book.volumeInfo.imageLinks?.thumbnail?.replace("http://", "https://"))
            .placeholder(R.drawable.ic_image_error)
            .into(binding.coverImageView)

    }

}