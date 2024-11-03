package com.example.opsc7213_goalignite

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class BadgeAdapter(private var badges: List<Badge>) :
    RecyclerView.Adapter<BadgeAdapter.BadgeViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BadgeViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_badge, parent, false)
        return BadgeViewHolder(view)
    }

    override fun onBindViewHolder(holder: BadgeViewHolder, position: Int) {
        val badge = badges[position]
        holder.bind(badge)
    }

    override fun getItemCount(): Int = badges.size

    fun updateBadges(newBadges: List<Badge>) {
        this.badges = newBadges
        notifyDataSetChanged() // Notify the adapter to refresh the views
    }

    inner class BadgeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val badgeText: TextView = itemView.findViewById(R.id.textViewBadge)
        private val badgeIcon: ImageView = itemView.findViewById(R.id.imageViewBadge) // Assume you have an ImageView for the icon

        fun bind(badge: Badge) {
            badgeText.text = badge.dayName
            badgeIcon.setImageResource(badge.iconResId)
            badgeIcon.visibility = if (badge.isActive) View.VISIBLE else View.INVISIBLE // Show/hide icon based on active state
        }
    }
}
