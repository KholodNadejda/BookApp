package com.example.bookapp.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.bookapp.fragments.PdfEditFragment
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

interface LoadBookInfoRepository {
    fun getDescription(): MutableLiveData<String>
    fun getTitle(): MutableLiveData<String>
    fun getCategory(): MutableLiveData<ArrayList<String>>
}
class LoadBookInfoRepositoryImpl(private val bookId: String): LoadBookInfoRepository {
    private var getDescription = MutableLiveData<String>()
    private var getTitle = MutableLiveData<String>()
    private var getCategory = MutableLiveData<ArrayList<String>>()
    init {
        Log.d("LoadBookInfoRepository", "loadBookInfo: Loading bool info")
        val ref = FirebaseDatabase.getInstance().getReference("Books")
        ref.child(bookId)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val selectCategoryId = snapshot.child("categoryId").value.toString()
                    val description = snapshot.child("description").value.toString()
                    val title = snapshot.child("title").value.toString()

                    getTitle.value = title
                    getDescription.value = description
                    val models = ArrayList<String>()
                    Log.d("LoadBookInfoRepository", "onDataChange: loading book category")
                    val refCategory = FirebaseDatabase.getInstance().getReference("Categories")
                    refCategory.child(selectCategoryId)
                        .addListenerForSingleValueEvent(object : ValueEventListener {
                            override fun onDataChange(snapshot: DataSnapshot) {
                                val category = snapshot.child("category").value
                                models.add(category.toString())
                                models.add(selectCategoryId)
                                getCategory.value = models
                                Log.d("LoadBookInfoRepository", " ${getCategory.value?.get(0)}  ${getCategory.value?.get(1)} ")
                                //getCategory.value = category.toString()
                            }
                            override fun onCancelled(error: DatabaseError) { }
                        })
                }
                override fun onCancelled(error: DatabaseError) {  }
            })
    }
    override fun getDescription(): MutableLiveData<String> {
        return getDescription
    }

    override fun getTitle(): MutableLiveData<String> {
        return getTitle
    }

    override fun getCategory(): MutableLiveData<ArrayList<String>> {
        return getCategory
    }

}