package com.example.bookapp.repository

import android.os.Environment
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.bookapp.Constants
import com.example.bookapp.fragments.PdfDetailFragment
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import java.io.FileOutputStream
import java.lang.Exception

interface DownloadBookRepository {
    fun downloadBook(): MutableLiveData<String>
}
class DownloadBookRepositoryImpl(private var bookUrl: String,  private var bookId: String): DownloadBookRepository{

    override fun downloadBook(): MutableLiveData<String> {
        val result = MutableLiveData<String>()
        val storageReference = FirebaseStorage.getInstance().getReferenceFromUrl(bookUrl)
        storageReference.getBytes(Constants.MAX_BYTES_PDF)
            .addOnSuccessListener { bytes ->
                Log.d("DownloadBook", "downloadBook: Book downloaded")
                saveToDownloadsFolder(bytes, result)
            }
            .addOnFailureListener { e ->
                Log.d("DownloadBook", "downloadBook: Failed to download book due to ${e.message}")
                result.value = "Failed to download book due to ${e.message}"
            }
        return result
    }
    private fun saveToDownloadsFolder(bytes: ByteArray?, result: MutableLiveData<String>) {
        Log.d("DownloadBook", "saveToDownloadsFolder: saving download book")

        val nameWithExtension = "${System.currentTimeMillis()}.pdf"

        try {
            val downloadsFolder =
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
            downloadsFolder.mkdirs() //создание папки, если ее нет

            val filePath = downloadsFolder.path + "/" + nameWithExtension

            val out = FileOutputStream(filePath)
            out.write(bytes)
            out.close()

            result.value = "Saved to Downloads Folder"
            Log.d("DownloadBook", "downloadBook: saved to Downloads Folder")

            incrementDownloadCount()
        } catch (e: Exception) {
            Log.d("DownloadBook", "downloadBook: Failed to save book due to ${e.message}")
            result.value = "downloadBook: Failed to save book due to ${e.message}"
        }
    }
    private fun incrementDownloadCount() {
        val ref = FirebaseDatabase.getInstance().getReference("Books")
        ref.child(bookId)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    var downloadsCount = "${snapshot.child("downloadCount").value}"
                    Log.d("DownloadBook", "onDataChange: current downloads count: $downloadsCount")
                    if (downloadsCount == "" || downloadsCount == "null") {
                        downloadsCount = "0"
                    }

                    val newDownloadCount: Int = try {
                        downloadsCount.toInt() + 1
                    } catch (e: Exception) {
                        0
                    }
                    Log.d("DownloadBook", "onDataChange: new downloads count: $newDownloadCount")

                    val hashMap: HashMap<String, Any> = HashMap()
                    hashMap["downloadCount"] = newDownloadCount

                    val dbRef = FirebaseDatabase.getInstance().getReference("Books")
                    dbRef.child(bookId)
                        .updateChildren(hashMap)
                        .addOnSuccessListener {
                            Log.d("DownloadBook", "onDataChange: downloads count incremented")
                        }
                        .addOnFailureListener { e ->
                            Log.d("DownloadBook", "onDataChange: Failed to increment due to ${e.message}")
                        }
                }
                override fun onCancelled(error: DatabaseError) {  }
            })
    }
}