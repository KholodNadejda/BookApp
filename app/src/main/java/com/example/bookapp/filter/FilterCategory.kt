package com.example.bookapp.filter

import android.widget.Filter
import com.example.bookapp.adapter.AdapterCategory
import com.example.bookapp.model.ModelCategory

class FilterCategory : Filter {
    private var filterList: List<ModelCategory> //
    private var adapterCategory: AdapterCategory //

    constructor(
        filterList: List<ModelCategory>,
        adapterCategory: AdapterCategory
    ) : super() { //
        this.filterList = filterList
        this.adapterCategory = adapterCategory
    }

    override fun performFiltering(constraint: CharSequence?): FilterResults { //
        var constraint = constraint
        val results = FilterResults()

        if (constraint != null && constraint.isNotEmpty()) {
            constraint = constraint.toString().uppercase()
            val filterModels: ArrayList<ModelCategory> = ArrayList()
            for (i in 0 until filterList.size) {
                if (filterList[i].category.uppercase().contains(constraint)) {
                    filterModels.add(filterList[i])
                }
            }
            results.count = filterModels.size
            results.values = filterModels
        } else {
            results.count = filterList.size
            results.values = filterList
        }
        return results
    }

    override fun publishResults(constraint: CharSequence?, results: FilterResults) { //
        adapterCategory.categoryList = results.values as ArrayList<ModelCategory>
        adapterCategory.notifyDataSetChanged()
    }
}