package com.ost.mge.notinstagram.content

import android.app.Activity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.notinstagram.R

class ImageCardAdapter(
    private val owner: Activity,
    private val imageCards: ArrayList<ImageCard>)
    : RecyclerView.Adapter<ImageCardViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageCardViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)

        val view = inflater.inflate(
            R.layout.content_item,
            parent,
            false
        )

        return ImageCardViewHolder (
            view,
            view.findViewById(R.id.description),
            view.findViewById(R.id.location_taken),
            view.findViewById(R.id.image_view)
        )
    }

    override fun onBindViewHolder(holder: ImageCardViewHolder, position: Int) {
        holder.descriptionView.text = imageCards[position].description
        holder.locationTakenView.text = imageCards[position].locationTaken
        val imageUri = imageCards[position].imageReference
        if(imageUri != null) {
            holder.imageView.setImageURI(imageUri)
        } else {
            holder.imageView.setImageDrawable(owner.resources.getDrawable(R.drawable.not_found))
        }
        holder.imageView.setImageURI(imageCards[position].imageReference)
    }

    override fun getItemCount(): Int {
        return imageCards.size
    }
}