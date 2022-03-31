package com.example.bookapp.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.bookapp.adapter.AdapterPdfAdmin
import com.example.bookapp.model.ModelPdf
import com.example.bookapp.contracts.navigator
import com.example.bookapp.databinding.FragmentPdfListAdminBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.lang.Exception

class PdfListAdminFragment : Fragment() {

    private lateinit var binding: FragmentPdfListAdminBinding

    private var categoryId = ""
    private var category = ""

    private lateinit var pdfArrayList: ArrayList<ModelPdf>
    private lateinit var adapterPdfAdmin: AdapterPdfAdmin

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPdfListAdminBinding.inflate(layoutInflater)

        categoryId = this.requireArguments().getString("categoryID").toString()
        category = this.requireArguments().getString("category").toString()

        binding.subTitleTv.text = category

        loadPdfList()

        binding.searchEt.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                try {
                    adapterPdfAdmin.filter!!.filter(p0)
                } catch (e: Exception) {
                    Log.d(TAG, "onTextChange: ${e.message}")
                }
            }

            override fun afterTextChanged(p0: Editable?) {}
        })

        binding.backBtn.setOnClickListener {
            navigator().goBack()
        }

        return binding.root

    }

    private fun loadPdfList() {
        pdfArrayList = ArrayList()

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
                    adapterPdfAdmin = AdapterPdfAdmin(requireActivity(), pdfArrayList)
                    binding.booksRv.adapter = adapterPdfAdmin
                }

                override fun onCancelled(error: DatabaseError) {

                }
            })
    }

    companion object {
        @JvmStatic
        fun newInstance() = PdfListAdminFragment()
        const val TAG = "PDF_LIST_ADMIN_TAG"
    }
}