package com.example.bookapp.repository

import android.util.Log
import android.widget.TextView
import androidx.lifecycle.MutableLiveData
import com.example.bookapp.MyApplication
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

interface LoadBookDetailsRepository {
    fun loadBookDetails(): MutableLiveData<ArrayList<String>>
}

class LoadBookDetailsRepositoryImpl(private var bookId: String, private var categoryTv: TextView, private  var sizeTv: TextView): LoadBookDetailsRepository {
   private val result = MutableLiveData<ArrayList<String>>()
    override fun loadBookDetails(): MutableLiveData<ArrayList<String>> {
        val ref = FirebaseDatabase.getInstance().getReference("Books")
        ref.child(bookId)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val models = ArrayList<String>()

                    val categoryId = "${snapshot.child("categoryId").value}"
                    val description = "${snapshot.child("description").value}"
                    val downloadsCount = "${snapshot.child("downloadCount").value}"
                    val timestamp = "${snapshot.child("timestamp").value}"
                    val bookTitle = "${snapshot.child("title").value}"
                    val uid = "${snapshot.child("uid").value}"
                    val bookUrl = "${snapshot.child("url").value}"
                    val viewsCount = "${snapshot.child("viewsCount").value}"

                    val date = MyApplication.formatTimeStamp(timestamp.toLong())
                    MyApplication.loadCategory(categoryId, categoryTv)
                    MyApplication.loadPdfSize("$bookUrl", sizeTv)

                    models.add(bookTitle)
                    models.add(description)
                    models.add(viewsCount)
                    models.add(downloadsCount)
                    models.add(date)
                    models.add(bookUrl)
                    result.value = models
                    Log.d("loadBookDetails", " ${result.value?.get(0)}  ${result.value?.get(1)}  ${result.value?.get(2)}  ${result.value?.get(3)}  ${result.value?.get(4)} ${result.value?.get(5)}")
                }
                override fun onCancelled(error: DatabaseError) {
                }
            })
        return  result
    }

}