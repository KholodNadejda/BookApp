package com.example.bookapp.repository

import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

interface UpdateUserInfoRepository {
    fun result(): MutableLiveData<String>
}
class UpdateUserInfoRepositoryImpl(email: String, name: String): UpdateUserInfoRepository {
    private var firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private val result = MutableLiveData<String>()
    init {
        val timestamp = System.currentTimeMillis()
        val uid = firebaseAuth.uid.toString()
        val hashMap: HashMap<String, Any?> = HashMap()
        hashMap["uid"] = uid
        hashMap["email"] = email
        hashMap["name"] = name
        hashMap["profileImage"] = ""
        hashMap["userType"] = "user"
        hashMap["timestamp"] = timestamp

        val ref = FirebaseDatabase.getInstance().getReference("User")
        ref.child(uid)
            .setValue(hashMap)
            .addOnSuccessListener {
                result.value = "Success"
            }
            .addOnFailureListener { e ->
                result.value ="Failed saving user info due to ${e.message}"
            }
    }
    override fun result(): MutableLiveData<String> {
        return result
    }
}