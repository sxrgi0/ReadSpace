package com.example.readspace.utils

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.readspace.data.BookEntity

class DatabaseManager(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        const val DATABASE_VERSION = 1
        const val DATABASE_NAME = "ReadSpace.db"

        private const val SQL_CREATE_BOOK =
            "CREATE TABLE ${BookEntity.TABLE_NAME} (" +
                    "${BookEntity.COLUMN_NAME_ID} INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "${BookEntity.COLUMN_NAME_APIID} TEXT, " +
                    "${BookEntity.COLUMN_NAME_TITLE} TEXT, " +
                    "${BookEntity.COLUMN_NAME_AUTHORS} TEXT, " +
                    "${BookEntity.COLUMN_NAME_THUMBNAIL} TEXT, " +
                    "${BookEntity.COLUMN_NAME_STATUS} TEXT)"

        private const val SQL_DELETE_BOOK = "DROP TABLE IF EXISTS ${BookEntity.TABLE_NAME}"
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(SQL_CREATE_BOOK)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        onDestroy(db)
        onCreate(db)
    }

    private fun onDestroy(db: SQLiteDatabase){
        db.execSQL(SQL_DELETE_BOOK)
    }
}