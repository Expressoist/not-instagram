package com.example.notinstagram.interactions

import android.content.Context
import android.content.Intent
import android.media.Image
import android.net.Uri
import androidx.activity.result.contract.ActivityResultContract
import com.example.notinstagram.EditorActivity
import com.example.notinstagram.content.ImageCard

class EditorContract : ActivityResultContract<Uri, ImageCard>() {

    companion object {
        const val IMAGE_URI = "IMAGE_URI"
    }

    override fun createIntent(context: Context, input: Uri): Intent {
        val intent = Intent(context, EditorActivity::class.java)
        intent.action = input.toString()
        intent.putExtra(IMAGE_URI, input)
        intent.putExtra("Test", "test")
        return intent
    }

    override fun parseResult(resultCode: Int, intent: Intent?): ImageCard {
        return ImageCard(
            intent?.extras?.get(IMAGE_URI) as Uri?,
            intent?.extras?.get("locationTaken") as String,
            intent?.extras?.get("description") as String
        )
    }

}