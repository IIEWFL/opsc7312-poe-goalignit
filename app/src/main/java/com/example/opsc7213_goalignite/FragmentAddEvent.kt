package com.example.opsc7213_goalignite

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.opsc7213_goalignite.AddNewTask.Companion.TAG
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.Calendar


class FragmentAddEvent : Fragment() {

    private lateinit var database: DatabaseReference
    private lateinit var eventsList: MutableList<Event>
    private lateinit var eventsViewModel: EventsViewModel
    private lateinit var selectedDate: String
    private lateinit var selectedTime: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        eventsViewModel = ViewModelProvider(requireActivity()).get(EventsViewModel::class.java)
        database = FirebaseDatabase.getInstance().getReference("events")
        eventsList = mutableListOf()

        val view = inflater.inflate(R.layout.fragment_add_event, container, false)

        // Initialize UI elements
        val nameEditText = view.findViewById<EditText>(R.id.nameEditText)
        val timeTextView = view.findViewById<TextView>(R.id.timeTextView)
        val dateTextView = view.findViewById<TextView>(R.id.dateTextView)
        val addButton = view.findViewById<Button>(R.id.addButton)

        // Date Picker
        dateTextView.setOnClickListener {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog = DatePickerDialog(
                requireContext(),
                { _, selectedYear, selectedMonth, selectedDay ->
                    selectedDate = String.format("%02d/%02d/%04d", selectedDay, selectedMonth + 1, selectedYear)
                    dateTextView.text = selectedDate // Update TextView with selected date
                },
                year,
                month,
                day
            )
            datePickerDialog.show()
        }

        // Time Picker
        timeTextView.setOnClickListener {
            val calendar = Calendar.getInstance()
            val hour = calendar.get(Calendar.HOUR_OF_DAY)
            val minute = calendar.get(Calendar.MINUTE)

            val timePickerDialog = TimePickerDialog(
                requireContext(),
                { _, selectedHour, selectedMinute ->
                    selectedTime = String.format("%02d:%02d", selectedHour, selectedMinute)
                    timeTextView.text = selectedTime // Update TextView with selected time
                },
                hour,
                minute,
                true
            )
            timePickerDialog.show()
        }

        // Add Button Click Listener
        addButton.setOnClickListener {
            val name = nameEditText.text.toString().trim()
            if (name.isNotEmpty() && ::selectedDate.isInitialized && ::selectedTime.isInitialized) {
                val eventId = database.push().key ?: ""
                val event = Event(id = eventId, name = name, date = selectedDate, time = selectedTime)

                addEventToFirebase(event)
                Toast.makeText(context, "Event Added", Toast.LENGTH_SHORT).show()
                parentFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, FragmentCalendar())
                    .addToBackStack(null)
                    .commit()
            } else {
                Toast.makeText(context, "Please fill all fields", Toast.LENGTH_SHORT).show()
            }
        }

        return view
    }

    private fun addEventToFirebase(event: Event) {
        database.child(event.id).setValue(event).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                eventsViewModel.addEvent(event)
                Log.d(TAG, "Event added successfully")
                // Clear fields after adding event
                clearFields()
            } else {
                Log.e(TAG, "Failed to add event", task.exception)
            }
        }
    }

    private fun clearFields() {
        val nameEditText = view?.findViewById<EditText>(R.id.nameEditText)
        nameEditText?.text?.clear()
        view?.findViewById<TextView>(R.id.timeTextView)?.text = "Select Time"
        view?.findViewById<TextView>(R.id.dateTextView)?.text = "Select Date"
        selectedDate = ""
        selectedTime = ""
    }
}
