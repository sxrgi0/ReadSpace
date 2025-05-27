package com.example.readspace.utils

import android.content.Context

class SessionManager(context: Context) {

    private val sharedPref = context.getSharedPreferences("ReadSpace_session", Context.MODE_PRIVATE)

    fun setRating(id: Float){
        val editor = sharedPref.edit()
        editor.putFloat("RATING_BOOK", id)
        editor.apply()
    }

    fun getRating(): Float {
        return sharedPref.getFloat("RATING_BOOK", 0F)
    }

}