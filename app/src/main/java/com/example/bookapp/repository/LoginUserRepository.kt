package com.example.bookapp.repository

import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

interface LoginUserRepository {
    fun checkUser(): MutableLiveData<String>
}
class LoginUserRepositoryImpl(private val email: String, private val password: String): LoginUserRepository{
    private var firebaseAuth: FirebaseAuth
    private var result = MutableLiveData<String>()
    init {
        firebaseAuth = FirebaseAuth.getInstance()
    }
    override fun checkUser(): MutableLiveData<String> {
        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                resultUser()
            }
            .addOnFailureListener { e ->
                result.value = "Login failed due to ${e.message}"
            }
        return result
    }
    private fun resultUser(): MutableLiveData<String> {
        val firebaseUser = firebaseAuth.currentUser!!
        val ref = FirebaseDatabase.getInstance().getReference("User")
        ref.child(firebaseUser.uid)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(error: DatabaseError) { }

                override fun onDataChange(snapshot: DataSnapshot) {
                    val userType = snapshot.child("userType").value
                    if (userType == "user") {
                        result.value = "user"
                    } else if (userType == "admin") {
                        result.value = "admin"
                    }
                }
            })
        return result
    }

}