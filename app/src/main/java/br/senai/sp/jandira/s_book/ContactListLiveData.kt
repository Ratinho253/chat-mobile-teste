package br.senai.sp.jandira.s_book

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class ContactListLiveData : LiveData<String>() {
    private val _contactList = MutableLiveData<String>()

    fun setContactList(list: String) {
        _contactList.postValue(list) // Use postValue() para atualizar o LiveData na thread principal
    }

    fun getContactList(): LiveData<String> {
        return _contactList
    }
}

