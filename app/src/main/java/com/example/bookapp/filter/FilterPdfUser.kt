package com.example.bookapp.filter

import android.widget.Filter
import com.example.bookapp.adapter.AdapterPdfUser
import com.example.bookapp.model.ModelPdf


class FilterPdfUser : Filter {
    var filterList: ArrayList<ModelPdf>
    var adapterPdfUser: AdapterPdfUser

    constructor(filterList: ArrayList<ModelPdf>, adapterPdfUser: AdapterPdfUser) {
        this.filterList = filterList
        this.adapterPdfUser = adapterPdfUser
    }

    override fun performFiltering(constaint: CharSequence?): FilterResults {
        var constaint: CharSequence? = constaint
        val results = FilterResults()
        if (constaint != null && constaint.isNotEmpty()) {
            constaint = constaint.toString().uppercase()
            var filterModels = ArrayList<ModelPdf>()
            for (i in filterList.indices) {
                if (filterList[i].title.uppercase().contains(constaint)) {
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

    override fun publishResults(constaint: CharSequence?, results: FilterResults) {
        adapterPdfUser.pdfArrayList = results.values as ArrayList<ModelPdf>
        adapterPdfUser.notifyDataSetChanged()
    }
}