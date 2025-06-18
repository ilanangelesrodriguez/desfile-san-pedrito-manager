package com.uns.desfilesanpedritomanager.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.uns.desfilesanpedritomanager.data.InMemoryPersonRepository

class PersonViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PersonViewModel::class.java)) {
            return PersonViewModel(InMemoryPersonRepository()) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}