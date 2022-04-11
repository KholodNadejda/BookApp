package com.example.bookapp.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.bookapp.DashboardRepository
import com.example.bookapp.model.ModelCategory

class DashboardViewModel(private val repository: DashboardRepository): ViewModel() {

    var modelsLiveData = MutableLiveData<List<ModelCategory>>()
    init {
        modelsLiveData = repository.getModels()
    }
}


