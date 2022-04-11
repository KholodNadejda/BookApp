package com.example.bookapp.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.bookapp.PdfListRepository
import com.example.bookapp.model.ModelPdf

class PdfListViewModel(private var repository: PdfListRepository): ViewModel() {
    var modelsLiveData = MutableLiveData<ArrayList<ModelPdf>>()
    init {
        modelsLiveData = repository.getModels()
    }
}
class PdfListAllViewModel(private var repository: PdfListRepository): ViewModel() {
    var modelsLiveData = MutableLiveData<ArrayList<ModelPdf>>()
    init {
        modelsLiveData = repository.getAllModels()
    }
}

class PdfListMostDownloadedViewModel(private var repository: PdfListRepository): ViewModel() {
    var modelsLiveData = MutableLiveData<ArrayList<ModelPdf>>()
    init {
        modelsLiveData = repository.getMostDownloadedModels()
    }
}

