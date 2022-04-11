package com.example.bookapp.repository

import androidx.lifecycle.MutableLiveData
import com.example.bookapp.fragments.BooksUserFragment
import com.example.bookapp.fragments.DashboardUserFragment
import com.example.bookapp.model.ModelCategory
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

interface ViewPagerAdapterRepository {
    fun setupWithViewPagerAdapter(): MutableLiveData<DashboardUserFragment.ViewPageAdapter>
}
class ViewPagerAdapterRepositoryImpl(var viewPageAdapter: DashboardUserFragment.ViewPageAdapter): ViewPagerAdapterRepository {
    private var categoryArrayList: ArrayList<ModelCategory>
    private val modelsLiveData = MutableLiveData<DashboardUserFragment.ViewPageAdapter>()
    init {
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
    }

    override fun setupWithViewPagerAdapter(): MutableLiveData<DashboardUserFragment.ViewPageAdapter> {
        modelsLiveData.value = viewPageAdapter
        return modelsLiveData
    }

}