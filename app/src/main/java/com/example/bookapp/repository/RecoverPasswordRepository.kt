package com.example.bookapp.repository

import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth

interface RecoverPasswordRepository {
    fun getResult(): MutableLiveData<String>
}

class RecoverPasswordRepositoryImpl(private var email: String): RecoverPasswordRepository{
    private lateinit var firebaseAuth: FirebaseAuth
    private var result = MutableLiveData<String>()
    override fun getResult(): MutableLiveData<String> {
        firebaseAuth = FirebaseAuth.getInstance()
        firebaseAuth.sendPasswordResetEmail(email)
            .addOnSuccessListener {
                result.value = "Instructions send to $email"
            }
            .addOnFailureListener { e ->
                result.value = "Failed to send due to ${e.message}"
            }
        return result
    }

}