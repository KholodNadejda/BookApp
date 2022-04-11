package com.example.bookapp.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.bookapp.repository.CheckUserRepository

class CheckUserViewModel(private val repository: CheckUserRepository): ViewModel(){
    var modelsLiveData = MutableLiveData<String>()
    init {
        modelsLiveData = repository.checkUser()
    }
}

class CheckUserNameViewModel(private val repository: CheckUserRepository): ViewModel(){
    var modelsLiveData = MutableLiveData<String>()
    init {
        modelsLiveData = repository.checkUserName()
    }
}

class LogoutUserViewModel(private val repository: CheckUserRepository): ViewModel(){
    var modelsLiveData = MutableLiveData<Boolean>()
    init {
        modelsLiveData = repository.logOut()
    }
}