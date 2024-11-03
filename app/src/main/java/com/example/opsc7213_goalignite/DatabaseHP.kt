package com.example.opsc7213_goalignite

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import androidx.annotation.Nullable
import com.example.opsc7213_goalignite.model.GradeModel

//This whole code was taken from YOUTUBE
//https://www.youtube.com/watch?v=4ZFde7We0H8&list=PLNSnaPe-iLVKpRUqg1yv5JqM_4DQN8Ofo
// Jowel Ahmed - CONTACT APPLICATION
class DatabaseHP (@Nullable context: Context?) :
    SQLiteOpenHelper(context, DeConstants.DATABASE_NAME,null, DeConstants.DATABASE_VERSION)  {

    override fun onCreate(db: SQLiteDatabase) { // This method is called when the database is created for the first time.
        db.execSQL(DeConstants.CREATE_TABLE) // Execute the SQL command to create the Grades table using the CREATE_TABLE statement
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // Drop the old table if it exists
        db.execSQL("DROP TABLE IF EXISTS ${DeConstants.TABLE_NAME}")
        // Recreate the table
        onCreate(db)
    }

    fun insertGrade(module: String, mark: String): Long {
        // Get writable database instance
        val db = this.writableDatabase

        // Create ContentValues object to store data
        val contentValues = ContentValues().apply {
            put(DeConstants.C_MODULE, module)
            put(DeConstants.C_MARK, mark)

        }
        val id = db.insert(DeConstants.TABLE_NAME, null, contentValues)

        // Close the database
        db.close()

        // Return the inserted row ID
        return id
    }

    fun deleteAllGrades() {
        val db = this.writableDatabase
        db.execSQL("DELETE FROM ${DeConstants.TABLE_NAME}") // Deletes all rows from the table
        db.close()
    }

    fun getAllData(): ArrayList<GradeModel> {
        val arrayList = ArrayList<GradeModel>()
        val selectQuery = "SELECT * FROM ${DeConstants.TABLE_NAME}"

        val db = this.readableDatabase
        val cursor = db.rawQuery(selectQuery, null)

        if (cursor.moveToFirst()) {
            do {
                // Create an instance of ContactModel and set properties using setters
                val gradeModel = GradeModel()
                gradeModel.setId(cursor.getString(cursor.getColumnIndexOrThrow(DeConstants.C_ID)))
                gradeModel.setModule(cursor.getString(cursor.getColumnIndexOrThrow(DeConstants.C_MODULE)))
                gradeModel.setMark(cursor.getString(cursor.getColumnIndexOrThrow(DeConstants.C_MARK)))

                arrayList.add(gradeModel)
            } while (cursor.moveToNext())
        }

        cursor.close()
        db.close()

        return arrayList
    }
}
