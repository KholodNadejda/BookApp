package com.example.bookapp.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.bookapp.repository.LoadCategoriesRepository

class LoadCategoriesIdViewModel(private val repository: LoadCategoriesRepository): ViewModel() {
    var modelsLiveData = MutableLiveData<ArrayList<String>>()
    init {
        modelsLiveData = repository.loadCategoriesId()
    }
}

class LoadCategoriesTitleViewModel(private val repository: LoadCategoriesRepository): ViewModel() {
    var modelsLiveData = MutableLiveData<ArrayList<String>>()
    init {
        modelsLiveData = repository.loadCategoriesTitle()
    }
}