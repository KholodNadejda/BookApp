package com.example.bookapp.fragments

import android.app.ProgressDialog
import android.os.Bundle
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.bookapp.MyApplication
import com.example.bookapp.ViewModelFactory.CreateUserAccountViewModelFactory
import com.example.bookapp.ViewModelFactory.UpdateUserInfoViewModelFactory
import com.example.bookapp.contracts.navigator
import com.example.bookapp.databinding.FragmentRegisterBinding
import com.example.bookapp.repository.CreateUserAccountRepositoryImpl
import com.example.bookapp.repository.UpdateUserInfoRepositoryImpl
import com.example.bookapp.viewModel.CreateUserAccountViewModel
import com.example.bookapp.viewModel.UpdateUserInfoViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class RegisterFragment : Fragment() {

    private lateinit var binding: FragmentRegisterBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var progressDialog: ProgressDialog

    private var name = ""
    private var email = ""
    private var password = ""

    private lateinit var createUserAccountViewModel: CreateUserAccountViewModel
    private lateinit var createUserAccountRepositoryImpl: CreateUserAccountRepositoryImpl

    private lateinit var updateUserInfoViewModel: UpdateUserInfoViewModel
    private lateinit var updateUserInfoRepositoryImpl: UpdateUserInfoRepositoryImpl

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRegisterBinding.inflate(layoutInflater)
        firebaseAuth = FirebaseAuth.getInstance()
        progressDialog = ProgressDialog(requireActivity())
        progressDialog.setTitle("Please wait")
        progressDialog.setCanceledOnTouchOutside(false)

        binding.backBtn.setOnClickListener {
            navigator().goBack()
        }
        binding.registerBtn.setOnClickListener {
            validateData()
        }

        return binding.root
    }

    private fun validateData() {
        name = binding.nameEt.text.toString().trim()
        email = binding.emailEt.text.toString().trim()
        password = binding.passwordEt.text.toString().trim()
        val confirmPassword = binding.confirmPasswordEt.text.toString().trim()

        if (name.isEmpty()) {
            Toast.makeText(activity?.applicationContext, "Enter your name", Toast.LENGTH_SHORT)
                .show()
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(requireActivity(), "Invalid Email", Toast.LENGTH_SHORT).show()
        } else if (password.isEmpty()) {
            Toast.makeText(requireActivity(), "Enter password", Toast.LENGTH_SHORT).show()
        } else if (confirmPassword.isEmpty()) {
            Toast.makeText(requireActivity(), "Confirm password", Toast.LENGTH_SHORT).show()
        } else if (password != confirmPassword) {
            Toast.makeText(requireActivity(), "Password doesn't match", Toast.LENGTH_SHORT).show()
        } else {
            if(!MyApplication.hasConnection(requireActivity())){
                Toast.makeText(requireActivity(), "Not internet connection", Toast.LENGTH_SHORT).show()
            } else {
                createUserAccount()
            }
        }
    }

    private fun createUserAccount() {
        progressDialog.setMessage("Creating Account")
        progressDialog.show()

        createUserAccountRepositoryImpl = CreateUserAccountRepositoryImpl(email, password)
        createUserAccountViewModel = ViewModelProvider(this, CreateUserAccountViewModelFactory(createUserAccountRepositoryImpl)
        )[CreateUserAccountViewModel::class.java]
        createUserAccountViewModel.modelsLiveData.observe(viewLifecycleOwner) {
            if( it == "Success") {
                updateUserInfo()
            } else {
                progressDialog.dismiss()
                Toast.makeText(requireActivity(), it, Toast.LENGTH_SHORT).show()
            }
        }

        /*firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                updateUserInfo()
            }
            .addOnFailureListener { e ->
                progressDialog.dismiss()
                Toast.makeText(
                    requireActivity(),
                    "Failed creatin account due to ${e.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }*/
    }

    private fun updateUserInfo() {
        updateUserInfoRepositoryImpl = UpdateUserInfoRepositoryImpl(email, name)
        updateUserInfoViewModel = ViewModelProvider(this, UpdateUserInfoViewModelFactory(updateUserInfoRepositoryImpl)
        )[UpdateUserInfoViewModel::class.java]

        updateUserInfoViewModel.modelsLiveData.observe(viewLifecycleOwner){
            if( it == "Success") {
                progressDialog.dismiss()
                Toast.makeText(requireActivity(), "Account created", Toast.LENGTH_SHORT).show()
                navigator().showDashboardUserFragment()
            } else {
                progressDialog.dismiss()
                Toast.makeText(requireActivity(), it, Toast.LENGTH_SHORT).show()
            }
        }

       /* val timestamp = System.currentTimeMillis()
        val uid = firebaseAuth.uid.toString()
        val hashMap: HashMap<String, Any?> = HashMap()
        hashMap["uid"] = uid
        hashMap["email"] = email
        hashMap["name"] = name
        hashMap["profileImage"] = ""
        hashMap["userType"] = "user"
        hashMap["timestamp"] = timestamp

        progressDialog.setMessage("Saving user info")
        val ref = FirebaseDatabase.getInstance().getReference("User")
        ref.child(uid)
            .setValue(hashMap)
            .addOnSuccessListener {
                progressDialog.dismiss()
                Toast.makeText(requireActivity(), "Account created", Toast.LENGTH_SHORT).show()
                navigator().showDashboardUserFragment()
            }
            .addOnFailureListener { e ->
                progressDialog.dismiss()
                Toast.makeText(
                    requireActivity(),
                    "Failed saving user info due to ${e.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }*/
    }

    companion object {
        @JvmStatic
        fun newInstance() = RegisterFragment()
    }
}