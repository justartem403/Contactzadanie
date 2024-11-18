package com.example.contactzadanie

import android.app.Application

class ContactApplication : Application() {
    val database: ContactDatabase by lazy {
        ContactDatabase.getDatabase(this)
    }
}