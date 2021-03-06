package com.example.bookapp.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager.widget.ViewPager
import com.example.bookapp.MyApplication
import com.example.bookapp.ViewModelFactory.CheckUserNameViewModelFactory
import com.example.bookapp.ViewModelFactory.LogoutUserViewModelFactory
import com.example.bookapp.ViewModelFactory.ViewPagerAdapterViewModelFactory
import com.example.bookapp.contracts.navigator
import com.example.bookapp.databinding.FragmentDashboardUserBinding
import com.example.bookapp.repository.CheckUserRepositoryImpl
import com.example.bookapp.repository.ViewPagerAdapterRepositoryImpl
import com.example.bookapp.viewModel.*


class DashboardUserFragment : Fragment() {

    private lateinit var binding: FragmentDashboardUserBinding
    private lateinit var viewPageAdapter: ViewPageAdapter
    private lateinit var checkUserNameViewModel: CheckUserNameViewModel
    private lateinit var checkUserRepositoryImpl: CheckUserRepositoryImpl
    private lateinit var logoutUserNameViewModel: LogoutUserViewModel
    private lateinit var viewPageAdapterViewModel: ViewPagerAdapterViewModel
    private lateinit var viewPagerAdapterRepositoryImpl: ViewPagerAdapterRepositoryImpl

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDashboardUserBinding.inflate(layoutInflater)
        viewPageAdapter = ViewPageAdapter(
            childFragmentManager,
            FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT,
            requireActivity()
        )
        checkUserRepositoryImpl = CheckUserRepositoryImpl()
        checkUserNameViewModel = ViewModelProvider(this,
            CheckUserNameViewModelFactory(checkUserRepositoryImpl)
        )[CheckUserNameViewModel::class.java]
        viewPagerAdapterRepositoryImpl = ViewPagerAdapterRepositoryImpl(viewPageAdapter)
        viewPageAdapterViewModel = ViewModelProvider(this, ViewPagerAdapterViewModelFactory(viewPagerAdapterRepositoryImpl)
        )[ViewPagerAdapterViewModel::class.java]

        checkUser()

        if(!MyApplication.hasConnection(requireActivity())){
            Toast.makeText(requireActivity(), "Not internet connection", Toast.LENGTH_SHORT).show()
        }
        setupWithViewPagerAdapter(binding.viewPager)

        binding.tabLayout.setupWithViewPager(binding.viewPager)

        binding.logOutBtn.setOnClickListener {
            logoutUserNameViewModel = ViewModelProvider(this, LogoutUserViewModelFactory(checkUserRepositoryImpl))[LogoutUserViewModel::class.java]
            logoutUserNameViewModel.modelsLiveData.observe(viewLifecycleOwner){
                if (it == true){
                    navigator().goToStart()
                }
            }
        }
        return binding.root
    }

    private fun setupWithViewPagerAdapter(viewPager: ViewPager) {

        viewPageAdapterViewModel.modelsLiveData.observe(viewLifecycleOwner){
            viewPager.adapter = it
        }
    }

    private fun checkUser() {
        checkUserNameViewModel.modelsLiveData.observe(viewLifecycleOwner){
            binding.subTitleTv.text = it
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = DashboardUserFragment()
    }

    class ViewPageAdapter(fm: FragmentManager, behavior: Int, context: Context) :
        FragmentPagerAdapter(fm, behavior) {
        private val fragmentList: ArrayList<BooksUserFragment> = ArrayList()
        private val fragmentTitleList: ArrayList<String> = ArrayList()
        private val context: Context

        init {
            this.context = context
        }

        override fun getCount(): Int {
            return fragmentList.size
        }

        override fun getItem(position: Int): Fragment {
            return fragmentList[position]
        }

        override fun getPageTitle(position: Int): CharSequence {
            return fragmentTitleList[position]
        }

        fun addFragment(fragment: BooksUserFragment, title: String) {
            fragmentList.add(fragment)
            fragmentTitleList.add(title)
        }
    }
}