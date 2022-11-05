package com.example.notinstagram.interactions

import android.net.Uri

interface PhotoObserver {

    fun onSuccess(imageUri : Uri?)

    fun onFailure()

}