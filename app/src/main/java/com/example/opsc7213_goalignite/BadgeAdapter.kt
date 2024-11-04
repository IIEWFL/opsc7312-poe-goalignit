package com.example.opsc7213_goalignite

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
// Adapter for displaying a list of badges in a RecyclerView
class BadgeAdapter(private var badges: List<Badge>) :
    RecyclerView.Adapter<BadgeAdapter.BadgeViewHolder>() {

    // Called when RecyclerView needs a new ViewHolder of the given type to represent an item
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BadgeViewHolder {
        // Inflate the layout for each badge item
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_badge, parent, false)
        return BadgeViewHolder(view)
    }

    // Called by RecyclerView to display the data at the specified position
    override fun onBindViewHolder(holder: BadgeViewHolder, position: Int) {
        // Bind the badge data to the ViewHolder
        val badge = badges[position]
        holder.bind(badge)
    }

    // Returns the total number of items in the dataset
    override fun getItemCount(): Int = badges.size

    // Method to update the list of badges and notify RecyclerView of data change
    fun updateBadges(newBadges: List<Badge>) {
        this.badges = newBadges
        notifyDataSetChanged() // Notify the adapter to refresh the views
    }

    // Inner ViewHolder class that represents the individual badge items in RecyclerView
    inner class BadgeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // References to the TextView and ImageView within each badge item layout
        private val badgeText: TextView = itemView.findViewById(R.id.textViewBadge)
        private val badgeIcon: ImageView = itemView.findViewById(R.id.imageViewBadge) // ImageView for badge icon

        // Binds the badge data to the item views
        fun bind(badge: Badge) {
            // Set the day name text for the badge
            badgeText.text = badge.dayName
            // Set the icon resource for the badge
            badgeIcon.setImageResource(badge.iconResId)
            // Display the icon only if the badge is active, otherwise hide it
            badgeIcon.visibility = if (badge.isActive) View.VISIBLE else View.INVISIBLE
        }
    }
}
