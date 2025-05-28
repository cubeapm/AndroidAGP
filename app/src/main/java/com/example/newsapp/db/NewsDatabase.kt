package com.example.newsapp.db

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.newsapp.model.FavoriteNews

class NewsDatabase(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "news_database"
        private const val DATABASE_VERSION = 1
        private const val TABLE_FAVORITES = "favorite_news"
        
        private const val COLUMN_URL = "url"
        private const val COLUMN_TITLE = "title"
        private const val COLUMN_DESCRIPTION = "description"
        private const val COLUMN_IMAGE_URL = "image_url"
        private const val COLUMN_SOURCE_NAME = "source_name"
        private const val COLUMN_PUBLISHED_AT = "published_at"
        private const val COLUMN_COMMENT = "comment"
        private const val COLUMN_TIMESTAMP = "timestamp"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createTable = """
            CREATE TABLE $TABLE_FAVORITES (
                $COLUMN_URL TEXT PRIMARY KEY,
                $COLUMN_TITLE TEXT NOT NULL,
                $COLUMN_DESCRIPTION TEXT,
                $COLUMN_IMAGE_URL TEXT,
                $COLUMN_SOURCE_NAME TEXT NOT NULL,
                $COLUMN_PUBLISHED_AT TEXT NOT NULL,
                $COLUMN_COMMENT TEXT,
                $COLUMN_TIMESTAMP INTEGER NOT NULL
            )
        """.trimIndent()
        
        db.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_FAVORITES")
        onCreate(db)
    }

    fun insertFavorite(favoriteNews: FavoriteNews) {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_URL, favoriteNews.url)
            put(COLUMN_TITLE, favoriteNews.title)
            put(COLUMN_DESCRIPTION, favoriteNews.description)
            put(COLUMN_IMAGE_URL, favoriteNews.urlToImage)
            put(COLUMN_SOURCE_NAME, favoriteNews.sourceName)
            put(COLUMN_PUBLISHED_AT, favoriteNews.publishedAt)
            put(COLUMN_COMMENT, favoriteNews.comment)
            put(COLUMN_TIMESTAMP, favoriteNews.timestamp)
        }
        db.insertWithOnConflict(TABLE_FAVORITES, null, values, SQLiteDatabase.CONFLICT_REPLACE)
        db.close()
    }

    fun deleteFavorite(favoriteNews: FavoriteNews) {
        val db = this.writableDatabase
        db.delete(TABLE_FAVORITES, "$COLUMN_URL = ?", arrayOf(favoriteNews.url))
        db.close()
    }

    fun getFavoriteByUrl(url: String): FavoriteNews? {
        val db = this.readableDatabase
        val cursor = db.query(
            TABLE_FAVORITES,
            null,
            "$COLUMN_URL = ?",
            arrayOf(url),
            null,
            null,
            null
        )

        val favorite = if (cursor.moveToFirst()) {
            FavoriteNews(
                url = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_URL)),
                title = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TITLE)),
                description = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DESCRIPTION)),
                urlToImage = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_IMAGE_URL)),
                sourceName = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_SOURCE_NAME)),
                publishedAt = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PUBLISHED_AT)),
                comment = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_COMMENT)),
                timestamp = cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_TIMESTAMP))
            )
        } else null

        cursor.close()
        db.close()
        return favorite
    }

    fun getAllFavorites(): List<FavoriteNews> {
        val favorites = mutableListOf<FavoriteNews>()
        val db = this.readableDatabase
        val cursor = db.query(
            TABLE_FAVORITES,
            null,
            null,
            null,
            null,
            null,
            "$COLUMN_TIMESTAMP DESC"
        )

        while (cursor.moveToNext()) {
            val favorite = FavoriteNews(
                url = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_URL)),
                title = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TITLE)),
                description = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DESCRIPTION)),
                urlToImage = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_IMAGE_URL)),
                sourceName = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_SOURCE_NAME)),
                publishedAt = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PUBLISHED_AT)),
                comment = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_COMMENT)),
                timestamp = cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_TIMESTAMP))
            )
            favorites.add(favorite)
        }

        cursor.close()
        db.close()
        return favorites
    }
} 