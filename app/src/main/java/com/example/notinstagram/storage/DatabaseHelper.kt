package com.example.notinstagram.storage

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.net.Uri
import com.example.notinstagram.content.ImageCard

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        const val DATABASE_VERSION = 1;
        const val DATABASE_NAME = "notinstagram.db"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL("""
            CREATE TABLE imageCards (
                id INTEGER PRIMARY KEY,
                imageUri STRING,
                locationTaken STRING,
                description TEXT
            )
        """.trimIndent())
    }

    fun clearDatabase() {
        this.writableDatabase.execSQL("DELETE FROM imageCards")
    }

    fun insertImageCard(imageCard: ImageCard) {
        val values = ContentValues()
        values.put("imageUri", imageCard.imageReference.toString())
        values.put("locationTaken", imageCard.locationTaken)
        values.put("description", imageCard.description)

        val db = this.writableDatabase
        db.insert("imageCards", null, values)
        db.close()
    }

    fun retrieveImageCards() : ArrayList<ImageCard> {
        val db = this.readableDatabase
        val columns = arrayOf("imageUri", "locationTaken", "description")

        val cursor = db.query(
            "imageCards",
            columns,
            null,
            null,
            null,
            null,
            "id DESC")

        val imageCards = ArrayList<ImageCard>()
        while (cursor.moveToNext()) {
            imageCards.add(
                ImageCard(
                    Uri.parse(cursor.getString(0)),
                    cursor.getString(1),
                    cursor.getString(2)
                )
            )
        }

        cursor.close()
        db.close()
        return imageCards
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
        TODO("Not yet implemented")
    }
}