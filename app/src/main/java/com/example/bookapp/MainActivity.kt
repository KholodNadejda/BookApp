package com.example.bookapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Parcelable
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LifecycleOwner
import com.example.bookapp.contracts.Navigator
import com.example.bookapp.contracts.ResultListener
import com.example.bookapp.databinding.ActivityMainBinding
import com.example.bookapp.fragments.*

class MainActivity : AppCompatActivity(), Navigator {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)
  //savedInstanceState =0
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainerView, MainFragment.newInstance()).commit()
    }

    override fun showLoginFragment() {
        launchFragment(LoginFragment())
    }

    override fun showRegisterFragment() {
        launchFragment(RegisterFragment())
    }

    override fun showCategoryAddFragment() {
        launchFragment(CategoryAddFragment())
    }

    override fun showPdfAddFragment() {
        launchFragment(PdfAddFragment())
    }

    override fun showPdfListAdminFragment() {
        launchFragment(PdfListAdminFragment())
    }

    override fun showDashboardUserFragment() {
        launchFragment(DashboardUserFragment())
    }

    override fun showDashboardAdminFragment() {
        launchFragment(DashboardAdminFragment())
    }

    override fun showPdfEditFragment() {
        launchFragment(PdfEditFragment())
    }

    override fun showForgotPasswordFragment() {
        launchFragment(ForgotPasswordFragment())
    }

    override fun goBack() {
        onBackPressed()
    }

    override fun goToStart() {
        supportFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
    }

    override fun <T : Parcelable> publishResult(result: T) { }

    override fun <T : Parcelable> listenResult(
        clazz: Class<T>,
        owner: LifecycleOwner,
        listener: ResultListener<T>
    ) {

    }

    private fun launchFragment(fragment: Fragment) {
        supportFragmentManager
            .beginTransaction()
            /*   .setCustomAnimations(
                   R.anim.slide_in,
                   R.anim.fade_out,
                   R.anim.fade_in,
                   R.anim.slide_out
               )*/
            .addToBackStack(null)
            .replace(R.id.fragmentContainerView, fragment)
            .commit()
    }
}