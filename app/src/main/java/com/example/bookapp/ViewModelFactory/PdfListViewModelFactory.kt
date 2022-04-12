package com.example.bookapp.ViewModelFactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.bookapp.PdfListRepository
import com.example.bookapp.viewModel.PdfListAllViewModel
import com.example.bookapp.viewModel.PdfListMostDownloadedViewModel
import com.example.bookapp.viewModel.PdfListViewModel

class PdfListViewModelFactory(private val PdfListRepository: PdfListRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PdfListViewModel::class.java)) {
            return PdfListViewModel(PdfListRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

class PdfListAllViewModelFactory(private val PdfListRepository: PdfListRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PdfListAllViewModel::class.java)) {
            return PdfListAllViewModel(PdfListRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

class PdfListMostDownloadedViewModelFactory(private val PdfListRepository: PdfListRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PdfListMostDownloadedViewModel::class.java)) {
            return PdfListMostDownloadedViewModel(PdfListRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}