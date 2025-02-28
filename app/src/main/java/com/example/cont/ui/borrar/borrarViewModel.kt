package com.example.cont.ui.borrar

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.cont.DatabaseHelper

class borrarViewModel(application: Application) : AndroidViewModel(application) {
    private val dbHelper = DatabaseHelper(application)

    private val _contacts = MutableLiveData<List<Triple<Long, String, String>>>().apply {
        value = dbHelper.getAllContacts()
    }
    val contacts: LiveData<List<Triple<Long, String, String>>> = _contacts

    fun deleteContact(id: Long) {
        dbHelper.deleteContact(id)
        _contacts.value = dbHelper.getAllContacts()
    }

    fun deleteAllContacts() {
        dbHelper.deleteAllContacts()
        _contacts.value = dbHelper.getAllContacts()
    }

    fun refreshContacts() {
        _contacts.value = dbHelper.getAllContacts()
    }
}