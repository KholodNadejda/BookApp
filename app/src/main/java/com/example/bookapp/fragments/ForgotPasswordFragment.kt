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
import com.example.bookapp.databinding.FragmentForgotPasswordBinding
import com.example.bookapp.repository.RecoverPasswordRepositoryImpl
import com.example.bookapp.viewModel.RecoverPasswordViewModel
import com.example.bookapp.ViewModelFactory.RecoverPasswordViewModelFactory

class ForgotPasswordFragment : Fragment() {

    private lateinit var binding: FragmentForgotPasswordBinding
    private lateinit var progressDialog: ProgressDialog
    private lateinit var recoverPasswordViewModel: RecoverPasswordViewModel
    private lateinit var recoverPasswordRepositoryImpl: RecoverPasswordRepositoryImpl

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentForgotPasswordBinding.inflate(layoutInflater)

        progressDialog = ProgressDialog(requireActivity())
        progressDialog.setTitle("Please wait")
        progressDialog.setCanceledOnTouchOutside(false)

        binding.submitBtn.setOnClickListener {
            validateData()
        }

        binding.backBtn.setOnClickListener {
            navigator().goBack()
        }

        return binding.root
    }

    private fun validateData() {
        val email = binding.emailEt.text.toString().trim()
        if (email.isEmpty()) {
            Toast.makeText(requireActivity(), "Enter email", Toast.LENGTH_SHORT).show()
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(requireActivity(), "Invalidate email pattern", Toast.LENGTH_SHORT).show()
        } else {
            recoverPassword(email)
        }
    }

    private fun recoverPassword(email: String) {
        recoverPasswordRepositoryImpl = RecoverPasswordRepositoryImpl(email)
        recoverPasswordViewModel = ViewModelProvider(this, RecoverPasswordViewModelFactory(recoverPasswordRepositoryImpl)
        )[RecoverPasswordViewModel::class.java]
        progressDialog.setMessage("Sending password reset instructions to $email")
        progressDialog.show()
        recoverPasswordViewModel.modelsLiveData.observe(viewLifecycleOwner){
            progressDialog.dismiss()
            Toast.makeText(requireActivity(), it, Toast.LENGTH_SHORT)
                .show()
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = ForgotPasswordFragment()
    }
}