package com.example.bookapp.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.bookapp.fragments.DashboardUserFragment
import com.example.bookapp.repository.ViewPagerAdapterRepository

class ViewPagerAdapterViewModel(private var repository: ViewPagerAdapterRepository): ViewModel() {
    var modelsLiveData = MutableLiveData<DashboardUserFragment.ViewPageAdapter>()
    init {
        modelsLiveData = repository.setupWithViewPagerAdapter()
    }
}