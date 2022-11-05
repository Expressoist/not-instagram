package com.example.notinstagram

import android.app.Application
import com.example.notinstagram.interactions.LocationHandler
import com.example.notinstagram.interactions.PhotoHandler
import com.example.notinstagram.storage.DatabaseHelper

class MainApplication : Application() {

    var database = DatabaseHelper(this)
    var photoHandler = PhotoHandler(this)

}