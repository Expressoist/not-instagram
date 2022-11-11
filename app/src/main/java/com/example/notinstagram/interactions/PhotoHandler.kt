package com.example.notinstagram.interactions

import android.annotation.SuppressLint
import android.content.ContentResolver.MimeTypeInfo
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.content.FileProvider
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import java.util.function.Consumer

class PhotoHandler(private var owner: ComponentActivity){

    companion object {
        const val TIMESTAMP_FORMAT = "yyyyMMdd_HHmmss";
        const val RELATIVE_PATH = "relative_path" // MediaStore.MediaColumns.RELATIVE_PATH needs API 29
    }

    private var currentPhotoPath : Uri? = null
    private var callback: Consumer<Uri>? = null;
    private var photoLauncher : ActivityResultLauncher<Uri> = owner.registerForActivityResult(
                                    ActivityResultContracts.TakePicture())
                                    { result -> onPhotoTaken(result) }

    fun takePhoto(onSuccess: Consumer<Uri>) {
        callback = onSuccess
        currentPhotoPath = initFile(owner)
        photoLauncher.launch(currentPhotoPath)
    }

    @SuppressLint("SimpleDateFormat")
    private fun initFile(context: Context) : Uri? {
        val timeStamp = SimpleDateFormat(TIMESTAMP_FORMAT).format(Date())
        val fileValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, "JPEG_${timeStamp}")
            put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
            put(RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
        }
        return context.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, fileValues)
    }

    private fun onPhotoTaken(wasSuccessful: Boolean){
        if(wasSuccessful) {
            currentPhotoPath?.let {
                callback?.accept(it)
            }
        }
    }
}