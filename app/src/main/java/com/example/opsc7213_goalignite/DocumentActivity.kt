package com.example.opsc7213_goalignite



import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import android.Manifest
import android.widget.ImageView
//SharedPreferences code adapted from Android Developers
//https://developer.android.com/training/data-storage/shared-preferences
//Android Developers-Save simple data with SharedPreferences (2024)

//File picker code adapted from Youtube Videos
//https://www.bing.com/videos/riverview/relatedvideo?q=file+picker+kotlin&mid=87279721F2A47FAF9CB487279721F2A47FAF9CB4&FORM=VIRE
//IT Wala- ANDROID - FILE PICKER || TUTORIAL IN KOTLIN (2023)
class DocumentActivity : AppCompatActivity() {

    private val PICK_FILE_REQUEST = 1 //Request code for file picker intent
    private lateinit var fileList: MutableList<Uri> //List of file URIs to store selected documents
    private lateinit var recyclerViewAdapter: DocumentAdapter //Adapter to display in ReceyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_document)

        //Retrives saved file URI from SharedPreferences
        fileList = getSavedFiles()


        // Initialize RecyclerView and set layout manager and adapter
        val recyclerView: RecyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = recyclerViewAdapter

        findViewById<ImageView>(R.id.add).setOnClickListener {//add files
            openFilePicker()
        }
    }

    private fun openFilePicker() { //opens file picker
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
        intent.addCategory(Intent.CATEGORY_OPENABLE) //makes sure that all files are visible
        intent.type = "*/*"
        startActivityForResult(intent, PICK_FILE_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_FILE_REQUEST && resultCode == RESULT_OK) {
            data?.data?.let { uri ->
                fileList.add(uri) //Adds selected file URI to lidt
                saveFiles(fileList) //Saves updated file list
                recyclerViewAdapter.notifyDataSetChanged() //Refreshes recycler view
            }
        }
    }

    private fun saveFiles(fileList: List<Uri>) {
        // Save the file list to SharedPreferences (or use SQLite if needed)
        val sharedPref = getSharedPreferences("documents", MODE_PRIVATE)
        val editor = sharedPref.edit()
        val fileString = fileList.joinToString(",") { it.toString() }
        editor.putString("fileList", fileString)
        editor.apply()
    }

    private fun getSavedFiles(): MutableList<Uri> {
        // Retrieve the saved files from SharedPreferences
        val sharedPref = getSharedPreferences("documents", MODE_PRIVATE)
        val fileString = sharedPref.getString("fileList", "") ?: ""
        return if (fileString.isNotEmpty()) {
            fileString.split(",").map { Uri.parse(it) }.toMutableList()
        } else {
            mutableListOf()
        }
    }
}

