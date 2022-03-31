package com.example.bookapp.fragments

import android.content.Context
import android.net.ConnectivityManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.example.bookapp.MyApplication
import com.example.bookapp.contracts.navigator
import com.example.bookapp.databinding.FragmentDashboardUserBinding
import com.example.bookapp.model.ModelCategory
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class DashboardUserFragment : Fragment() {

    private lateinit var binding: FragmentDashboardUserBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var categoryArrayList: ArrayList<ModelCategory>
    private lateinit var viewPageAdapter: ViewPageAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDashboardUserBinding.inflate(layoutInflater)
        firebaseAuth = FirebaseAuth.getInstance()

        checkUser()
        if(!MyApplication.hasConnection(requireActivity())){
            Toast.makeText(requireActivity(), "Not internet connection", Toast.LENGTH_SHORT).show()
        }
        setupWithViewPagerAdapter(binding.viewPager)

        binding.tabLayout.setupWithViewPager(binding.viewPager)

        binding.logOutBtn.setOnClickListener {
            firebaseAuth.signOut()
            navigator().goToStart()
        }

        return binding.root
    }

    private fun setupWithViewPagerAdapter(viewPager: ViewPager) {
        viewPageAdapter = ViewPageAdapter(
            childFragmentManager,
            FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT,
            requireActivity()
        )
        categoryArrayList = ArrayList()

        val ref = FirebaseDatabase.getInstance().getReference("Categories")
        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                categoryArrayList.clear()

                val modelAll = ModelCategory("01", "All", 1, "")
                // val modelMostViewed = ModelCategory("01", "Most Viewed", 1, "")
                val modelMostDownloaded = ModelCategory("01", "Most Downloaded", 1, "")

                categoryArrayList.add(modelAll)
                // categoryArrayList.add(modelMostViewed)
                categoryArrayList.add(modelMostDownloaded)

                viewPageAdapter.addFragment(
                    BooksUserFragment.newInstance(
                        "${modelAll.id}",
                        "${modelAll.category}",
                        "${modelAll.uid}"
                    ), modelAll.category
                )
                /*   viewPageAdapter.addFragment(
                       BooksUserFragment.newInstance(
                           "${modelMostViewed.id}",
                           "${modelMostViewed.category}",
                           "${modelMostViewed.uid}"
                       ), modelMostViewed.category
                   )*/
                viewPageAdapter.addFragment(
                    BooksUserFragment.newInstance(
                        "${modelMostDownloaded.id}",
                        "${modelMostDownloaded.category}",
                        "${modelMostDownloaded.uid}"
                    ), modelMostDownloaded.category
                )
                viewPageAdapter.notifyDataSetChanged()

                for (ds in snapshot.children) {
                    val model = ds.getValue(ModelCategory::class.java)
                    categoryArrayList.add(model!!)

                    viewPageAdapter.addFragment(
                        BooksUserFragment.newInstance(
                            "${model.id}",
                            "${model.category}",
                            "${model.uid}"
                        ), model.category
                    )
                    viewPageAdapter.notifyDataSetChanged()
                }
            }
            override fun onCancelled(error: DatabaseError) {  }
        })
        //
        ref.keepSynced(true)
        //
        viewPager.adapter = viewPageAdapter
    }

    private fun checkUser() {
        val firebaseUser = firebaseAuth.currentUser
        if (firebaseUser == null) {
            binding.subTitleTv.text = "Not Logging In"
        } else {
            binding.subTitleTv.text = firebaseUser.email
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