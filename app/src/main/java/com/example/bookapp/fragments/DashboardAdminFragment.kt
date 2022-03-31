package com.example.bookapp.fragments

import android.content.Context
import android.net.ConnectivityManager
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.bookapp.MyApplication
import com.example.bookapp.adapter.AdapterCategory
import com.example.bookapp.model.ModelCategory
import com.example.bookapp.contracts.navigator
import com.example.bookapp.databinding.FragmentDashboardAdminBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class DashboardAdminFragment : Fragment() {

    private lateinit var binding: FragmentDashboardAdminBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var categoryArrayList: ArrayList<ModelCategory>
    private lateinit var adapterCategory: AdapterCategory

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDashboardAdminBinding.inflate(layoutInflater)
        firebaseAuth = FirebaseAuth.getInstance()
        checkUser()
        binding.addCategoryBtn.setOnClickListener {
            if(!MyApplication.hasConnection(requireActivity())){
                Toast.makeText(requireActivity(), "Not internet connection", Toast.LENGTH_SHORT).show()
            } else {
                navigator().showCategoryAddFragment()
            }
        }

        binding.addPdfFab.setOnClickListener {
            if(!MyApplication.hasConnection(requireActivity())){
                Toast.makeText(requireActivity(), "Not internet connection", Toast.LENGTH_SHORT).show()
            } else {
                navigator().showPdfAddFragment()
            }
        }

        binding.logOutBtn.setOnClickListener {
            firebaseAuth.signOut()
            checkUser()
        }

        binding.searchEt.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                try {
                    adapterCategory.filter.filter(p0)
                } catch (e: Exception) { }
            }

            override fun afterTextChanged(p0: Editable?) {}
        })

        loadCategories()

        return binding.root
    }

    private fun loadCategories() {
        categoryArrayList = ArrayList()
        val ref = FirebaseDatabase.getInstance().getReference("Categories")
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                categoryArrayList.clear()
                for (ds in snapshot.children) {
                    val model = ds.getValue((ModelCategory::class.java))
                    categoryArrayList.add(model!!)
                }
                adapterCategory = AdapterCategory(requireActivity(), categoryArrayList)
                binding.categoriesRv.adapter = adapterCategory
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
        //
        ref.keepSynced(true)
        //
    }

    private fun checkUser() {
        val firebaseUser = firebaseAuth.currentUser
        if (firebaseUser == null) {
            navigator().goToStart()
        } else {
            binding.subTitleTv.text = firebaseUser.email
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = DashboardAdminFragment()
    }
}