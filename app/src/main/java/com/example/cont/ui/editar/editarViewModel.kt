package com.example.cont.ui.editar

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.cont.DatabaseHelper

class editarViewModel(application: Application) : AndroidViewModel(application) {
    private val dbHelper = DatabaseHelper(application)
    private val _contacts = MutableLiveData<List<Triple<Long, String, String>>>()
    val contacts: LiveData<List<Triple<Long, String, String>>> = _contacts

    init {
        loadContacts()
    }

    private fun loadContacts() {
        _contacts.value = dbHelper.getAllContacts()
    }

    fun updateContact(id: Long, name: String, phoneNumber: String) {
        dbHelper.updateContact(id, name, phoneNumber)
        loadContacts()
    }
}