package com.example.bookapp.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.bookapp.DeleteCategoryRepository

class DeleteCategoryViewModel(private var repository: DeleteCategoryRepository): ViewModel() {
    var modelsLiveData = MutableLiveData<String>()
    init {
        modelsLiveData = repository.deleteCategory()
    }
}