package com.example.bookapp.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.bookapp.fragments.PdfEditFragment
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

interface LoadCategoriesRepository {
    fun loadCategoriesId(): MutableLiveData<ArrayList<String>>
    fun loadCategoriesTitle(): MutableLiveData<ArrayList<String>>
}
class LoadCategoriesRepositoryImpl: LoadCategoriesRepository {
    private val modelsLiveDataId = MutableLiveData<ArrayList<String>>()
    private val modelsLiveDataTitle = MutableLiveData<ArrayList<String>>()
    private var categoryTitleArrayList = ArrayList<String>()
    private var categoryIdArrayList = ArrayList<String>()

    init {
        val ref = FirebaseDatabase.getInstance().getReference("Categories")
        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (ds in snapshot.children) {
                    val id = "${ds.child("id").value}"
                    val category = "${ds.child("category").value}"

                    categoryIdArrayList.add(id)
                    categoryTitleArrayList.add(category)

                    Log.d(PdfEditFragment.TAG, "onDataChange: Category id $id")
                }
                modelsLiveDataId.value = categoryIdArrayList
                modelsLiveDataTitle.value = categoryTitleArrayList
            }
            override fun onCancelled(error: DatabaseError) {}
        })
    }
    override fun loadCategoriesId(): MutableLiveData<ArrayList<String>> {
        return modelsLiveDataId
    }

    override fun loadCategoriesTitle(): MutableLiveData<ArrayList<String>> {
        return modelsLiveDataTitle
    }
}