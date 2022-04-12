package com.example.bookapp

import androidx.lifecycle.MutableLiveData
import com.example.bookapp.model.ModelCategory
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

interface DashboardRepository {
    fun getModels(): MutableLiveData<List<ModelCategory>>
}
class DashboardRepositoryImpl: DashboardRepository {
    private val modelsLiveData = MutableLiveData<List<ModelCategory>>()
    init {
        val ref = FirebaseDatabase.getInstance().getReference("Categories")
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val models = mutableListOf<ModelCategory>()
                for (ds in snapshot.children) {
                    val model = ds.getValue((ModelCategory::class.java))
                    if (model != null) {
                        models.add(model)
                    }
                }
                modelsLiveData.value = models
            }
            override fun onCancelled(error: DatabaseError) {
            }
        })
        ref.keepSynced(true)
    }
    override fun getModels(): MutableLiveData<List<ModelCategory>> {
        return modelsLiveData
    }

}