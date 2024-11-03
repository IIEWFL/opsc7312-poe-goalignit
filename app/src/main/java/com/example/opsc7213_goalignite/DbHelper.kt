package com.example.opsc7213_goalignite

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import androidx.annotation.Nullable
import com.example.opsc7213_goalignite.model.ContactModel
import com.example.opsc7213_goalignite.utilis.NetworkUtil
import com.google.firebase.firestore.FirebaseFirestore

//THIS WHOLE CODE WAS TAKEN FROM YOUTUBE
//https://www.youtube.com/watch?v=7u5_NNrbQos&list=PLzEWSvaHx_Z2MeyGNQeUCEktmnJBp8136
//Penguin Coders - TO-DO-LIST APPLICATION


// Class for database helper
class DbHelper( context: Context) : SQLiteOpenHelper(context, Constants.DATABASE_NAME, null, Constants.DATABASE_VERSION) {
    private val mContext: Context = context
    override fun onCreate(db: SQLiteDatabase) {


        // Create table on database
        db.execSQL(Constants.CREATE_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // Drop the old table if it exists
        db.execSQL("DROP TABLE IF EXISTS ${Constants.TABLE_NAME}")
        // Recreate the table
        onCreate(db)
    }

    // Insert function to insert data in the database
    fun insertContact(name: String, phone: String, email: String, subject: String): Long {
        // Get writable database instance
        val db = this.writableDatabase

        // Create ContentValues object to store data
        val contentValues = ContentValues().apply {
            put(Constants.C_NAME, name)
            put(Constants.C_PHONE, phone)
            put(Constants.C_EMAIL, email)
            put(Constants.C_SUBJECT, subject)
        }

        // Insert data into the table and return the ID of the inserted row
        val id = db.insert(Constants.TABLE_NAME, null, contentValues)

        // Close the database
        db.close()

        // Sync to cloud if online
        if (NetworkUtil.isNetworkAvailable(mContext)) {
            syncContactToCloud(ContactModel().apply {
                setId(id.toString())
                setName(name)
                setPhone(phone)
                setEmail(email)
                setSubject(subject)
            })
        }


        // Return the inserted row ID
        return id
    }
    fun syncContactToCloud(contact: ContactModel) {
        val db = FirebaseFirestore.getInstance()
        val contactData = hashMapOf(
            "name" to contact.getName(),
            "phone" to contact.getPhone(),
            "email" to contact.getEmail(),
            "subject" to contact.getSubject()
        )

        db.collection("contacts").document(contact.getId() ?: "").set(contactData)
            .addOnSuccessListener {
                Log.d("Firestore", "Contact successfully written!")
            }
            .addOnFailureListener { e ->
                Log.w("Firestore", "Error writing contact", e)
            }
    }


    // Function to delete a contact by ID
    fun deleteAllContacts() {
        val db = this.writableDatabase
        db.execSQL("DELETE FROM ${Constants.TABLE_NAME}") // Deletes all rows from the table
        db.close()
    }


    //get data
    // Define the method to fetch all contacts
    fun getAllData(): ArrayList<ContactModel> {
        val arrayList = ArrayList<ContactModel>()
        val selectQuery = "SELECT * FROM ${Constants.TABLE_NAME}"

        val db = this.readableDatabase
        val cursor = db.rawQuery(selectQuery, null)

        if (cursor.moveToFirst()) {
            do {
                // Create an instance of ContactModel and set properties using setters
                val contactModel = ContactModel()
                contactModel.setId(cursor.getString(cursor.getColumnIndexOrThrow(Constants.C_ID)))
                contactModel.setName(cursor.getString(cursor.getColumnIndexOrThrow(Constants.C_NAME)))
                contactModel.setPhone(cursor.getString(cursor.getColumnIndexOrThrow(Constants.C_PHONE)))
                contactModel.setEmail(cursor.getString(cursor.getColumnIndexOrThrow(Constants.C_EMAIL)))
                contactModel.setSubject(cursor.getString(cursor.getColumnIndexOrThrow(Constants.C_SUBJECT)))

                arrayList.add(contactModel)
            } while (cursor.moveToNext())
        }

        cursor.close()
        db.close()

        return arrayList
    }
}