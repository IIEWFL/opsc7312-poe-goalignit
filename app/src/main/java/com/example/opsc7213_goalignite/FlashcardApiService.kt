package com.example.opsc7213_goalignite

import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

//API endpoint code adapted from Medium
//https://medium.com/@imkuldeepsinghrai/api-calls-with-retrofit-in-android-kotlin-a-comprehensive-guide-e049e19deba9#:~:text=In%20this%20article,%20we%20will%20explore%20the%20ins%20and%20outs
//Kuldeep Singh Rai-API Calls with Retrofit in Android Kotlin: A Comprehensive Guide(2023)
interface FlashcardApiService {
    @POST("api/flashcards") //sends post request to create flashcard
    suspend fun createFlashcard(@Body flashcard: Flashcard): Response<Flashcard>

    @GET("api/flashcards") //sends post request to create list
    suspend fun getFlashcards(): Response<List<Flashcard>>

    @PUT("api/flashcards/{id}")
    suspend fun updateFlashcard(@Path("id") id: String, @Body flashcard: Flashcard): Response<Flashcard>

    @DELETE("api/flashcards/{id}")
    suspend fun deleteFlashcard(@Path("id") id: String): Response<Unit>
}


