package com.example.bookapp

import androidx.lifecycle.MutableLiveData
import com.example.bookapp.model.ModelCategory
import com.google.firebase.database.FirebaseDatabase

interface DeleteCategoryRepository {
    fun deleteCategory(): MutableLiveData<String>
}
class DeleteCategoryRepositoryImpl(model: ModelCategory): DeleteCategoryRepository {
    private val modelsLiveData = MutableLiveData<String>()
    val id = model.id

    override fun deleteCategory(): MutableLiveData<String> {
        val ref = FirebaseDatabase.getInstance().getReference("Categories")
        ref.child(id)
            .removeValue()
            .addOnSuccessListener {
                modelsLiveData.value = "Deleted"
            }
            .addOnFailureListener { e ->
                modelsLiveData.value = "Unable to delete due to ${e.message}."
            }
        return modelsLiveData
    }
}
