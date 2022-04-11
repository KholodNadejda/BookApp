package com.example.bookapp

import android.app.ProgressDialog
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

interface AddCategoryRepository {
    fun addCategory(): MutableLiveData<String>
}
class AddCategoryRepositoryImpl(var category: String): AddCategoryRepository {

    private var firebaseAuth: FirebaseAuth
    private var stringOut = MutableLiveData<String>()
    init {
        firebaseAuth = FirebaseAuth.getInstance()
    }

    override fun addCategory(): MutableLiveData<String> {
        val timestamp = System.currentTimeMillis()
        val hashMap: HashMap<String, Any?> = HashMap()
        hashMap["id"] = "$timestamp"
        hashMap["category"] = category
        hashMap["timestamp"] = timestamp
        hashMap["uid"] = "${firebaseAuth.uid}"

        val ref = FirebaseDatabase.getInstance().getReference("Categories")
        ref.child("$timestamp")
            .setValue(hashMap)
            .addOnSuccessListener {
                stringOut.value = "Add successfully"
            }
            .addOnFailureListener { e ->
                stringOut.value = "Failed to add due to ${e.message}"
            }
        return stringOut
    }
}