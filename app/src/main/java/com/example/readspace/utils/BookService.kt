package com.example.readspace.utils

import com.example.readspace.data.Book
import com.example.readspace.data.BookSearchResponse
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface BookService {

    @GET("volumes")
    suspend fun findBookbyName(
        @Query("q") name: String,
        @Query("key") apiKey: String = "AIzaSyA0XN-NMA5ffvG-1pooFfvGPNkyN7eVsJM",
        @Query("maxResults") maxResults: Int = 40
        ): BookSearchResponse

    @GET("volumes/{id}")
    suspend fun findBookbyId(
        @Path("id") id: String,
        @Query("key") apiKey: String = "AIzaSyA0XN-NMA5ffvG-1pooFfvGPNkyN7eVsJM"
    ) :Book



    companion object{
        fun getInstance(): BookService {

            val retrofit = Retrofit.Builder()
                .baseUrl("https://www.googleapis.com/books/v1/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

            return retrofit.create(BookService::class.java)

        }
    }
}