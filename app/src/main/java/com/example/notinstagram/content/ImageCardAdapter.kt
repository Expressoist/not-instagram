package com.example.notinstagram.content

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.notinstagram.MainActivity
import com.example.notinstagram.R

class ImageCardAdapter(mainActivity: MainActivity, data: ArrayList<ImageCard>) : RecyclerView.Adapter<ImageCardViewHolder>() {

    private val imageCards = data

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
        holder.imageView.setImageURI(imageCards[position].imageReference)
    }

    override fun getItemCount(): Int {
        return imageCards.size
    }
}