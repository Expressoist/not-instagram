package com.example.notinstagram.interactions

import android.content.Context
import android.net.Uri
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import com.example.notinstagram.content.ImageCard
import java.io.File

class PhotoHandler(context: Context){

    private val context = context
    private var mostRecentUri : Uri? = null
    private var currentListener : PhotoObserver? = null
    private var launcher : ActivityResultLauncher<Uri>? = null

    fun setListener(activity: ComponentActivity, listener: PhotoObserver) {
        currentListener = listener
        launcher = activity.registerForActivityResult(
            ActivityResultContracts.TakePicture())
                { result -> handlePhotoResult(result) }
    }

    fun takePicture() {
        mostRecentUri = createTempUri(context)
        launcher?.launch(mostRecentUri)
    }

    private fun createTempUri(context: Context): Uri {
        val tmpFile = File.createTempFile("tmp_image_file", ".png", context.cacheDir).apply {
            createNewFile()
            deleteOnExit()
        }
        return FileProvider.getUriForFile(context, "${context.packageName}.provider", tmpFile)
    }

    private fun handlePhotoResult(hasBeenSavedToUri: Boolean){
        if(hasBeenSavedToUri) {
            currentListener?.onSuccess(mostRecentUri)
        }
    }

}