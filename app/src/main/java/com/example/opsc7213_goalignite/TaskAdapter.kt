package com.example.opsc7213_goalignite

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class TaskAdapter(private val eventsList: MutableList<Event>) : RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_event, parent, false)
        return TaskViewHolder(view)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val event = eventsList[position]
        holder.bind(event)
    }

    override fun getItemCount(): Int = eventsList.size

    fun updateTasks(newEvents: List<Event>) {
        eventsList.clear()
        eventsList.addAll(newEvents)
        notifyDataSetChanged()
    }


    inner class TaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val nameTextView: TextView = itemView.findViewById(R.id.EventName)
        private val timeTextView: TextView = itemView.findViewById(R.id.EventTime)
        private val dateTextView: TextView = itemView.findViewById(R.id.EventDate)

        fun bind(event: Event) {
            nameTextView.text = event.name
            timeTextView.text = event.time
            dateTextView.text = event.date
        }
    }
}
