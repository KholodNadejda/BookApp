package com.example.bookapp.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

interface UpdatePdfRepository {
    fun updatePdf(): MutableLiveData<String>
}
class UpdatePdfRepositoryImpl(private val bookId: String, private val title: String, private val description: String, private val selectCategoryId: String): UpdatePdfRepository{
    private var result = MutableLiveData<String>()
    private var firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    override fun updatePdf(): MutableLiveData<String> {
        Log.d("UpdatePdfRepository", "updatePdf: Starting updating pdf info...")

        val hashMap = HashMap<String, Any>()
        hashMap["title"] = title
        hashMap["description"] = description
        hashMap["categoryId"] = selectCategoryId
        hashMap["uid"] = "${firebaseAuth.uid}"

        val ref = FirebaseDatabase.getInstance().getReference("Books")
        ref.child(bookId)
            .updateChildren(hashMap)
            .addOnSuccessListener {
                Log.d("UpdatePdfRepository", "updatePdf: Update successful...")
                result.value = "Update successful"
            }
            .addOnFailureListener { e ->
                Log.d("UpdatePdfRepository", "updatePdf: failed to update due to ${e.message}")
                result.value = "Failed to update due to ${e.message}"
            }
        return result
    }
}