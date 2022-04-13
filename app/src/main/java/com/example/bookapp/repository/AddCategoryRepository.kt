package com.example.bookapp

import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

interface AddCategoryRepository {
    fun addCategory(): MutableLiveData<String>
}
class AddCategoryRepositoryImpl(var category: String): AddCategoryRepository {

    private var firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()

    override fun addCategory(): MutableLiveData<String> {
        val timestamp = System.currentTimeMillis()
        val hashMap: HashMap<String, Any?> = HashMap()
        hashMap["id"] = "$timestamp"
        hashMap["category"] = category
        hashMap["timestamp"] = timestamp
        hashMap["uid"] = "${firebaseAuth.uid}"
        val stringOut = MutableLiveData<String>()
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