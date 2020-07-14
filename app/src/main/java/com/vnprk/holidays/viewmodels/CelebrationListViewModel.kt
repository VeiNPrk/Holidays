package com.vnprk.holidays.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class CelebrationListViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is LIST Fragment"
    }
    val text: LiveData<String> = _text
}