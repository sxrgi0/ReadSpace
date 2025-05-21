package com.example.readspace.data

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.util.Log
import com.example.readspace.utils.DatabaseManager

class BookDAO(private val context: Context) {

    private lateinit var db: SQLiteDatabase

    private fun open() {
        db = DatabaseManager(context).writableDatabase
    }

    private fun close() {
        db.close()
    }

    // Insertar
    fun insert(book: Book, bookEntity: BookEntity) {
        open()

        try {// Create a new map of values, where column names are the keys
            val values = ContentValues().apply {
                put(BookEntity.COLUMN_NAME_APIID, book.apiId)
                put(BookEntity.COLUMN_NAME_TITLE, book.volumeInfo.title)
                put(BookEntity.COLUMN_NAME_AUTHORS, book.volumeInfo.authors?.joinToString(", "))
                put(BookEntity.COLUMN_NAME_THUMBNAIL, book.volumeInfo.imageLinks?.thumbnail)
                put(BookEntity.COLUMN_NAME_STATUS, bookEntity.status)
            }

            // Insert the new row, returning the primary key value of the new row
            val newRowId = db.insert(BookEntity.TABLE_NAME, null, values)

            Log.i("DATABASE", "Inserted a category with id: $newRowId")
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            close()
        }
    }

    // Actualizar

    fun update(book: Book, bookEntity: BookEntity) {
        open()

        try {// Create a new map of values, where column names are the keys
            val values = ContentValues().apply {
                put(BookEntity.COLUMN_NAME_APIID, book.apiId)
                put(BookEntity.COLUMN_NAME_TITLE, book.volumeInfo.title)
                put(BookEntity.COLUMN_NAME_AUTHORS, book.volumeInfo.authors?.joinToString(", "))
                put(BookEntity.COLUMN_NAME_THUMBNAIL, book.volumeInfo.imageLinks?.thumbnail)
                put(BookEntity.COLUMN_NAME_STATUS, bookEntity.status)
            }

            // Which row to update, based on the id
            val selection = "${BookEntity.COLUMN_NAME_ID} = ${bookEntity.id}"

            val count = db.update(BookEntity.TABLE_NAME, values, selection, null)

            Log.i("DATABASE", "Updated category with id: ${bookEntity.id}")
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            close()
        }
    }

    // Borrar
    fun delete(book: Book, bookEntity: BookEntity) {
        open()

        try {// Define 'where' part of query.
            val selection = "${BookEntity.COLUMN_NAME_ID} = ${bookEntity.id}"

            // Issue SQL statement.
            val deletedRows = db.delete(BookEntity.TABLE_NAME, selection, null)

            Log.i("DATABASE", "Deleted category with id: ${bookEntity.id}")
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            close()
        }
    }

    // Obtener un registro por ID
    fun findById(id: Log): BookEntity? {
        open()

        var bookEntity: BookEntity? = null

        try {// Define a projection that specifies which columns from the database
            // you will actually use after this query.
            val projection = arrayOf(
                BookEntity.COLUMN_NAME_ID,
                BookEntity.COLUMN_NAME_APIID,
                BookEntity.COLUMN_NAME_TITLE,
                BookEntity.COLUMN_NAME_AUTHORS,
                BookEntity.COLUMN_NAME_THUMBNAIL,
                BookEntity.COLUMN_NAME_STATUS
            )

            // Filter results WHERE "id" = 'bookEntity.id'
            val selection = "${BookEntity.COLUMN_NAME_TITLE} = $id"

            val cursor = db.query(
                BookEntity.TABLE_NAME,   // The table to query
                projection,             // The array of columns to return (pass null to get all)
                selection,              // The columns for the WHERE clause
                null,          // The values for the WHERE clause
                null,                   // don't group the rows
                null,                   // don't filter by row groups
                null               // The sort order
            )

            if (cursor.moveToNext()) {
                val id = cursor.getLong(cursor.getColumnIndexOrThrow(BookEntity.COLUMN_NAME_ID))
                val apiId = cursor.getString(cursor.getColumnIndexOrThrow(BookEntity.COLUMN_NAME_APIID))
                val title = cursor.getString(cursor.getColumnIndexOrThrow(BookEntity.COLUMN_NAME_TITLE))
                val authors = cursor.getString(cursor.getColumnIndexOrThrow(BookEntity.COLUMN_NAME_AUTHORS))
                val thumbnail = cursor.getString(cursor.getColumnIndexOrThrow(BookEntity.COLUMN_NAME_THUMBNAIL))
                val status = cursor.getString(cursor.getColumnIndexOrThrow(BookEntity.COLUMN_NAME_STATUS))

                bookEntity = BookEntity(id, apiId, title, authors, thumbnail, status)
            }

            cursor.close()
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            close()
        }

        return bookEntity
    }

    // Obtener todos los registros
    fun findAll(): List<BookEntity> {
        open()

        var bookList: MutableList<BookEntity> = mutableListOf()

        try {// Define a projection that specifies which columns from the database
            // you will actually use after this query.
            val projection = arrayOf(
                BookEntity.COLUMN_NAME_ID,
                BookEntity.COLUMN_NAME_APIID,
                BookEntity.COLUMN_NAME_TITLE,
                BookEntity.COLUMN_NAME_AUTHORS,
                BookEntity.COLUMN_NAME_THUMBNAIL,
                BookEntity.COLUMN_NAME_STATUS
            )

            // Filter results WHERE "id" = 'bookEntity.id'
            val selection = null

            val cursor = db.query(
                BookEntity.TABLE_NAME,   // The table to query
                projection,             // The array of columns to return (pass null to get all)
                selection,              // The columns for the WHERE clause
                null,          // The values for the WHERE clause
                null,                   // don't group the rows
                null,                   // don't filter by row groups
                null               // The sort order
            )

            while (cursor.moveToNext()) {
                val id = cursor.getLong(cursor.getColumnIndexOrThrow(BookEntity.COLUMN_NAME_ID))
                val apiId = cursor.getString(cursor.getColumnIndexOrThrow(BookEntity.COLUMN_NAME_APIID))
                val title = cursor.getString(cursor.getColumnIndexOrThrow(BookEntity.COLUMN_NAME_TITLE))
                val authors = cursor.getString(cursor.getColumnIndexOrThrow(BookEntity.COLUMN_NAME_AUTHORS))
                val thumbnail = cursor.getString(cursor.getColumnIndexOrThrow(BookEntity.COLUMN_NAME_THUMBNAIL))
                val status = cursor.getString(cursor.getColumnIndexOrThrow(BookEntity.COLUMN_NAME_STATUS))

                val bookEntity = BookEntity(id, apiId, title, authors, thumbnail, status)
                bookList.add(bookEntity)
            }

            cursor.close()
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            close()
        }

        return bookList
    }
}