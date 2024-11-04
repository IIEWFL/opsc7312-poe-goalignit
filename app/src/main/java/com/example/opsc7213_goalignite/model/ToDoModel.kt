package com.example.opsc7213_goalignite.model

data class ToDoModel(
    var id: Int = 0,// Unique identifier for the To-Do item
    var status: Int = 0,  // Completion status (0: incomplete, 1: complete)
    var task: String = "",// Description of the To-Do item
    var synced: Boolean = false
)



