package com.example.opsc7213_goalignite

import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView

//getCount() adapted from Android Developers
//https://developer.android.com/reference/kotlin/android/widget/Adapter#:~:text=Adapter%20|%20Android%20Developers.%20Essentials.%20Gemini%20in%20Android
//Android Developers
//Adapter to display list of documents
class DocumentAdapter(private val documentList: List<Document>, private val onItemClick: (Document) -> Unit) : RecyclerView.Adapter<DocumentAdapter.DocumentViewHolder>() {

    inner class DocumentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val fileNameTextView: TextView = itemView.findViewById(R.id.file_name) //displays file name in the recycler view


        //Binds the Document data to the ViewHolder's views.
        fun bind(document: Document) {
            fileNameTextView.text = document.fileName // Display file name in TextView
            itemView.setOnClickListener {
                onItemClick(document)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DocumentViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_document, parent, false)// Inflate the layout for the item (item_document.xml) and return a ViewHolder
        return DocumentViewHolder(view)
    }

    override fun onBindViewHolder(holder: DocumentViewHolder, position: Int) {
        holder.bind(documentList[position])
    }

    //returns documents by gettimng how many items are in data set
    override fun getItemCount() = documentList.size
}
