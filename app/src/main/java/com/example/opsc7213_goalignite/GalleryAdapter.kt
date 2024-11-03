package com.example.opsc7213_goalignite

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
//getCount() adapted from Android Developers
//https://developer.android.com/reference/kotlin/android/widget/Adapter#:~:text=Adapter%20|%20Android%20Developers.%20Essentials.%20Gemini%20in%20Android
//Android Developers
//Adapter to display list of documents

class GalleryAdapter(private val mediaList: List<Gallery>, private val onItemClick: (Gallery) -> Unit) : RecyclerView.Adapter<GalleryAdapter.MediaViewHolder>() {

    inner class MediaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.media_title)
        val mediaThumbnail: ImageView = itemView.findViewById(R.id.media_thumbnail)

        init {
            itemView.setOnClickListener {
                onItemClick(mediaList[adapterPosition])
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MediaViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_media, parent, false)
        return MediaViewHolder(view)
    }

    override fun onBindViewHolder(holder: MediaViewHolder, position: Int) {
        val media = mediaList[position]
        holder.title.text = media.title

        // Load thumbnail for videos or display a placeholder for images
        if (media.filePath.endsWith(".mp4")) { // Assuming video files
            holder.mediaThumbnail.setImageResource(R.drawable.video_placeholder) // Placeholder for videos
        } else {
            Glide.with(holder.itemView.context)
                .load(media.filePath)
                .placeholder(R.drawable.image_placeholder) // Placeholder for images
                .into(holder.mediaThumbnail)
        }
    }

    override fun getItemCount(): Int {
        return mediaList.size
    }
}
