package com.example.bookapp.contracts

import android.os.Parcelable
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner

typealias ResultListener<T> = (T) -> Unit

fun Fragment.navigator(): Navigator {
    return requireActivity() as Navigator
}

interface Navigator {
    fun showLoginFragment()

    fun showRegisterFragment()

    fun showForgotPasswordFragment()

    fun showDashboardUserFragment()

    fun showDashboardAdminFragment()

    fun showCategoryAddFragment()

    fun showPdfAddFragment()

    fun showPdfEditFragment()

    fun showPdfListAdminFragment()

    fun goBack()

    fun goToStart()

    fun <T : Parcelable> publishResult(result: T)

    fun <T : Parcelable> listenResult(
        clazz: Class<T>,
        owner: LifecycleOwner,
        listener: ResultListener<T>
    )
}