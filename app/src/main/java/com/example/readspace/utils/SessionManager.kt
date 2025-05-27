package com.example.readspace.utils

import android.content.Context

class SessionManager(context: Context) {

    private val sharedPref = context.getSharedPreferences("ReadSpace_session", Context.MODE_PRIVATE)

    fun setRating(id: String, rating: Float){
        val editor = sharedPref.edit()
        editor.putFloat("RATING_BOOK_$id", rating)
        editor.apply()
    }

    fun getRating(id: String): Float {
        return sharedPref.getFloat("RATING_BOOK_$id", 0F)
    }

    fun removeRating(id: String) {
        val editor = sharedPref.edit()
        editor.remove("RATING_BOOK_$id")
        editor.apply()
    }

}