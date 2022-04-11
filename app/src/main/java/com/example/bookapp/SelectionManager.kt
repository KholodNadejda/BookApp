package com.example.bookapp

import com.example.bookapp.model.ModelCategory

interface SelectionManager {
    fun deleteCategory(model: ModelCategory)
    fun goToPdfList()
}
