package com.example.opsc7213_goalignite
//data class for flashcards
data class Flashcard(
    val id: String? = null, //unique identifier for flashcard
    val question: String,
    val answer: String,  //Flashcard creation data
    val module: String
)
