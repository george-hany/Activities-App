package com.example.android.activitiesproject.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

object ViewModelProviderFactory {
    fun <T : ViewModel> createFor(viewModel: T): ViewModelProvider.Factory {
        return object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                if (modelClass.isAssignableFrom(viewModel.javaClass)) {
                    @Suppress("UNCHECKED_CAST")
                    return viewModel as T
                }
                throw IllegalArgumentException("Unknown class name")
            }
        }
    }
}
