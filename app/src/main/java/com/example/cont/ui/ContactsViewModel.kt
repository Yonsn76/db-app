package com.example.cont.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.cont.DatabaseHelper

class ContactsViewModel(application: Application) : AndroidViewModel(application) {
    private val dbHelper = DatabaseHelper(application)
    private val _contacts = MutableLiveData<List<Triple<Long, String, String>>>()
    val contacts: LiveData<List<Triple<Long, String, String>>> = _contacts

    init {
        loadContacts()
    }

    fun loadContacts() {
        _contacts.value = dbHelper.getAllContacts()
    }

    fun addContact(name: String, phoneNumber: String): Long {
        val id = dbHelper.addContact(name, phoneNumber)
        if (id != -1L) {
            loadContacts()
        }
        return id
    }

    fun updateContact(id: Long, name: String, phoneNumber: String): Int {
        val result = dbHelper.updateContact(id, name, phoneNumber)
        if (result > 0) {
            loadContacts()
        }
        return result
    }

    fun deleteContact(id: Long): Int {
        val result = dbHelper.deleteContact(id)
        if (result > 0) {
            loadContacts()
        }
        return result
    }
}