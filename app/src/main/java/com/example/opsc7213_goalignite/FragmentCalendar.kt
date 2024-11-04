package com.example.opsc7213_goalignite

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CalendarView
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
//Code adapted from Android developers
//Calender
//https://developer.android.com/reference/kotlin/android/icu/util/Calendar
class FragmentCalendar : Fragment() {

    private lateinit var db: FirebaseFirestore
    private lateinit var taskAdapter: TaskAdapter
    private lateinit var eventsViewModel: EventsViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        eventsViewModel = ViewModelProvider(requireActivity()).get(EventsViewModel::class.java)

        eventsViewModel.loadEvents() // Load events when the fragment is created

        val view = inflater.inflate(R.layout.fragment_calendar, container, false)

        db = FirebaseFirestore.getInstance()

        val calendarView = view.findViewById<CalendarView>(R.id.calendarView) // Initialize calendarView here
        val recyclerView = view.findViewById<RecyclerView>(R.id.taskRecyclerView)

        recyclerView.layoutManager = LinearLayoutManager(context)
        taskAdapter = TaskAdapter(mutableListOf()) // Initialize with an empty MutableList
        recyclerView.adapter = taskAdapter

        calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
            val selectedDate = String.format("%04d-%02d-%02d", year, month + 1, dayOfMonth) // Adjust format to match Firestore
            getEventsByDate(selectedDate)
        }

        val addEvent = view.findViewById<ImageView>(R.id.addevent)
        addEvent.setOnClickListener {
            val fragmentAddEvent = FragmentAddEvent()
            parentFragmentManager.beginTransaction()
                .replace(
                    R.id.fragment_container,
                    fragmentAddEvent
                ) // Change R.id.fragment_container to your actual container ID
                .addToBackStack(null) // Add to back stack to allow navigation back
                .commit()
        }


        eventsViewModel.events.observe(viewLifecycleOwner) { events ->
            Log.d("FragmentCalendar", "Observed events: $events")
            taskAdapter.updateTasks(events)
        }


        return view
    }

    private fun getEventsByDate(date: String) {
        db.collection("events")
            .whereEqualTo("date", date)
            .get()
            .addOnSuccessListener { result ->

                val events = result.toObjects(Event::class.java).toMutableList() // Convert to MutableList
                Log.d("FragmentCalendar", "Fetched events: $events") // Log fetched events
                taskAdapter.updateTasks(events) // Update the adapter with fetched events
            }
            .addOnFailureListener { exception ->
                Log.e("FragmentCalendar", "Error getting events: ", exception)
                taskAdapter.updateTasks(emptyList()) // Pass empty list if there's an error
            }
    }

}
