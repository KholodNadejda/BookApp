package com.example.bookapp.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.bookapp.repository.LoadBookInfoRepository

class LoadBookInfoTitleViewModel(private val reposiroty: LoadBookInfoRepository): ViewModel() {
    var modelsLiveData = MutableLiveData<String>()
    init {
        modelsLiveData = reposiroty.getTitle()
    }
}
class LoadBookInfoDescriptionViewModel(private val reposiroty: LoadBookInfoRepository): ViewModel()  {
    var modelsLiveData = MutableLiveData<String>()
    init {
        modelsLiveData = reposiroty.getDescription()
    }
}
class LoadBookInfoCategoryViewModel(private val reposiroty: LoadBookInfoRepository): ViewModel()  {
    var modelsLiveData = MutableLiveData<String>()
    init {
        modelsLiveData = reposiroty.getCategory()
    }
}