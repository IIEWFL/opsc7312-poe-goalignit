package com.example.opsc7213_goalignite.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.opsc7213_goalignite.FAQItem
import com.example.opsc7213_goalignite.R

class FAQAdapter(private val faqList: List<FAQItem>) : RecyclerView.Adapter<FAQAdapter.FAQViewHolder>() {

    inner class FAQViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val question: TextView = itemView.findViewById(R.id.question)
        val answer: TextView = itemView.findViewById(R.id.answer)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FAQViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.faq_item, parent, false)
        return FAQViewHolder(view)
    }

    override fun onBindViewHolder(holder: FAQViewHolder, position: Int) {
        val item = faqList[position]
        holder.question.text = item.question
        holder.answer.text = item.answer

        // Toggle answer visibility on question click
        holder.question.setOnClickListener {
            if (holder.answer.visibility == View.GONE) {
                holder.answer.visibility = View.VISIBLE
                holder.question.setCompoundDrawablesWithIntrinsicBounds(R.drawable.minus, 0, 0, 0)
            } else {
                holder.answer.visibility = View.GONE
                holder.question.setCompoundDrawablesWithIntrinsicBounds(R.drawable.plusone, 0, 0, 0)
            }
        }
    }

    override fun getItemCount(): Int = faqList.size
}
