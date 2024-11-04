package com.example.opsc7213_goalignite

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

// Adapter class for displaying a list of events in a RecyclerView
class TaskAdapter(private val eventsList: MutableList<Event>) : RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {

    // Called when RecyclerView needs a new ViewHolder to represent an item
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        // Inflate the item layout for each event
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_event, parent, false)
        return TaskViewHolder(view)
    }

    // Called to display the data for a specific position in the RecyclerView
    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        // Get the event at the specified position and bind it to the ViewHolder
        val event = eventsList[position]
        holder.bind(event)
    }

    // Returns the total count of items in the events list
    override fun getItemCount(): Int = eventsList.size

    // Method to update the list of events in the adapter and refresh the view
    fun updateTasks(newEvents: List<Event>) {
        eventsList.clear() // Clear the existing list
        eventsList.addAll(newEvents) // Add new events to the list
        notifyDataSetChanged() // Notify adapter to rebind data and update the view
    }

    // ViewHolder class to hold and bind views for each item in the RecyclerView
    inner class TaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // TextView references for event name, time, and date
        private val nameTextView: TextView = itemView.findViewById(R.id.EventName)
        private val timeTextView: TextView = itemView.findViewById(R.id.EventTime)
        private val dateTextView: TextView = itemView.findViewById(R.id.EventDate)

        // Method to bind event data to the views
        fun bind(event: Event) {
            nameTextView.text = event.name // Set the event name
            timeTextView.text = event.time // Set the event time
            dateTextView.text = event.date // Set the event date
        }
    }
}
