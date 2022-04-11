package com.example.bookapp.fragments

import android.app.ProgressDialog
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.example.bookapp.MyApplication
import com.example.bookapp.contracts.navigator
import com.example.bookapp.databinding.FragmentPdfDetailBinding
import com.example.bookapp.repository.DownloadBookRepositoryImpl
import com.example.bookapp.repository.LoadBookDetailsRepositoryImpl
import com.example.bookapp.viewModel.DownloadBookViewModel
import com.example.bookapp.ViewModelFactory.DownloadBookViewModelFactory
import com.example.bookapp.viewModel.LoadBookDetailsViewModel
import com.example.bookapp.ViewModelFactory.LoadBookDetailsViewModelFactory

class PdfDetailFragment : Fragment() {

    private lateinit var binding: FragmentPdfDetailBinding
    private lateinit var downloadBookRepositoryImpl: DownloadBookRepositoryImpl
    private lateinit var downloadBookViewModel: DownloadBookViewModel
    private lateinit var loadBookDetailsRepositoryImpl: LoadBookDetailsRepositoryImpl
    private lateinit var loadBookDetailsViewModel: LoadBookDetailsViewModel
    private var bookId = ""
    private var bookTitle = ""
    private var bookUrl = ""

    private lateinit var progressDialog: ProgressDialog

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPdfDetailBinding.inflate(layoutInflater)

        progressDialog = ProgressDialog(requireActivity())
        progressDialog.setTitle("Please wait")
        progressDialog.setCanceledOnTouchOutside(false)

        bookId = this.requireArguments().getString("bookId").toString()

        binding.backBtn.setOnClickListener {
            navigator().goBack()
        }
        binding.downloadBookBtn.setOnClickListener {
            if(!MyApplication.hasConnection(requireActivity())){
                Toast.makeText(requireActivity(), "Not internet connection", Toast.LENGTH_SHORT).show()
            } else {
                if (ContextCompat.checkSelfPermission(
                        requireActivity(),
                        android.Manifest.permission.WRITE_EXTERNAL_STORAGE
                    ) == PackageManager.PERMISSION_GRANTED
                ) {
                    Log.d(TAG, "onCreate: STORAGE PERMISSION is already granted")
                    downloadBook()
                } else {
                    Log.d(TAG, "onCreate: STORAGE PERMISSION was not granted, LETS request it")
                    requestStoragePermissionLauncher.launch(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                }
            }
        }

        // MyApplication.incrementBookViewCount(bookId)

        loadBookDetails()

        return binding.root
    }

    private val requestStoragePermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                Log.d(TAG, "onCreate: STORAGE PERMISSION is granted")
                downloadBook()
            } else {
                Log.d(TAG, "onCreate: STORAGE PERMISSION is denied")
                Toast.makeText(requireActivity(), "PERMISSION denied", Toast.LENGTH_SHORT).show()
            }
        }

    private fun downloadBook() {
        progressDialog.setMessage("Downloading Book")
        progressDialog.show()

        downloadBookRepositoryImpl = DownloadBookRepositoryImpl(bookUrl, bookId)
        downloadBookViewModel = ViewModelProvider(this, DownloadBookViewModelFactory(downloadBookRepositoryImpl)
        )[DownloadBookViewModel::class.java]

        downloadBookViewModel.modelsLiveData.observe(viewLifecycleOwner){
            Toast.makeText(
                requireActivity(),
                it,
                Toast.LENGTH_SHORT
            ).show()
        }

      /*  val storageReference = FirebaseStorage.getInstance().getReferenceFromUrl(bookUrl)
        storageReference.getBytes(Constants.MAX_BYTES_PDF)
            .addOnSuccessListener { bytes ->
                Log.d(TAG, "downloadBook: Book downloaded")
                saveToDownloadsFolder(bytes)
            }
            .addOnFailureListener { e ->
                Log.d(TAG, "downloadBook: Failed to download book due to ${e.message}")
                Toast.makeText(
                    requireActivity(),
                    "Failed to download book due to ${e.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }*/
    }

   /* private fun saveToDownloadsFolder(bytes: ByteArray?) {
        Log.d(TAG, "saveToDownloadsFolder: saving download book")

        val nameWithExtension = "${System.currentTimeMillis()}.pdf"

        try {
            val downloadsFolder =
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
            downloadsFolder.mkdirs() //создание папки, если ее нет

            val filePath = downloadsFolder.path + "/" + nameWithExtension

            val out = FileOutputStream(filePath)
            out.write(bytes)
            out.close()

            Toast.makeText(requireActivity(), "Saved to Downloads Folder", Toast.LENGTH_SHORT)
                .show()
            Log.d(TAG, "downloadBook: saved to Downloads Folder")

            progressDialog.dismiss()
            incrementDownloadCount()
        } catch (e: Exception) {
            progressDialog.dismiss()
            Log.d(TAG, "downloadBook: Failed to save book due to ${e.message}")
            Toast.makeText(
                requireActivity(),
                "Failed to save due to ${e.message}",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun incrementDownloadCount() {
        val ref = FirebaseDatabase.getInstance().getReference("Books")
        ref.child(bookId)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    var downloadsCount = "${snapshot.child("downloadCount").value}"
                    Log.d(TAG, "onDataChange: current downloads count: $downloadsCount")
                    if (downloadsCount == "" || downloadsCount == "null") {
                        downloadsCount = "0"
                    }

                    val newDownloadCount: Int = try {
                        downloadsCount.toInt() + 1
                    } catch (e: Exception) {
                        0
                    }
                    Log.d(TAG, "onDataChange: new downloads count: $newDownloadCount")

                    val hashMap: HashMap<String, Any> = HashMap()
                    hashMap["downloadCount"] = newDownloadCount

                    val dbRef = FirebaseDatabase.getInstance().getReference("Books")
                    dbRef.child(bookId)
                        .updateChildren(hashMap)
                        .addOnSuccessListener {
                            Log.d(TAG, "onDataChange: downloads count incremented")
                        }
                        .addOnFailureListener { e ->
                            Log.d(TAG, "onDataChange: Failed to increment due to ${e.message}")
                        }
                }

                override fun onCancelled(error: DatabaseError) {

                }
            })
    }*/

    private fun loadBookDetails() {
        loadBookDetailsRepositoryImpl = LoadBookDetailsRepositoryImpl(bookId, bookTitle, bookUrl, binding.categoryTv, binding.sizeTv)
        loadBookDetailsViewModel = ViewModelProvider(this, LoadBookDetailsViewModelFactory(loadBookDetailsRepositoryImpl)
        )[LoadBookDetailsViewModel::class.java]

        loadBookDetailsViewModel.modelsLiveData.observe(viewLifecycleOwner){
            binding.titleTv.text = it[0]
            binding.descriptionTv.text = it[1]
            binding.viewsTv.text = it[2]
            binding.downloadsTv.text = it[3]
            binding.dateTv.text = it[4]
        }

      /*  val ref = FirebaseDatabase.getInstance().getReference("Books")
        ref.child(bookId)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val categoryId = "${snapshot.child("categoryId").value}"
                    val description = "${snapshot.child("description").value}"
                    val downloadsCount = "${snapshot.child("downloadCount").value}"
                    val timestamp = "${snapshot.child("timestamp").value}"
                    bookTitle = "${snapshot.child("title").value}"
                    val uid = "${snapshot.child("uid").value}"
                    bookUrl = "${snapshot.child("url").value}"
                    val viewsCount = "${snapshot.child("viewsCount").value}"

                    val date = MyApplication.formatTimeStamp(timestamp.toLong())
                    MyApplication.loadCategory(categoryId, binding.categoryTv)
                    // MyApplication.loadPdfFromUrlSingPage("$url", "$title", binding.pagesTv)
                    MyApplication.loadPdfSize("$bookUrl", "$bookTitle", binding.sizeTv)

                    binding.titleTv.text = bookTitle
                    binding.descriptionTv.text = description
                    binding.viewsTv.text = viewsCount
                    binding.downloadsTv.text = downloadsCount
                    binding.dateTv.text = date
                }

                override fun onCancelled(error: DatabaseError) {

                }
            })*/
    }

    companion object {
        const val TAG = "BOOK_DETAILS_TAG"

        @JvmStatic
        fun newInstance() = PdfDetailFragment()
    }
}