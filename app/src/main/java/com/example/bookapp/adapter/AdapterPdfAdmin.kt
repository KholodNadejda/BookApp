package com.example.bookapp.adapter

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.bookapp.filter.FilterPdfAdmin
import com.example.bookapp.model.ModelPdf
import com.example.bookapp.MyApplication
import com.example.bookapp.R
import com.example.bookapp.databinding.RowPdfAdminBinding
import com.example.bookapp.fragments.PdfDetailFragment
import com.example.bookapp.fragments.PdfEditFragment

class AdapterPdfAdmin
    : RecyclerView.Adapter<AdapterPdfAdmin.HolderPdfAdmin>, Filterable {

    private var context: Context
    var pdfArrayList: ArrayList<ModelPdf>
    private val filterList: ArrayList<ModelPdf>
    private var filter: FilterPdfAdmin? = null

    private lateinit var binding: RowPdfAdminBinding

    constructor(context: Context, pdfArrayList: ArrayList<ModelPdf>) : super() {
        this.context = context
        this.pdfArrayList = pdfArrayList
        this.filterList = pdfArrayList
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HolderPdfAdmin {
        binding = RowPdfAdminBinding.inflate(LayoutInflater.from(context), parent, false)
        return HolderPdfAdmin(binding.root)
    }

    override fun onBindViewHolder(holder: HolderPdfAdmin, position: Int) {
        val model = pdfArrayList[position]
        val pdfId = model.id
        val categoryId = model.categoryId
        val title = model.title
        val description = model.description
        val pdfUrl = model.url
        val timestamp = model.timestamp
        val formattedDate = MyApplication.formatTimeStamp(timestamp)

        holder.titleTv.text = title
        holder.descriptionTv.text = description
        holder.dateTv.text = formattedDate

        MyApplication.loadCategory(categoryId, holder.categoryTv)
        // MyApplication.loadPdfFromUrlSingPage(pdfUrl, title, holder.pdfView, holder.progressBar, null)
        MyApplication.loadPdfSize(pdfUrl, holder.sizeTv)

        holder.moreBtn.setOnClickListener {
            moreOptionDialog(model, holder)
        }

        holder.itemView.setOnClickListener { p0 ->
            val bundle = Bundle()
            bundle.putString("bookId", pdfId)
            val frag = PdfDetailFragment()
            frag.arguments = bundle
            val activity = p0!!.context as AppCompatActivity
            activity.supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainerView, frag)
                .addToBackStack(null)
                .commit()
        }
    }

    private fun moreOptionDialog(model: ModelPdf, holder: HolderPdfAdmin) {

        val options = arrayOf("Edit", "Delete")

        val builder = AlertDialog.Builder(context)
        builder.setTitle("Choose Options")
            .setItems(options) { _, position ->
                if (position == 0) {
                    val bundle = Bundle()
                    bundle.putString("bookId", model.id)
                    val frag = PdfEditFragment()
                    frag.arguments = bundle
                    val activity = context as AppCompatActivity
                    activity.supportFragmentManager.beginTransaction()
                        .replace(R.id.fragmentContainerView, frag)
                        .addToBackStack(null)
                        .commit()
                } else if (position == 1) {
                    if(!MyApplication.hasConnection(context)){
                        Toast.makeText(context, "Not internet connection", Toast.LENGTH_SHORT).show()
                    } else {
                        MyApplication.deleteBook(context, model)
                    }
                }
            }
            .show()
    }

    override fun getItemCount(): Int {
        return pdfArrayList.size
    }

    override fun getFilter(): Filter {
        if (filter == null) {
            filter = FilterPdfAdmin(filterList, this)
        }
        return filter as FilterPdfAdmin
    }

    inner class HolderPdfAdmin(itemView: View) : RecyclerView.ViewHolder(itemView) {
        //  val pdfView = binding.pdfView
        //  val progressBar = binding.progressBar
        val titleTv = binding.titleTv
        val descriptionTv = binding.descriptionTv
        val categoryTv = binding.categoryTv
        val sizeTv = binding.sizeTv
        val dateTv = binding.dateTv
        val moreBtn = binding.moreBtn
    }
}