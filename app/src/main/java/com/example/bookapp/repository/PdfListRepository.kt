package com.example.bookapp

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.bookapp.fragments.PdfDetailFragment.Companion.TAG
import com.example.bookapp.fragments.PdfListAdminFragment
import com.example.bookapp.model.ModelPdf
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

interface PdfListRepository {
    fun getModels(): MutableLiveData<ArrayList<ModelPdf>>
    fun getAllModels(): MutableLiveData<ArrayList<ModelPdf>>
    fun getMostDownloadedModels(): MutableLiveData<ArrayList<ModelPdf>>
}

/*class PdfListMostDownloadedRepositoryImpl(): PdfListRepository {
    private val modelsLiveData = MutableLiveData<ArrayList<ModelPdf>>()
    private var pdfArrayList = ArrayList<ModelPdf>()
    val category = "downloadCount"
    init {
        Log.d("PdfListMostDownloaded", "PdfList category downloadCount")
        val ref = FirebaseDatabase.getInstance().getReference("Books")
        ref.orderByChild("downloadCount").limitToLast(10)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    pdfArrayList.clear()
                    for (ds in snapshot.children) {
                        val model = ds.getValue(ModelPdf::class.java)
                        if (model != null) {
                            pdfArrayList.add(model)
                            Log.d("PdfListMostDownloaded", "onDataChange most model: ${model.title} ${model.categoryId}")
                        }
                    }
                    modelsLiveData.value = pdfArrayList
                    Log.d("PdfListMostDownloaded", "onDataChange most modelsLiveData: ${modelsLiveData.value}")
                }
                override fun onCancelled(error: DatabaseError) {}
            })
    }
    override fun getModels(): MutableLiveData<ArrayList<ModelPdf>> {
        Log.d("PdfListMostDownloaded", "onDataChange most getModels: ${modelsLiveData.value}")
        return modelsLiveData
    }
}*/

class PdfListRepositoryImpl(var categoryId: String): PdfListRepository {
    private val modelsLiveData = MutableLiveData<ArrayList<ModelPdf>>()
    private var pdfArrayList = ArrayList<ModelPdf>()
    init {

    }
    override fun getModels(): MutableLiveData<ArrayList<ModelPdf>> {
        val ref = FirebaseDatabase.getInstance().getReference("Books")
        ref.orderByChild("categoryId").equalTo(categoryId)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    pdfArrayList.clear()
                    for (ds in snapshot.children) {
                        val model = ds.getValue(ModelPdf::class.java)
                        if (model != null) {
                            pdfArrayList.add(model)
                            Log.d(TAG, "onDataChange: ${model.title} ${model.categoryId}")
                        }
                    }
                    modelsLiveData.value = pdfArrayList
                }
                override fun onCancelled(error: DatabaseError) {  }
            })
        return modelsLiveData
    }

    override fun getAllModels(): MutableLiveData<ArrayList<ModelPdf>> {
        val ref = FirebaseDatabase.getInstance().getReference("Books")
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                pdfArrayList.clear()
                for (ds in snapshot.children) {
                    val model = ds.getValue(ModelPdf::class.java)
                    pdfArrayList.add(model!!)
                }
                modelsLiveData.value = pdfArrayList
            }
            override fun onCancelled(error: DatabaseError) {}
        })
        return modelsLiveData
    }

    override fun getMostDownloadedModels(): MutableLiveData<ArrayList<ModelPdf>> {
        Log.d("PdfListMostDownloaded", "PdfList category downloadCount")
        val ref = FirebaseDatabase.getInstance().getReference("Books")
        ref.orderByChild("downloadCount").limitToLast(10)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    pdfArrayList.clear()
                    for (ds in snapshot.children) {
                        val model = ds.getValue(ModelPdf::class.java)
                        if (model != null) {
                            pdfArrayList.add(model)
                            Log.d("PdfListMostDownloaded", "onDataChange most model: ${model.title} ${model.categoryId}")
                        }
                    }
                    pdfArrayList.reverse()
                    //Log.d("PdfListMostDownloaded", "onDataChange most model: ${pdfArrayList[0].downloadCount} ${pdfArrayList[1].downloadCount}")
                    modelsLiveData.value = pdfArrayList
                    Log.d("PdfListMostDownloaded", "onDataChange most modelsLiveData: ${modelsLiveData.value}")
                }
                override fun onCancelled(error: DatabaseError) {}
            })
        //Log.d("PdfListMostDownloaded", "onDataChange most getModels: ${modelsLiveData.value}")
        return modelsLiveData
    }
}

/*class PdfListAllRepositoryImpl: PdfListRepository {
    private val modelsLiveData = MutableLiveData<ArrayList<ModelPdf>>()
    private var pdfArrayList = ArrayList<ModelPdf>()
    init {
        val ref = FirebaseDatabase.getInstance().getReference("Books")
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                pdfArrayList.clear()
                for (ds in snapshot.children) {
                    val model = ds.getValue(ModelPdf::class.java)
                    pdfArrayList.add(model!!)
                }
                modelsLiveData.value = pdfArrayList
            }
            override fun onCancelled(error: DatabaseError) {}
        })
    }
    override fun getModels(): MutableLiveData<ArrayList<ModelPdf>> {
        return modelsLiveData
    }
}*/