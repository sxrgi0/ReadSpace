package com.example.readspace.data

import com.google.gson.annotations.SerializedName


data class BookSearchResponse(val items: List<Book>)

data class Book(
    @SerializedName("id")val apiId: String,
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
    val averageRating: Float?
)

data class ImageLinks(
    val smallThumbnail: String,
    val thumbnail: String
)

data class BookEntity(
    val id: Long,
    val apiId: String,
    val title: String,
    val authors: String,
    val thumbnail: String?,
    val status: String
) {
    companion object{
        const val TABLE_NAME = "Books"
        const val COLUMN_NAME_ID = "id"
        const val COLUMN_NAME_APIID = "apiid"
        const val COLUMN_NAME_TITLE = "title"
        const val COLUMN_NAME_AUTHORS = "authors"
        const val COLUMN_NAME_THUMBNAIL = "thumbnail"
        const val COLUMN_NAME_STATUS = "status"
    }
}