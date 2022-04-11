package com.example.bookapp.fragments

import android.app.AlertDialog
import android.app.ProgressDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.bookapp.MyApplication
import com.example.bookapp.ViewModelFactory.*
import com.example.bookapp.contracts.navigator
import com.example.bookapp.databinding.FragmentPdfEditBinding
import com.example.bookapp.repository.UpdatePdfRepositoryImpl
import com.example.bookapp.repository.LoadBookInfoRepositoryImpl
import com.example.bookapp.repository.LoadCategoriesRepositoryImpl
import com.example.bookapp.viewModel.*
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class PdfEditFragment : Fragment() {

    private lateinit var binding: FragmentPdfEditBinding

    private var bookId = ""
    private var selectCategoryId = ""
    private var selectCategoryTitle = ""
    private var title = ""
    private var description = ""
    private lateinit var categoryTitleArrayList: ArrayList<String>
    private lateinit var categoryIdArrayList: ArrayList<String>
    private lateinit var updatePdfViewModel: UpdatePdfViewModel
    private lateinit var updatePdfRepositoryImpl: UpdatePdfRepositoryImpl

    private lateinit var loadCategoriesRepositoryImpl: LoadCategoriesRepositoryImpl
    private lateinit var loadCategoriesIdViewModel: LoadCategoriesIdViewModel
    private lateinit var loadCategoriesTitleViewModel: LoadCategoriesTitleViewModel

    private lateinit var loadBookInfoCategoryViewModel: LoadBookInfoCategoryViewModel
    private lateinit var loadBookInfoDescriptionViewModel: LoadBookInfoDescriptionViewModel
    private lateinit var loadBookInfoTitleViewModel: LoadBookInfoTitleViewModel
    private lateinit var loadBookInfoRepositoryImpl: LoadBookInfoRepositoryImpl

    private lateinit var processDialog: ProgressDialog

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentPdfEditBinding.inflate(layoutInflater)

        bookId = this.requireArguments().getString("bookId").toString()

        processDialog = ProgressDialog(requireActivity())
        processDialog.setTitle("Please wait")
        processDialog.setCanceledOnTouchOutside(false)

        binding.backBtn.setOnClickListener {
            navigator().goBack()
        }

        binding.categoryTv.setOnClickListener {
            categoryDialog()
        }

        binding.submitBtn.setOnClickListener {
            if(!MyApplication.hasConnection(requireActivity())){
                Toast.makeText(requireActivity(), "Not internet connection", Toast.LENGTH_SHORT).show()
            } else {
                validateData()
            }
        }

        loadCategories()
        loadBookInfo()

        return binding.root
    }

    private fun loadBookInfo() {
        Log.d(TAG, "loadBookInfo: Loading bool info")

        loadBookInfoRepositoryImpl = LoadBookInfoRepositoryImpl(bookId)

        loadBookInfoTitleViewModel = ViewModelProvider(this, LoadBookInfoTitleViewModelFactory(loadBookInfoRepositoryImpl)
        )[LoadBookInfoTitleViewModel::class.java]
        loadBookInfoDescriptionViewModel = ViewModelProvider(this, LoadBookInfoDescriptionViewModelFactory(loadBookInfoRepositoryImpl)
        )[LoadBookInfoDescriptionViewModel::class.java]
        loadBookInfoCategoryViewModel = ViewModelProvider(this, LoadBookInfoCategoryViewModelFactory(loadBookInfoRepositoryImpl)
        )[LoadBookInfoCategoryViewModel::class.java]

        loadBookInfoTitleViewModel.modelsLiveData.observe(viewLifecycleOwner){
            binding.titleEt.setText(it)
        }
        loadBookInfoDescriptionViewModel.modelsLiveData.observe(viewLifecycleOwner){
            binding.descriptionEt.setText(it)
        }
        loadBookInfoCategoryViewModel.modelsLiveData.observe(viewLifecycleOwner){
            binding.categoryTv.text = it
        }

       /* val ref = FirebaseDatabase.getInstance().getReference("Books")
        ref.child(bookId)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    selectCategoryId = snapshot.child("categoryId").value.toString()
                    val description = snapshot.child("description").value.toString()
                    val title = snapshot.child("title").value.toString()

                    binding.titleEt.setText(title)
                    binding.descriptionEt.setText(description)

                    Log.d(TAG, "onDataChange: loading book category")
                    val refCategory = FirebaseDatabase.getInstance().getReference("Categories")
                    refCategory.child(selectCategoryId)
                        .addListenerForSingleValueEvent(object : ValueEventListener {
                            override fun onDataChange(snapshot: DataSnapshot) {
                                val category = snapshot.child("category").value
                                binding.categoryTv.text = category.toString()
                            }
                            override fun onCancelled(error: DatabaseError) { }
                        })
                }
                override fun onCancelled(error: DatabaseError) {  }
            })*/
    }

    private fun validateData() {
        title = binding.titleEt.text.toString().trim()
        description = binding.descriptionEt.text.toString().trim()

        when {
            title.isEmpty() -> {
                Toast.makeText(requireActivity(), "Enter title", Toast.LENGTH_SHORT).show()
            }
            description.isEmpty() -> {
                Toast.makeText(requireActivity(), "Enter description", Toast.LENGTH_SHORT).show()
            }
            selectCategoryId.isEmpty() -> {
                Toast.makeText(requireActivity(), "Pick Category", Toast.LENGTH_SHORT).show()
            }
            else -> {
                updatePdf()
            }
        }
    }

    private fun updatePdf() {
        processDialog.setMessage("Updating book info")
        processDialog.show()

        updatePdfRepositoryImpl = UpdatePdfRepositoryImpl(bookId, title, description, selectCategoryId)
        updatePdfViewModel = ViewModelProvider(this, UpdatePdfViewModelFactory(updatePdfRepositoryImpl)
        )[UpdatePdfViewModel::class.java]

        updatePdfViewModel.modelsLiveData.observe(viewLifecycleOwner){
            processDialog.dismiss()
            Toast.makeText(
                requireActivity(),
                it,
                Toast.LENGTH_SHORT
            ).show()
        }

      /*Log.d(TAG, "updatePdf: Starting updating pdf info...")
        val hashMap = HashMap<String, Any>()
        hashMap["title"] = "$title"
        hashMap["description"] = "$description"
        hashMap["categoryId"] = "$selectCategoryId"

        val ref = FirebaseDatabase.getInstance().getReference("Books")
        ref.child(bookId)
            .updateChildren(hashMap)
            .addOnSuccessListener {
                processDialog.dismiss()
                Log.d(TAG, "updatePdf: Update successful...")
                Toast.makeText(requireActivity(), "Update successful", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                processDialog.dismiss()
                Log.d(TAG, "updatePdf: failed to update due to ${e.message}")
                Toast.makeText(
                    requireActivity(),
                    "Failed to update due to ${e.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }*/
    }

    private fun categoryDialog() {
        // val categoriesArray = categoryTitleArrayList
        val categoriesArray = arrayOfNulls<String>(categoryTitleArrayList.size)
        for (i in categoryTitleArrayList.indices) {
            categoriesArray[i] = categoryTitleArrayList[i]
        }

        val builder = AlertDialog.Builder(requireActivity())
        builder.setTitle("Choose Category")
            .setItems(categoriesArray) { dialopg, position ->
                selectCategoryId = categoryIdArrayList[position]
                selectCategoryTitle = categoryTitleArrayList[position]

                binding.categoryTv.text = selectCategoryTitle
            }
            .show()
    }

    private fun loadCategories() {
        //Log.d(TAG, "loadCategories: loading Categories...")
        categoryTitleArrayList = ArrayList()
        categoryIdArrayList = ArrayList()

        loadCategoriesRepositoryImpl = LoadCategoriesRepositoryImpl()
        loadCategoriesIdViewModel = ViewModelProvider(this, LoadCategoriesIdViewModelFactory(loadCategoriesRepositoryImpl)
        )[LoadCategoriesIdViewModel::class.java]
        loadCategoriesTitleViewModel = ViewModelProvider(this, LoadCategoriesTitleViewModelFactory(loadCategoriesRepositoryImpl)
        )[LoadCategoriesTitleViewModel::class.java]

        loadCategoriesIdViewModel.modelsLiveData.observe(viewLifecycleOwner){
            categoryIdArrayList = it
        }
        loadCategoriesTitleViewModel.modelsLiveData.observe(viewLifecycleOwner){
            categoryTitleArrayList = it
        }
       /* val ref = FirebaseDatabase.getInstance().getReference("Categories")
        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                categoryIdArrayList.clear()
                categoryTitleArrayList.clear()
                for (ds in snapshot.children) {
                    val id = "${ds.child("id").value}"
                    val category = "${ds.child("category").value}"

                    categoryIdArrayList.add(id)
                    categoryTitleArrayList.add(category)

                    Log.d(TAG, "onDataChange: Category id $id")
                    Log.d(TAG, "onDataChange: Category $category")
                }
            }

            override fun onCancelled(error: DatabaseError) {}
        })*/
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) = PdfEditFragment()
        const val TAG = "PDF_EDIT_TAG"
    }
}