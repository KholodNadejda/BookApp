package com.example.bookapp.adapter

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.bookapp.*
import com.example.bookapp.viewModel.DeleteCategoryViewModel
import com.example.bookapp.filter.FilterCategory
import com.example.bookapp.model.ModelCategory
import com.example.bookapp.databinding.RowCategoryBinding
import com.example.bookapp.fragments.PdfListAdminFragment
import com.google.firebase.database.FirebaseDatabase


class AdapterCategory : RecyclerView.Adapter<AdapterCategory.HolderCategory>, Filterable {
    private val context: Context //
    var categoryList: List<ModelCategory> //
    private var filterList: List<ModelCategory>
    private var filter: FilterCategory? = null

    private lateinit var binding: RowCategoryBinding //

    private lateinit var viewModel: DeleteCategoryViewModel

    constructor(
        context: Context,
        //
        categoryArrayList: List<ModelCategory>,
    ) {
        this.context = context
        this.categoryList = categoryArrayList
        this.filterList = categoryArrayList
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HolderCategory { //
        binding = RowCategoryBinding.inflate(LayoutInflater.from(context), parent, false)
        return HolderCategory(binding.root)
    }

    override fun onBindViewHolder(holder: HolderCategory, position: Int) {
        //get data
        val model = categoryList[position]
        val id = model.id
        val category = model.category
        //val uid = model.uid
        //val timestamp = model.timestamp

        //set data
        holder.categoryTv.text = category

        //delete category
        holder.deleteBtn.setOnClickListener {
            val builder = AlertDialog.Builder(context)
            builder.setTitle("Delete")
                .setMessage("Are you sure?")
                .setPositiveButton("Confirm") { _, _ ->
                    if(!MyApplication.hasConnection(context)){
                        Toast.makeText(context, "Not internet connection", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(context, "Deleting", Toast.LENGTH_SHORT).show()
                        deleteCategory(model, holder)
                    }
                }
                .setNegativeButton("Cancel") { a, _ ->
                    a.dismiss()
                }
                .show()
        }

        holder.itemView.setOnClickListener { p0 ->
            /* Toast.makeText(
                 context,
                 "holder.itemView.setOnClickListener ${id.toString()} ${category.toString()} ",
                 Toast.LENGTH_SHORT
             ).show()*/
            val bundle = Bundle()
            bundle.putString("category", category)
            bundle.putString("categoryID", id)
            val frag = PdfListAdminFragment()
            frag.arguments = bundle
            val activity = p0!!.context as AppCompatActivity
            activity.supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainerView, frag)
                .addToBackStack(null)
                .commit()
        }
    }

    override fun getItemCount(): Int { //
        return categoryList.size
    }

    override fun getFilter(): Filter {
        if (filter == null) {
            filter = FilterCategory(filterList, this)
        }
        return filter as FilterCategory
    }

    private fun deleteCategory(model: ModelCategory, holder: HolderCategory) { //
        val id = model.id
        val ref = FirebaseDatabase.getInstance().getReference("Categories")
        ref.child(id)
            .removeValue()
            .addOnSuccessListener {
                Toast.makeText(context, "Deleted", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                Toast.makeText(context, "Unable to delete due to ${e.message}.", Toast.LENGTH_SHORT)
                    .show()
            }
    }

    inner class HolderCategory(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var categoryTv: TextView = binding.categoryTv
        var deleteBtn: ImageButton = binding.deleteBtn
    }
}