package com.example.bookapp.repository

import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth

interface CreateUserAccountRepository {
    fun result(): MutableLiveData<String>
}
class CreateUserAccountRepositoryImpl(email: String, password: String): CreateUserAccountRepository {
    private val result = MutableLiveData<String>()
    private var firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()

    init {
        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                result.value = "Success"
            }
            .addOnFailureListener { e ->
                result.value ="Failed creating account due to ${e.message}"
            }
    }

    override fun result(): MutableLiveData<String> {
        return result
    }
}