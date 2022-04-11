package com.example.bookapp.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.bookapp.contracts.navigator
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

interface CheckUserRepository {
    fun checkUser(): MutableLiveData<String>
    fun checkUserName(): MutableLiveData<String>
    fun logOut(): MutableLiveData<Boolean>
}


class CheckUserRepositoryImpl: CheckUserRepository {
    private var userType = MutableLiveData<String>()
    private var userName = MutableLiveData<String>()
    private var userLogout = MutableLiveData<Boolean>()
    private var firebaseAuth: FirebaseAuth
    init {
        firebaseAuth = FirebaseAuth.getInstance()
    }
    override fun checkUser(): MutableLiveData<String> {
        Log.d("TAG11", "checkUser impl")
        val firebaseUser = firebaseAuth.currentUser
        Log.d("TAG11", "checkUser impl ${firebaseUser.toString()}")
        if (firebaseUser !== null) {
            val ref = FirebaseDatabase.getInstance().getReference("User")
            ref.child(firebaseUser.uid)
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onCancelled(error: DatabaseError) { }
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val type = snapshot.child("userType").value
                        Log.d("TAG11", "checkUser impl userType ${type.toString()}")
                        userType.value = type.toString()
                    }
                })
            ref.keepSynced(true)
        }
        return userType
    }

    override fun checkUserName(): MutableLiveData<String> {
        val ref = FirebaseDatabase.getInstance().reference
        val firebaseUser = firebaseAuth.currentUser
        if (firebaseUser == null) {
            userName.value = "No user"
        } else {
            userName.value = firebaseUser.email
        }
        ref.keepSynced(true)
        return userName
    }

    override fun logOut(): MutableLiveData<Boolean> {
        try {
            firebaseAuth.signOut()
            userLogout.value = true
        } catch (e: Exception) {
            userLogout.value = false
        }
        return userLogout
    }
}