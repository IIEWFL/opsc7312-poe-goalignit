package com.example.opsc7213_goalignite

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat


//This whole code was taken from YOUTUBE
//https://www.youtube.com/watch?v=4ZFde7We0H8&list=PLNSnaPe-iLVKpRUqg1yv5JqM_4DQN8Ofo
// Jowel Ahmed - CONTACT APPLICATION BUT CHANGED AS THE "ADD GRADE"


class GradeDetail : AppCompatActivity() {

    private lateinit var moduleTextView: TextView
    private lateinit var markTextView: TextView
    private lateinit var databaseHP: DatabaseHP
    private lateinit var goingArrow: ImageView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_grade_detail)

        // Initialize TextViews

        markTextView = findViewById(R.id.markTextView)
        moduleTextView = findViewById(R.id.moduleTextView)
        goingArrow = findViewById(R.id.goingArrow)





        goingArrow.setOnClickListener {
            finish()  // Close the current activity and go back to the previous one
        }
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        databaseHP = DatabaseHP(this)


        val gradeId = intent.getStringExtra("gradeId") ?: return
        loadDataById(gradeId)
    }
    private fun loadDataById(id: String) {
        // Query to find data by ID
        val selectQuery = "SELECT * FROM ${DeConstants.TABLE_NAME} WHERE ${DeConstants.C_ID}=\"$id\""

        val db = databaseHP.readableDatabase
        val cursor = db.rawQuery(selectQuery, null)

        if (cursor.moveToFirst()) {
            moduleTextView.text = cursor.getString(cursor.getColumnIndexOrThrow(DeConstants.C_MODULE))
            markTextView.text = cursor.getString(cursor.getColumnIndexOrThrow(DeConstants.C_MARK))
        }
        cursor.close()
        db.close()
    }
}
