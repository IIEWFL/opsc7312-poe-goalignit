package com.example.opsc7213_goalignite

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.opsc7213_goalignite.adapter.ToDoAdapter

//THIS WHOLE CODE WAS TAKEN FROM YOUTUBE
//https://www.youtube.com/watch?v=7u5_NNrbQos&list=PLzEWSvaHx_Z2MeyGNQeUCEktmnJBp8136
//Penguin Coders - TO-DO-LIST APPLICATION




// This class implements swipe functionality for items in a RecyclerView
class RecyclerItemTouchHelper(
    private val context: Context,// Context to show dialogs and get resources
    private val adapter: ToDoAdapter// Adapter to interact with the RecyclerView items
) : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {

    private val backgroundCornerOffset = 20 // Offset for the background corners
    private lateinit var icon: Drawable // Icon to be displayed during the swipe
    private lateinit var background: ColorDrawable // Background color for the swipe action

    // This method is called when an item is moved
    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        return false // No movement functionality is needed
    }

    // This method is called when an item is swiped
    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        val position = viewHolder.adapterPosition

        if (direction == ItemTouchHelper.LEFT) {
            // Show confirmation dialog to delete the task
            val builder = AlertDialog.Builder(context)
            builder.setTitle("Delete Task")
            builder.setMessage("Are you sure you want to delete this task?")
            builder.setPositiveButton("Confirm") { _, _ ->
                adapter.deleteItem(position) // Delete task
            }
            builder.setNegativeButton(android.R.string.cancel) { dialog, _ ->
                adapter.notifyItemChanged(viewHolder.adapterPosition) // Reset the item view
                dialog.dismiss()
            }
            val dialog = builder.create()
            dialog.show()
        } else {
            // Edit the task
            adapter.editItem(position)
        }
    }
    // This method is called to draw the item view while it is being swiped
    override fun onChildDraw(
        c: Canvas, recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder,
        dX: Float, dY: Float, actionState: Int, isCurrentlyActive: Boolean
    ) {
        val itemView = viewHolder.itemView

        if (dX > 0) { // Swiping to the right
            icon = ContextCompat.getDrawable(context, R.drawable.baseline_edit)!!
            background = ColorDrawable(ContextCompat.getColor(context, R.color.baby_blue))
            background.setBounds(itemView.right - dX.toInt() - backgroundCornerOffset, itemView.top, itemView.right, itemView.bottom)
        } else { // Swiping to the left
            icon = ContextCompat.getDrawable(context, R.drawable.baseline_delete)!!
            background = ColorDrawable(Color.RED)
            background.setBounds(itemView.left, itemView.top, itemView.left + dX.toInt() + backgroundCornerOffset, itemView.bottom)
        }

        // Draw the background
        background.draw(c)

        // Calculate icon position
        val iconMargin = (itemView.height - icon.intrinsicHeight) / 2
        val iconTop = itemView.top + (itemView.height - icon.intrinsicHeight) / 2
        val iconBottom = iconTop + icon.intrinsicHeight

        // Draw the icon
        if (dX > 0) { // Swiping to the right
            val iconLeft = itemView.right - iconMargin - icon.intrinsicWidth
            val iconRight = itemView.right - iconMargin
            icon.setBounds(iconLeft, iconTop, iconRight, iconBottom)
            icon.draw(c)
        } else { // Swiping to the left
            val iconLeft = itemView.left + iconMargin
            val iconRight = itemView.left + iconMargin + icon.intrinsicWidth
            icon.setBounds(iconLeft, iconTop, iconRight, iconBottom)
            icon.draw(c)
        }

        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
    }
}