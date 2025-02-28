package com.example.cont

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "ContactsDB"
        private const val TABLE_CONTACTS = "contacts"
        private const val KEY_ID = "id"
        private const val KEY_NAME = "name"
        private const val KEY_PHONE_NUMBER = "phone_number"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val CREATE_CONTACTS_TABLE = ("CREATE TABLE " + TABLE_CONTACTS + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_NAME + " TEXT,"
                + KEY_PHONE_NUMBER + " TEXT" + ")")
        db.execSQL(CREATE_CONTACTS_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_CONTACTS")
        onCreate(db)
    }

    fun addContact(name: String, phoneNumber: String): Long {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(KEY_NAME, name)
        values.put(KEY_PHONE_NUMBER, phoneNumber)
        return db.insert(TABLE_CONTACTS, null, values)
    }

    fun getContact(id: Long): Triple<Long, String, String>? {
        val db = this.readableDatabase
        val cursor = db.query(TABLE_CONTACTS, arrayOf(KEY_ID, KEY_NAME, KEY_PHONE_NUMBER),
            "$KEY_ID=?", arrayOf(id.toString()), null, null, null, null)
        return if (cursor.moveToFirst()) {
            val contactId = cursor.getLong(cursor.getColumnIndex(KEY_ID))
            val name = cursor.getString(cursor.getColumnIndex(KEY_NAME))
            val phoneNumber = cursor.getString(cursor.getColumnIndex(KEY_PHONE_NUMBER))
            cursor.close()
            Triple(contactId, name, phoneNumber)
        } else {
            cursor.close()
            null
        }
    }

    fun getAllContacts(): List<Triple<Long, String, String>> {
        val contactList = mutableListOf<Triple<Long, String, String>>()
        val selectQuery = "SELECT * FROM $TABLE_CONTACTS"
        val db = this.readableDatabase
        val cursor = db.rawQuery(selectQuery, null)
        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getLong(cursor.getColumnIndex(KEY_ID))
                val name = cursor.getString(cursor.getColumnIndex(KEY_NAME))
                val phoneNumber = cursor.getString(cursor.getColumnIndex(KEY_PHONE_NUMBER))
                contactList.add(Triple(id, name, phoneNumber))
            } while (cursor.moveToNext())
        }
        cursor.close()
        return contactList
    }

    fun updateContact(id: Long, name: String, phoneNumber: String): Int {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(KEY_NAME, name)
        values.put(KEY_PHONE_NUMBER, phoneNumber)
        return db.update(TABLE_CONTACTS, values, "$KEY_ID=?", arrayOf(id.toString()))
    }

    fun deleteContact(id: Long): Int {
        val db = this.writableDatabase
        return db.delete(TABLE_CONTACTS, "$KEY_ID=?", arrayOf(id.toString()))
    }

    fun deleteAllContacts(): Int {
        val db = this.writableDatabase
        return db.delete(TABLE_CONTACTS, null, null)
    }

    fun searchContacts(query: String): List<Triple<Long, String, String>> {
        val contactList = mutableListOf<Triple<Long, String, String>>()
        val selectQuery = "SELECT * FROM $TABLE_CONTACTS WHERE $KEY_NAME LIKE '%$query%' OR $KEY_PHONE_NUMBER LIKE '%$query%'"
        val db = this.readableDatabase
        val cursor = db.rawQuery(selectQuery, null)
        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getLong(cursor.getColumnIndex(KEY_ID))
                val name = cursor.getString(cursor.getColumnIndex(KEY_NAME))
                val phoneNumber = cursor.getString(cursor.getColumnIndex(KEY_PHONE_NUMBER))
                contactList.add(Triple(id, name, phoneNumber))
            } while (cursor.moveToNext())
        }
        cursor.close()
        return contactList
    }
}