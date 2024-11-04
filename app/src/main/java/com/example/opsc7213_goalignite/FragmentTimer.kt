package com.example.opsc7213_goalignite



//Code adapted from Learnoset - Learn Coding Online "How to create online timer in Android Studio"
// available at https://www.youtube.com/@learnoset-learncodingonlin600


import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ListView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.opsc7213_goalignite.adapter.MusicAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.util.Locale

class FragmentTimer : Fragment() {

    private var mediaPlayer: MediaPlayer? = null
    private lateinit var musicListView: ListView

    // List of music file names and their corresponding raw resource IDs
    private val musicList =
        listOf("Sunsent", "Beauty", "Warm Memories", "White Petals", "Magical Moments")
    private val musicFiles = listOf(
        R.raw.sunset,
        R.raw.beauty,
        R.raw.warm_memories,
        R.raw.white__petals,
        R.raw.magical_moments
    )
    private val musicImages = listOf(
        R.drawable.sun,
        R.drawable.beauty,
        R.drawable.memories,
        R.drawable.pink,
        R.drawable.magics
    )
    private var duration: Int = 120 // duration in seconds
    private var running = false
    private var wasRunning = false
    private var seconds = 0
    private lateinit var textView10: TextView
    private lateinit var handler: Handler


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        savedInstanceState?.let {
            // Restore the 'seconds' value from the saved instance state to continue counting from the previous state
            seconds = it.getInt("seconds")

// Restore the 'running' state to check if the timer was running before the activity was recreated
            running = it.getBoolean("running")

// Restore the 'wasRunning' state to determine if the timer should resume running after recreation
            wasRunning = it.getBoolean("wasRunning")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_timer, container, false)

        // Initialize the ListView and set up the adapter
        musicListView = view.findViewById(R.id.music_list_view)
        textView10 = view.findViewById(R.id.textView10)
        val startButton: FloatingActionButton = view.findViewById(R.id.start)
        val stopButton: FloatingActionButton = view.findViewById(R.id.stop)
        val resetButton: FloatingActionButton = view.findViewById(R.id.reset)


        val adapter = MusicAdapter(requireContext(), musicList, musicImages)
        musicListView.adapter = adapter

        // Set up item click listener to play the selected song
        musicListView.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
            // Stop any currently playing music before starting new one
            mediaPlayer?.release()

            // Initialize MediaPlayer with the selected music file
            mediaPlayer = MediaPlayer.create(requireContext(), musicFiles[position])
            mediaPlayer?.start()


            // Create a handler to manage and post actions to a thread
            handler = Handler()

// Set up the Start button click listener to start the timer by setting 'running' to true
            startButton.setOnClickListener {
                running = true
            }

// Set up the Stop button click listener to stop the timer by setting 'running' to false
            stopButton.setOnClickListener {
                running = false
            }

// Set up the Reset button click listener to stop the timer and reset the seconds counter to 0
            resetButton.setOnClickListener {
                running = false
                seconds = 0
            }

// Start the timer function to keep track of elapsed time
            runTimer()
        }

// Return the view to display it on the screen
        return view
    }

    override fun onDestroy() {
        super.onDestroy()
        // Release MediaPlayer resources when the fragment is destroyed
        mediaPlayer?.release()
        mediaPlayer = null
    }

    // Function to run the timer, updating every second
    private fun runTimer() {
        // Post a runnable to the handler to execute a repetitive task
        handler.post(object : Runnable {
            override fun run() {
                // Calculate the number of hours by dividing total seconds by 3600 (seconds in an hour)
                val hours = seconds / 3600
                // Calculate the number of minutes by finding the remainder of hours and dividing by 60
                val minutes = (seconds % 3600) / 60
                // Calculate the remaining seconds after hours and minutes
                val secs = seconds % 60

                // Format the time as a string in "HH:MM:SS" format and set it to the text view
                val time = String.format(Locale.getDefault(), "%d:%02d:%02d", hours, minutes, secs)
                textView10.text = time

                // If the timer is running, increment the seconds count by 1
                if (running) {
                    seconds++
                }

                // Schedule the runnable to run again after 1 second (1000 milliseconds)
                handler.postDelayed(this, 1000)
            }
        })
    }

    // Save the instance state to preserve data when the activity is recreated (e.g., on rotation)
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        // Save the current seconds count in the outState Bundle
        outState.putInt("seconds", seconds)
        // Save the running state to indicate if the timer is currently running
        outState.putBoolean("running", running)
        // Save the wasRunning state to track if the timer was running before the activity was paused
        outState.putBoolean("wasRunning", wasRunning)
    }

    // Handle the activity being paused (e.g., when the user navigates away)
    override fun onPause() {
        super.onPause()

        // Store the current running state in wasRunning
        wasRunning = running
        // Set running to false to pause the timer while the activity is not in the foreground
        running = false
    }

    // Handle the activity resuming (e.g., when the user returns to the app)
    override fun onResume() {
        super.onResume()

        // Restore the running state if the timer was running before the activity was paused
        if (wasRunning) {
            running = true
        }
    }
}

