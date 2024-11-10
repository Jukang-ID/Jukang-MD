package com.example.jukang.view.auth.regis

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class RegisterViewModel:ViewModel() {

    private val _username = MutableLiveData<String>()
    val username: MutableLiveData<String>
        get() = _username

    private val _password = MutableLiveData<String>()
    val password: MutableLiveData<String>
        get() = _password

    private val _email = MutableLiveData<String>()
    val email: MutableLiveData<String>
        get() = _email

    private val _telpon = MutableLiveData<String>()
    val telpon: MutableLiveData<String>
        get() = _telpon

    private val _loading = MutableLiveData<Boolean>()
    val loading: MutableLiveData<Boolean>
        get() = _loading

    fun Register(){

    }

}