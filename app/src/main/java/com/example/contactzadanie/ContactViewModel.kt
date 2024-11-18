package com.example.contactzadanie

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ContactViewModel(
    private val contactDao: ContactDao
) : ViewModel() {
    val contacts: Flow<List<Contact>> = contactDao.getAllContacts()

    private val _errorState = MutableStateFlow<String?>(null)
    val errorState = _errorState.asStateFlow()

    fun addContact(name: String, phoneNumber: String, email: String) {
        viewModelScope.launch {
            try {
                if (name.isBlank() || phoneNumber.isBlank() || email.isBlank()) {
                    _errorState.value = "All fields must be filled"
                    return@launch
                }

                val contact = Contact(
                    name = name.trim(),
                    phoneNumber = phoneNumber.trim(),
                    email = email.trim()
                )
                contactDao.insertContact(contact)
            } catch (e: Exception) {
                _errorState.value = "Failed to add contact: ${e.message}"
            }
        }
    }

    fun updateContact(contact: Contact) {
        viewModelScope.launch {
            try {
                if (contact.name.isBlank() || contact.phoneNumber.isBlank() || contact.email.isBlank()) {
                    _errorState.value = "All fields must be filled"
                    return@launch
                }
                contactDao.updateContact(contact)
            } catch (e: Exception) {
                _errorState.value = "Failed to update contact: ${e.message}"
            }
        }
    }

    fun deleteContact(contact: Contact) {
        viewModelScope.launch {
            try {
                contactDao.deleteContact(contact)
            } catch (e: Exception) {
                _errorState.value = "Failed to delete contact: ${e.message}"
            }
        }
    }

    fun clearError() {
        _errorState.value = null
    }

    class ContactViewModelFactory(private val contactDao: ContactDao) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(ContactViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return ContactViewModel(contactDao) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}