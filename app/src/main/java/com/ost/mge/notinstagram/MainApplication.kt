package com.ost.mge.notinstagram

import android.app.Application
import com.ost.mge.notinstagram.storage.DatabaseHelper

class MainApplication : Application() {
    var database = DatabaseHelper(this)
}