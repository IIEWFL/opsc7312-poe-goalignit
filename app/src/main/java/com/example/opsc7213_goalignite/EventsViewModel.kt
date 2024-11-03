package com.example.opsc7213_goalignite

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore

class EventsViewModel : ViewModel() {
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance() // Initialize Firestore
    private val _events = MutableLiveData<List<Event>>()
    val events: LiveData<List<Event>> get() = _events

    fun loadEvents() {
        FirebaseFirestore.getInstance().collection("events")
            .get()
            .addOnSuccessListener { result ->
                val eventsList = result.toObjects(Event::class.java)
                _events.value = eventsList
            }
        db.collection("events").get().addOnSuccessListener { result ->
            _events.value = result.toObjects(Event::class.java)
        }.addOnFailureListener { exception ->
            Log.e("EventsViewModel", "Error loading events: ", exception)
        }
    }

    fun addEvent(event: Event) {
        db.collection("events")
            .add(event)
            .addOnSuccessListener { documentReference ->
                Log.d("EventsViewModel", "Event added with ID: ${documentReference.id}")
                loadEvents() // Refresh the event list after adding a new event
            }
            .addOnFailureListener { e ->
                Log.e("EventsViewModel", "Error adding event", e)
            }
    }
}
