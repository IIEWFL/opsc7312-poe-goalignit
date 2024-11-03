package com.example.opsc7213_goalignite.adapter


import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import com.example.opsc7213_goalignite.GradeDetail
import com.example.opsc7213_goalignite.R
import com.example.opsc7213_goalignite.model.GradeModel

class GradeAdapter(
    private val context: Context, // Context of the calling activity or fragment
    private val gradeList: ArrayList<GradeModel>// List of GradeModel items to display in the RecyclerView
) : RecyclerView.Adapter<GradeAdapter.GradeViewHolder>() {// Extending RecyclerView.Adapter with a ViewHolder

    // ViewHolder class to hold the views
    class GradeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val moduleName: TextView =itemView.findViewById(R.id.module_name)
        val mark: TextView = itemView.findViewById(R.id.mark)
    }

    @NonNull
    override fun onCreateViewHolder(@NonNull parent: ViewGroup, viewType: Int): GradeViewHolder {
        // Inflate the custom row layout
        val view = LayoutInflater.from(context).inflate(R.layout.row_grade_item, parent, false)
        return GradeViewHolder(view)
    }

    override fun onBindViewHolder(@NonNull holder: GradeViewHolder, position: Int) {
        // Get the current grade model
        val gradeModel = gradeList[position]

        // Set the data to the views
        holder.moduleName.text = gradeModel.getModule()
        holder.mark.text = gradeModel.getMark()
        // Set a placeholder or default image resource



        holder.itemView.setOnClickListener {
            val intent = Intent(context, GradeDetail::class.java)
            intent.putExtra("contactId", gradeModel.getId())
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return gradeList.size
    }

    fun refreshGradeList(newGradeList: ArrayList<GradeModel>) {
        // Update the contact list with the new data
        gradeList.clear()
        gradeList.addAll(newGradeList)

        // Notify the adapter that the data has changed
        notifyDataSetChanged()
    }
}