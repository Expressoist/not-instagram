package com.ost.mge.notinstagram.interactions

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.activity.result.contract.ActivityResultContract
import com.ost.mge.notinstagram.EditorActivity
import com.ost.mge.notinstagram.content.ImageCard

class EditorContract : ActivityResultContract<Uri, ImageCard?>() {

    companion object {
        const val IMAGE_URI = "IMAGE_URI"
        const val LOCATION_TAKEN = "LOCATION_TAKEN"
        const val DESCRIPTION = "DESCRIPTION"
    }

    override fun createIntent(context: Context, input: Uri): Intent {
        val intent = Intent(context, EditorActivity::class.java)
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        intent.putExtra(IMAGE_URI, input)
        return intent
    }

    override fun parseResult(resultCode: Int, intent: Intent?): ImageCard? {
        if (resultCode == Activity.RESULT_OK && intent != null) {
            return ImageCard(
                intent.extras?.get(IMAGE_URI) as Uri?,
                intent.extras?.get(LOCATION_TAKEN) as String,
                intent.extras?.get(DESCRIPTION) as String,
            )
        }
        return null;
    }
}