package com.vnprk.holidays.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class PrivateListViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is PRIVATE LIST Fragment"
    }
    val text: LiveData<String> = _text
}