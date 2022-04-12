package com.example.bookapp.repository

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage

interface UploadPdfToStorageRepository {
    fun uploadPdfToStorage(): MutableLiveData<String>
}

class UploadPdfToStorageRepositoryImpl(var pdfUri: Uri?, var title: String, var description: String, var category: String, var selectedCategoryId: String):UploadPdfToStorageRepository{
    private var firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private var result = MutableLiveData<String>()

    override fun uploadPdfToStorage(): MutableLiveData<String> {
        val timestamp = System.currentTimeMillis()
        val filePathAndName = "Books/$timestamp"
        val storageReference = FirebaseStorage.getInstance().getReference(filePathAndName)
        storageReference.putFile(pdfUri!!)
            .addOnSuccessListener { taskSnapshot ->
                val uriTask: Task<Uri> = taskSnapshot.storage.downloadUrl
                while (!uriTask.isSuccessful);
                val uploadedPdfUri = "${uriTask.result}"
                uploadPdfInfoToDb(uploadedPdfUri, timestamp)
            }
            .addOnFailureListener { e ->
                result.value = "Failed to upload due to ${e.message}"
            }
        return result
    }

    private fun uploadPdfInfoToDb(uploadedPdfUri: String, timestamp: Long) {
        val uid = firebaseAuth.uid
        val hashMap: HashMap<String, Any> = HashMap()
        hashMap["uid"] = "$uid"
        hashMap["id"] = "$timestamp"
        hashMap["title"] = title
        hashMap["description"] = description
        hashMap["categoryId"] = selectedCategoryId
        hashMap["url"] = uploadedPdfUri
        hashMap["timestamp"] = timestamp
        hashMap["viewsCount"] = 0
        hashMap["downloadCount"] = 0

        val ref = FirebaseDatabase.getInstance().getReference("Books")
        ref.child("$timestamp")
            .setValue(hashMap)
            .addOnSuccessListener {
                result.value = "Uploaded"
                //pdfUri == null
            }
            .addOnFailureListener { e ->
                result.value = "Failed to upload due to ${e.message}"
            }
    }
}