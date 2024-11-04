package com.example.opsc7213_goalignite

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.UUID

/**
 * A simple [Fragment] subclass.
 * Use the [FlashcardFragment.newInstance] factory method to
 * create an instance of this fragment.
 */

    //API calls code adapted from Medium
//https://medium.com/@imkuldeepsinghrai/api-calls-with-retrofit-in-android-kotlin-a-comprehensive-guide-e049e19deba9#:~:text=In%20this%20article,%20we%20will%20explore%20the%20ins%20and%20outs
//Kuldeep Singh Rai-API Calls with Retrofit in Android Kotlin: A Comprehensive Guide(2023)
    //Coroutines adapted from Android Developers
    //https://developer.android.com/kotlin/coroutines#:~:text=A%20coroutine%20is%20a%20concurrency%20design%20pattern%20that%20you%20can#:~:text=A%20coroutine%20is%20a%20concurrency%20design%20pattern%20that%20you%20can
    //Android Developers-Kotlin coroutines on Android
class FlashcardFragment : Fragment() {
        private lateinit var api: FlashcardApiService
        private lateinit var createButton: Button
        private lateinit var questionInput: EditText
        private lateinit var answerInput: EditText
        private lateinit var moduleInput: EditText
        private lateinit var flashcardContainer: LinearLayout

        override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View? {
            // Inflate the layout for this fragment
            val view = inflater.inflate(R.layout.fragment_flashcard, container, false)

            createButton = view.findViewById(R.id.createButton)
            questionInput = view.findViewById(R.id.questionInput)
            answerInput = view.findViewById(R.id.answerInput)
            moduleInput = view.findViewById(R.id.moduleInput)
            flashcardContainer = view.findViewById(R.id.flashcardContainer)

            val logging = HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            }

            val okHttpClient = OkHttpClient.Builder()
                .addInterceptor(logging)
                .build()

            val retrofit = Retrofit.Builder()
                .baseUrl("http://10.0.2.2:3004/")
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            api = retrofit.create(FlashcardApiService::class.java)

            createButton.setOnClickListener {
                val module = moduleInput.text.toString()
                val question = questionInput.text.toString()
                val answer = answerInput.text.toString()

                if (module.isNotEmpty() && question.isNotEmpty() && answer.isNotEmpty()) {
                    val flashcard = Flashcard(id = UUID.randomUUID().toString(), module, question, answer)
                    saveFlashcard(flashcard)
                }
            }
            loadFlashcards()

            return view
        }

        private fun saveFlashcard(flashcard: Flashcard) {
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val response = api.createFlashcard(flashcard)
                    if (response.isSuccessful) {
                        val createdFlashcard = response.body()
                        if (createdFlashcard != null) {
                            withContext(Dispatchers.Main) {
                                displayFlashcard(createdFlashcard)
                            }
                        } else {
                            Log.e("FlashcardFragment", "Response was successful but body is null")
                        }
                    } else {
                        Log.e("FlashcardFragment", "Error: ${response.errorBody()?.string()}")
                    }
                } catch (e: Exception) {
                    Log.e("FlashcardFragment", "Exception: ${e.message}")
                }
            }
        }

        private fun updateFlashcard(flashcard: Flashcard) {
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    flashcard.id?.let { nonNullId ->
                        val response = api.updateFlashcard(nonNullId, flashcard)
                        if (response.isSuccessful) {
                            Log.d("FlashcardFragment", "Flashcard updated successfully")
                        } else {
                            Log.e("FlashcardFragment", "Error updating flashcard: ${response.errorBody()?.string()}")
                        }
                    } ?: Log.e("FlashcardFragment", "Flashcard ID is null, cannot update")
                } catch (e: Exception) {
                    Log.e("FlashcardFragment", "Exception: ${e.message}")
                }
            }
        }

        private fun deleteFlashcard(id: String) {
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val response = api.deleteFlashcard(id)
                    if (response.isSuccessful) {
                        withContext(Dispatchers.Main) {
                            // Remove the flashcard from the UI if necessary
                            Log.d("FlashcardFragment", "Flashcard deleted successfully")
                        }
                    } else {
                        Log.e("FlashcardFragment", "Error deleting flashcard: ${response.errorBody()?.string()}")
                    }
                } catch (e: Exception) {
                    Log.e("FlashcardFragment", "Exception: ${e.message}")
                }
            }
        }

        private fun loadFlashcards() {
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val response = api.getFlashcards()
                    if (response.isSuccessful) {
                        val flashcards = response.body()
                        if (flashcards != null) {
                            withContext(Dispatchers.Main) {
                                flashcards.forEach { displayFlashcard(it) }
                            }
                        } else {
                            Log.e("FlashcardFragment", "No flashcards found")
                        }
                    } else {
                        Log.e("FlashcardFragment", "Error loading flashcards: ${response.errorBody()?.string()}")
                    }
                } catch (e: Exception) {
                    Log.e("FlashcardFragment", "Exception: ${e.message}")
                }
            }
        }

        private fun displayFlashcard(flashcard: Flashcard) {
            // Create a new CardView for each flashcard
            val cardView = CardView(requireContext()).apply {
                radius = 8f
                setContentPadding(16, 16, 16, 16)
                setCardBackgroundColor(Color.parseColor("#ADD8E6")) // Light Blue color
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                ).apply {
                    setMargins(16, 16, 16, 16)
                }
            }

            // Create a LinearLayout to hold the flashcard details
            val flashcardLayout = LinearLayout(requireContext()).apply {
                orientation = LinearLayout.VERTICAL
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
            }

            // TextView for displaying the module
            val moduleTextView = TextView(requireContext()).apply {
                text = flashcard.module
                setPadding(16, 16, 16, 16)
                setTextColor(Color.DKGRAY)
                textSize = 19f
            }

            // TextView for displaying the question
            val questionTextView = TextView(requireContext()).apply {
                text = flashcard.question
                setPadding(16, 16, 16, 16)
                setTextColor(Color.BLACK)
                textSize = 16f
            }

            // TextView for displaying the answer, initially hidden
            val answerTextView = TextView(requireContext()).apply {
                text = flashcard.answer
                setPadding(16, 16, 16, 16)
                setTextColor(Color.BLACK)
                textSize = 16f
                // Initially hidden
                visibility = View.GONE
            }

            // Button for editing the flashcard
            val editButton = Button(requireContext()).apply {
                text = "Edit"
                setOnClickListener {
                    questionInput.setText(flashcard.question)
                    answerInput.setText(flashcard.answer)
                    moduleInput.setText(flashcard.module)
                    createButton.text = "Update"

                    createButton.setOnClickListener {
                        val updatedFlashcard = Flashcard(
                            id = flashcard.id,
                            module = moduleInput.text.toString(),
                            question = questionInput.text.toString(),
                            answer = answerInput.text.toString()
                        )

                        updateFlashcard(updatedFlashcard)
                        createButton.text = "Create" // Reset button text
                    }
                }
            }

            // Button for deleting the flashcard
            val deleteButton = Button(requireContext()).apply {
                text = "Delete"
                setOnClickListener {
                    flashcard.id?.let { id -> deleteFlashcard(id) }
                    flashcardContainer.removeView(cardView) // Remove from UI
                }
            }

            // Add the module, question, and answer to the LinearLayout in the correct order
            flashcardLayout.addView(moduleTextView)  // Module first
            flashcardLayout.addView(questionTextView) // Then the question
            flashcardLayout.addView(answerTextView)   // Finally, the answer
            flashcardLayout.addView(editButton)        // Add edit button
            flashcardLayout.addView(deleteButton)      // Add delete button

            // Add the LinearLayout to the CardView
            cardView.addView(flashcardLayout)

            // Toggle answer visibility on card click
            cardView.setOnClickListener {
                answerTextView.visibility = if (answerTextView.visibility == View.GONE) {
                    View.VISIBLE
                } else {
                    View.GONE
                }
            }

            // Add the CardView to the flashcard container
            flashcardContainer.addView(cardView)
        }
    }
