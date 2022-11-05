package com.example.notinstagram.content

import android.net.Uri

data class ImageCard(
    var imageReference: Uri?,
    var locationTaken: String,
    var description: String,
)
