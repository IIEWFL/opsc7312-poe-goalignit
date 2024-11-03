package com.example.opsc7213_goalignite

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class GalleryFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: GalleryAdapter
    private var mediaList = mutableListOf<Gallery>()
    private val sharedPreferences by lazy {
        requireActivity().getSharedPreferences("GalleryPrefs", Context.MODE_PRIVATE)
    }

    private val firestore: FirebaseFirestore = Firebase.firestore
    private val storage: FirebaseStorage = Firebase.storage

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_gallery, container, false)

        val addMediaButton: ImageView = view.findViewById(R.id.add)
        recyclerView = view.findViewById(R.id.recycler_view_media)

        // Fetch and initialize the media list
        mediaList = getSavedMediaFiles().toMutableList()

        // Initialize the adapter with onItemClick listener
        adapter = GalleryAdapter(mediaList) { media ->
            openMedia(media) // Handle item click
        }

        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = adapter

        // Handle Add Media button click
        addMediaButton.setOnClickListener {
            openMediaPicker()
        }

        return view
    }

    // Function to get saved media files
    private fun getSavedMediaFiles(): List<Gallery> {
        val json = sharedPreferences.getString("media_list", null)
        return if (json != null) {
            val gson = Gson()
            val type = object : TypeToken<List<Gallery>>() {}.type
            gson.fromJson(json, type) ?: emptyList()
        } else {
            emptyList()
        }
    }

    // Function to save media files to SharedPreferences
    private fun saveMediaFiles() {
        val gson = Gson()
        val json = gson.toJson(mediaList)
        sharedPreferences.edit().putString("media_list", json).apply()
    }

    // Function to open media picker
    private fun openMediaPicker() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "*/*"
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        intent.putExtra(Intent.EXTRA_MIME_TYPES, arrayOf("image/*", "video/*"))
        startActivityForResult(Intent.createChooser(intent, "Select Media"), 1000)
    }
    // Function to open media in an external viewer
    private fun openMedia(media: Gallery) {
        val uri = Uri.parse(media.filePath) // Convert file path string to URI
        val intent = Intent(Intent.ACTION_VIEW)
        intent.setDataAndType(uri, getMimeType(uri)) // Set the MIME type based on the file URI
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION) // Ensure read permissions for URI

        // Check if there is an app that can handle this intent
        if (intent.resolveActivity(requireContext().packageManager) != null) {
            startActivity(intent)
        } else {
            Toast.makeText(context, "No app available to open this media", Toast.LENGTH_SHORT).show()
        }
    }

    // Helper function to get the MIME type of the file based on URI
    private fun getMimeType(uri: Uri): String? {
        return requireContext().contentResolver.getType(uri)
    }

    // Handling the result of media selection
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1000 && resultCode == AppCompatActivity.RESULT_OK) {
            val uri = data?.data ?: return
            showTitleInputDialog(uri) // Show dialog to enter title
        }
    }

    // Function to show dialog for entering title
    private fun showTitleInputDialog(uri: Uri) {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Enter Media Title")

        val input = EditText(requireContext())
        builder.setView(input)

        builder.setPositiveButton("OK") { dialog, _ ->
            val title = input.text.toString()
            saveMediaToFirebase(title, uri) // Save media to Firebase
            dialog.dismiss()
        }
        builder.setNegativeButton("Cancel") { dialog, _ -> dialog.cancel() }

        builder.show()
    }

    // Function to save media to Firebase Storage and Firestore
    private fun saveMediaToFirebase(title: String, uri: Uri) {
        val mediaRef = storage.reference.child("media/${System.currentTimeMillis()}_${uri.lastPathSegment}")
        val uploadTask = mediaRef.putFile(uri)

        uploadTask.addOnSuccessListener {
            mediaRef.downloadUrl.addOnSuccessListener { downloadUrl ->
                // Add the media info to Firestore
                val mediaData = mapOf(
                    "title" to title,
                    "url" to downloadUrl.toString()
                )
                firestore.collection("gallery").add(mediaData)
                    .addOnSuccessListener {
                        Toast.makeText(context, "Media uploaded and synced", Toast.LENGTH_SHORT).show()
                        mediaList.add(Gallery(title, downloadUrl.toString()))
                        adapter.notifyDataSetChanged()
                        saveMediaFiles() // Save the updated list locally
                    }
                    .addOnFailureListener {
                        Toast.makeText(context, "Failed to sync media", Toast.LENGTH_SHORT).show()
                    }
            }
        }.addOnFailureListener {
            Toast.makeText(context, "Failed to upload media", Toast.LENGTH_SHORT).show()
        }
    }
}
