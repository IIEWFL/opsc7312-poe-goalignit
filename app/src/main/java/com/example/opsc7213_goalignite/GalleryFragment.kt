package com.example.opsc7213_goalignite

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [GalleryFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class GalleryFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: GalleryAdapter
    private var mediaList = mutableListOf<Gallery>() // Holds MediaModel instances

    private val sharedPreferences by lazy {
        requireActivity().getSharedPreferences("GalleryPrefs", Context.MODE_PRIVATE)
    }

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
        intent.type = "*/*" // Allows all types first, but will restrict below
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        intent.putExtra(Intent.EXTRA_MIME_TYPES, arrayOf("image/*", "video/*")) // Images and Videos

        startActivityForResult(Intent.createChooser(intent, "Select Media"), 1000)
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
        val builder = androidx.appcompat.app.AlertDialog.Builder(requireContext())
        builder.setTitle("Enter Media Title")

        val input = EditText(requireContext())
        builder.setView(input)

        builder.setPositiveButton("OK") { dialog, _ ->
            val title = input.text.toString()
            val filePath = uri.toString()

            mediaList.add(Gallery(title, filePath))
            adapter.notifyDataSetChanged() // Notify adapter of data change
            saveMediaFiles() // Save the updated list
            dialog.dismiss()
        }
        builder.setNegativeButton("Cancel") { dialog, _ -> dialog.cancel() }

        builder.show()
    }

    // Function to open the media when clicked in RecyclerView
    private fun openMedia(media: Gallery) {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.setDataAndType(Uri.parse(media.filePath), "*/*")
        startActivity(intent)
    }
}
