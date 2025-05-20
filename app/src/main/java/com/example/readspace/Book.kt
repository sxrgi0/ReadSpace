package com.example.readspace


data class BookSearchResponse(val items: List<Book>)

data class Book(
    val id: String,
    val volumeInfo: VolumeInfo
)

data class VolumeInfo(
    val title: String,
    val subtitle: String,
    val authors: List<String>?,
    val publisher: String,
    val publishedDate: String?,
    val description: String,
    val pageCount: Int,
    val categories: List<String>?,
    val imageLinks: ImageLinks?,
    val averageRating: Int?
)

data class ImageLinks(
    val smallThumbnail: String,
    val thumbnail: String
)