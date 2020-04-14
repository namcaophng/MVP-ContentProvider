package com.example.mvp.data.local

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.mvp.data.User

class DatabaseHelper(context: Context):
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION){
    private lateinit var db: SQLiteDatabase
    companion object{
        val DATABASE_NAME = "AccountDatabase"
        val TABLE_NAME = "User"
        val DATABASE_VERSION = 1
        val ACCOUNT = "account"
        val PASSWORD = "password"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val CREATE_DB_TABLE = "CREATE TABLE ${TABLE_NAME}( " +
                " ${ContentProviderUser.ACCOUNT} TEXT PRIMARY KEY," +
                " ${ContentProviderUser.PASSWORD} TEXT)"
        db?.execSQL(CREATE_DB_TABLE)

    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS ${TABLE_NAME}")
        onCreate(db)
    }

    fun getCursorAll(): Cursor?{
        val query = "SELECT * FROM $TABLE_NAME"

        val db = this.readableDatabase
        val cursor = db.rawQuery(query, null)
        return cursor
    }

    fun addToDB(user: User){
        db = this.writableDatabase

        var value = ContentValues()
        value.put(ACCOUNT, user.name)
        value.put(PASSWORD, user.pass)

        db.insert(TABLE_NAME, null, value)
        db.close()
    }
}