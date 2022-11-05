package com.example.notinstagram.content

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ImageCardViewHolder(
    itemView: View,
    descriptionView: TextView,
    locationTakenView: TextView,
    imageView: ImageView) : RecyclerView.ViewHolder(itemView) {

    val descriptionView: TextView = descriptionView
    val locationTakenView: TextView = locationTakenView
    val imageView: ImageView = imageView

}