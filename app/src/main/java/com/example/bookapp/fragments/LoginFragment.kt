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
import com.example.bookapp.contracts.navigator
import com.example.bookapp.databinding.FragmentLoginBinding
import com.example.bookapp.repository.LoginUserRepositoryImpl
import com.example.bookapp.viewModel.LoginUserViewModel
import com.example.bookapp.ViewModelFactory.LoginUserViewModelFactory
import com.google.firebase.auth.FirebaseAuth

class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var progressDialog: ProgressDialog
    private lateinit var loginUserViewModel: LoginUserViewModel
    private lateinit var loginUserRepositoryImpl: LoginUserRepositoryImpl
    private var email = ""
    private var password = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        firebaseAuth = FirebaseAuth.getInstance()
        progressDialog = ProgressDialog(requireActivity())
        progressDialog.setTitle("Please wait")
        progressDialog.setCanceledOnTouchOutside(false)
        binding = FragmentLoginBinding.inflate(layoutInflater)

        binding.noAccoutTv.setOnClickListener {
            navigator().showRegisterFragment()
        }

        binding.loginBtn.setOnClickListener {
            validateData()
        }

        binding.backBtn.setOnClickListener {
            navigator().goBack()
        }

        binding.forgotTv.setOnClickListener {
            navigator().showForgotPasswordFragment()
        }

        return binding.root
    }

    private fun validateData() {
        email = binding.emailEt.text.toString().trim()
        password = binding.passwordEt.text.toString().trim()

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(requireActivity(), "Invalid Email format", Toast.LENGTH_SHORT).show()
        } else if (password.isEmpty()) {
            Toast.makeText(requireActivity(), "Enter password", Toast.LENGTH_SHORT).show()
        } else {
            loginUser()
        }
    }

    private fun loginUser() {
        loginUserRepositoryImpl = LoginUserRepositoryImpl(email, password)
        loginUserViewModel = ViewModelProvider(this, LoginUserViewModelFactory(loginUserRepositoryImpl)
        )[LoginUserViewModel::class.java]

        progressDialog.setMessage("Loggin in...")
        progressDialog.show()

        loginUserViewModel.modelsLiveData.observe(viewLifecycleOwner){
            progressDialog.dismiss()
            when (it) {
                "user" -> {
                    navigator().showDashboardUserFragment()
                }
                "admin" -> {
                    navigator().showDashboardAdminFragment()
                }
                else -> {
                    Toast.makeText(
                        requireActivity(),
                        it,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }

        /*firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                checkUser()
            }
            .addOnFailureListener { e ->
                progressDialog.dismiss()
                Toast.makeText(
                    requireActivity(),
                    "Login failed due to ${e.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }*/
    }

   /* private fun checkUser() {
        progressDialog.setMessage("Checking user...")

        val firebaseUser = firebaseAuth.currentUser!!
        val ref = FirebaseDatabase.getInstance().getReference("User")
        ref.child(firebaseUser.uid)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(error: DatabaseError) { }

                override fun onDataChange(snapshot: DataSnapshot) {
                    progressDialog.dismiss()
                    val userType = snapshot.child("userType").value
                    if (userType == "user") {
                        navigator().showDashboardUserFragment()
                    } else if (userType == "admin") {
                        navigator().showDashboardAdminFragment()
                    }
                }
            })
    }*/

    companion object {
        @JvmStatic
        fun newInstance() = LoginFragment()
    }
}