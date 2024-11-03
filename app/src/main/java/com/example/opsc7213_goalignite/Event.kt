package com.example.opsc7213_goalignite

import com.google.firebase.firestore.PropertyName

data class Event(
    @get:PropertyName("id") @set:PropertyName("id") var id: String = "", // Firestore document ID
    @get:PropertyName("name") @set:PropertyName("name") var name: String = "",
    @get:PropertyName("date") @set:PropertyName("date") var date: String = "",
    @get:PropertyName("time") @set:PropertyName("time") var time: String = ""
)
