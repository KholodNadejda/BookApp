package com.example.bookapp.filter

import android.widget.Filter
import com.example.bookapp.adapter.AdapterPdfAdmin
import com.example.bookapp.model.ModelPdf

class FilterPdfAdmin
    : Filter {
    var filterList: ArrayList<ModelPdf>
    var adapterPdfAdmin: AdapterPdfAdmin

    constructor(filterList: ArrayList<ModelPdf>, adapterPdfAdmin: AdapterPdfAdmin) {
        this.filterList = filterList
        this.adapterPdfAdmin = adapterPdfAdmin
    }

    override fun performFiltering(constaint: CharSequence?): FilterResults {
        var constaint: CharSequence? = constaint
        val results = FilterResults()
        if (constaint != null && constaint.isNotEmpty()) {
            constaint = constaint.toString().lowercase()
            var filterModels = ArrayList<ModelPdf>()
            for (i in filterList.indices) {
                if (filterList[i].title.lowercase().contains(constaint)) {
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

    override fun publishResults(constaint: CharSequence, results: FilterResults) {
        adapterPdfAdmin.pdfArrayList = results.values as ArrayList<ModelPdf>
        adapterPdfAdmin.notifyDataSetChanged()
    }
}