package com.example.bookapp.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.bookapp.repository.LoadBookInfoRepository

class LoadBookInfoTitleViewModel(private val repository: LoadBookInfoRepository): ViewModel() {
    var modelsLiveData = MutableLiveData<String>()
    init {
        modelsLiveData = repository.getTitle()
    }
}
class LoadBookInfoDescriptionViewModel(private val repository: LoadBookInfoRepository): ViewModel()  {
    var modelsLiveData = MutableLiveData<String>()
    init {
        modelsLiveData = repository.getDescription()
    }
}
class LoadBookInfoCategoryViewModel(private val repository: LoadBookInfoRepository): ViewModel()  {
    var modelsLiveData = MutableLiveData<ArrayList<String>>()
    init {
        modelsLiveData = repository.getCategory()
    }
}