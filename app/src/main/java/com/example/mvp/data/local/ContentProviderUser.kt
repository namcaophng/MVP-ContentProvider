package com.example.mvp.data.local

import android.content.*
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.database.sqlite.SQLiteQueryBuilder
import android.net.Uri
import android.text.TextUtils

class ContentProviderUser: ContentProvider() {

    companion object{
        val AUTHORITY = "com.example.mvp.data.local.ContentProviderUser"
        val CONTENT_PATH = "backupdata"
        val URL = "content://${AUTHORITY}/${CONTENT_PATH}"
        val CONTENT_URI = Uri.parse(URL)
        val ACCOUNT = "account"
        val PASSWORD = "password"
        val URI_ALL_ITEMS_CODE = 1
        val URI_ONE_ITEM_CODE = 2
    }

    private var USERS_PROJECTION_MAP = HashMap<String, String>()

    val uriMatcher = UriMatcher(UriMatcher.NO_MATCH)


    /////////////////////////////
    //Database
    private lateinit var db: SQLiteDatabase
    val DATABASE_NAME = "AccountDatabase"
    val TABLE_NAME = "User"
    val DATABASE_VERSION = 1

    override fun onCreate(): Boolean {
        uriMatcher.addURI(AUTHORITY, CONTENT_PATH, URI_ALL_ITEMS_CODE)
        uriMatcher.addURI(AUTHORITY, "$CONTENT_PATH/#", URI_ONE_ITEM_CODE)

        if (this.context != null){
            var dbHelper = DatabaseHelper(context!!)
            db = dbHelper.writableDatabase
        }
        return db != null
    }


    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        var rowID = db.insert(TABLE_NAME, null, values)

        var _uri =ContentUris.withAppendedId(CONTENT_URI, rowID)

        return _uri
    }

    override fun query(
        uri: Uri,
        projection: Array<out String>?,
        selection: String?,
        selectionArgs: Array<out String>?,
        sortOrder: String?
    ): Cursor? {
        var qb =SQLiteQueryBuilder()
        qb.tables = TABLE_NAME

        when(uriMatcher.match(uri)){
            URI_ALL_ITEMS_CODE -> qb.projectionMap = USERS_PROJECTION_MAP

            URI_ONE_ITEM_CODE -> qb.appendWhere("$ACCOUNT = ${uri.pathSegments[1]}")
        }

        val c = qb.query(db, projection, selection, selectionArgs, null, null, ACCOUNT)
        c.setNotificationUri(context?.contentResolver, uri)
        return c
    }

    override fun update(
        uri: Uri,
        values: ContentValues?,
        selection: String?,
        selectionArgs: Array<out String>?
    ): Int {
        var count = 0
        when (uriMatcher.match(uri)) {
            URI_ALL_ITEMS_CODE -> count = db.update(TABLE_NAME, values, selection, selectionArgs)

            URI_ONE_ITEM_CODE -> count = db.update(TABLE_NAME, values,
                ACCOUNT + " = " + uri.pathSegments[1] +
                        if (!TextUtils.isEmpty(selection)) "AND ($selection)"
                        else "", selectionArgs)
            else -> throw IllegalArgumentException("Unknown URI $uri")
        }

        context!!.contentResolver.notifyChange(uri, null)
        return count
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<out String>?): Int {
        var count = 0
        when (uriMatcher.match(uri)) {
            URI_ALL_ITEMS_CODE -> count = db.delete(TABLE_NAME, selection, selectionArgs)

            URI_ONE_ITEM_CODE -> {
                val id = uri.pathSegments[1]
                count = db.delete(
                    TABLE_NAME,
                    ACCOUNT + " = " + id +
                            if (!TextUtils.isEmpty(selection)) "AND ($selection)" else "",
                    selectionArgs
                )
            }
            else -> throw IllegalArgumentException("Unknown URI $uri")
        }

        context!!.contentResolver.notifyChange(uri, null)
        return count
    }

    override fun getType(uri: Uri): String? {
        when(uriMatcher.match(uri)){
            URI_ALL_ITEMS_CODE -> return "vnd.android.cursor.dir/vnd.example.backupdata"

            URI_ONE_ITEM_CODE -> return "vnd.android.cursor.item/vnd.example.backupdata"

            else -> throw IllegalArgumentException("Unsupported URI: $uri")
        }
    }
}