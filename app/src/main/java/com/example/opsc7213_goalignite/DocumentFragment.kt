package com.example.opsc7213_goalignite

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import android.app.Activity
import android.widget.Toast
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.drive.*
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import java.io.OutputStream

class DocumentFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: DocumentAdapter
    private var documentList = mutableListOf<Document>() // Stores documents in a list
    private val sharedPreferences by lazy {
        requireActivity().getSharedPreferences("DocumentPrefs", Context.MODE_PRIVATE)
    }
    private val firestore by lazy { FirebaseFirestore.getInstance() }
    private lateinit var driveClient: DriveResourceClient
    private val GOOGLE_SIGN_IN_REQUEST_CODE = 1001

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_documents, container, false)

        recyclerView = view.findViewById(R.id.recycler_view_files)
        documentList = getSavedFiles().toMutableList()

        // Initialize RecyclerView
        adapter = DocumentAdapter(documentList) { document -> openFile(document) }
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = adapter

        // Initialize Google Drive Sign-In
        setupGoogleSignIn()

        // Add Document Button
        val addFileButton: ImageView = view.findViewById(R.id.add)
        addFileButton.setOnClickListener { openFilePicker() }

        return view
    }

    // Google Drive Sign-In setup
    private fun setupGoogleSignIn() {
        val signInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestScopes(Drive.SCOPE_FILE)
            .requestEmail()
            .build()
        val client = GoogleSignIn.getClient(requireActivity(), signInOptions)
        startActivityForResult(client.signInIntent, GOOGLE_SIGN_IN_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == GOOGLE_SIGN_IN_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
        } else if (requestCode == FILE_PICKER_REQUEST_CODE && resultCode == AppCompatActivity.RESULT_OK) {
            val uri = data?.data ?: return
            val fileName = uri.lastPathSegment ?: "Unknown File"
            val filePath = uri.toString()

            // Add document to the list and notify adapter
            documentList.add(Document(fileName, filePath))
            adapter.notifyDataSetChanged()
            saveFiles()

            // Show "waiting to sync" message and start Google Drive upload
            Toast.makeText(context, "$fileName is waiting to sync...", Toast.LENGTH_SHORT).show()
            uploadToDrive(fileName, uri) // Upload to Google Drive
        }
    }

    private fun handleSignInResult(task: Task<GoogleSignInAccount>) {
        try {
            val account = task.getResult(ApiException::class.java)
            account?.let {
                driveClient = Drive.getDriveResourceClient(requireActivity(), account)
            }
        } catch (e: ApiException) {
            Toast.makeText(context, "Google Sign-In failed", Toast.LENGTH_SHORT).show()
        }
    }

    // Function to sync Document to Google Drive with messages
    private fun uploadToDrive(title: String, uri: Uri) {
        driveClient.createContents().addOnSuccessListener { driveContents ->
            val outputStream: OutputStream = driveContents.outputStream
            val inputStream = requireContext().contentResolver.openInputStream(uri)
            inputStream?.copyTo(outputStream)

            // Create metadata
            val metadata = MetadataChangeSet.Builder()
                .setTitle(title)
                .setMimeType("application/pdf") // Adjust MIME type as needed
                .build()

            // Upload to Google Drive
            driveClient.rootFolder
                .continueWithTask { task ->
                    task.result?.let { rootFolder ->
                        driveClient.createFile(rootFolder, metadata, driveContents)
                    }
                }
                .addOnSuccessListener { file ->
                    // Show "synced" message on success
                    Toast.makeText(context, "$title synced to Google Drive!", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener {
                    // Show "failed to sync" message on failure
                    Toast.makeText(context, "Failed to sync $title to Google Drive", Toast.LENGTH_SHORT).show()
                }
        }
    }

    // Other functions (e.g., getSavedFiles, saveFiles, openFilePicker, showTitleInputDialog, getFileName, openFile) remain unchanged...

    // Load saved files from SharedPreferences
    private fun getSavedFiles(): List<Document> {
        val json = sharedPreferences.getString("document_list", null)
        return if (json != null) {
            val gson = Gson()
            val type = object : TypeToken<List<Document>>() {}.type
            gson.fromJson(json, type)
        } else {
            emptyList()
        }
    }

    // Save files locally and sync with Firebase
    private fun saveFiles() {
        val gson = Gson()
        val json = gson.toJson(documentList)
        sharedPreferences.edit().putString("document_list", json).apply()

        documentList.forEach { document ->
            firestore.collection("documents")
                .document(document.fileName)
                .set(document, SetOptions.merge())
                .addOnSuccessListener {
                    Toast.makeText(requireContext(), "${document.fileName} synced to cloud!", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener {
                    Toast.makeText(requireContext(), "Failed to sync ${document.fileName}", Toast.LENGTH_SHORT).show()
                }
        }
    }

    private fun openFilePicker() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "*/*"
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        intent.putExtra(Intent.EXTRA_MIME_TYPES, arrayOf("application/pdf", "application/msword", "application/vnd.openxmlformats-officedocument.wordprocessingml.document"))

        startActivityForResult(Intent.createChooser(intent, "Select Document"), FILE_PICKER_REQUEST_CODE)
    }

    private fun showTitleInputDialog(uri: Uri) {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Enter File Title")

        val input = EditText(requireContext())
        builder.setView(input)

        builder.setPositiveButton("OK") { dialog, _ ->
            val title = input.text.toString()
            val filePath = uri.toString()

            documentList.add(Document(title, filePath))
            adapter.notifyDataSetChanged()
            saveFiles()
            dialog.dismiss()
        }
        builder.setNegativeButton("Cancel") { dialog, _ -> dialog.cancel() }

        builder.show()
    }

    private fun getFileName(uri: Uri): String? {
        var fileName: String? = null
        val cursor = requireActivity().contentResolver.query(uri, null, null, null, null)
        cursor?.use {
            if (it.moveToFirst()) {
                val nameIndex = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                fileName = it.getString(nameIndex)
            }
        }
        return fileName
    }

    private fun openFile(document: Document) {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.setDataAndType(Uri.parse(document.filePath), "*/*")
        startActivity(intent)
    }

    companion object {
        private const val FILE_PICKER_REQUEST_CODE = 1000
    }
}
