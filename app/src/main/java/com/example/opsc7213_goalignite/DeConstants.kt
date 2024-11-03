package com.example.opsc7213_goalignite



//This whole code was taken from YOUTUBE
//https://www.youtube.com/watch?v=4ZFde7We0H8&list=PLNSnaPe-iLVKpRUqg1yv5JqM_4DQN8Ofo
// Jowel Ahmed - CONTACT APPLICATION
object DeConstants {

    //database name
    const val DATABASE_NAME = "Grade_DB"
    const val DATABASE_VERSION = 2

    const val TABLE_NAME = "Grades"
    const val C_ID = "ID" // Unique identifier for each grade entry
    const val C_MODULE = "MODULE"// Name of the module associated with the grade
    const val C_MARK = "MARK"// The mark or grade received
    // SQL statement to create the Grades table
    const val CREATE_TABLE = """
        CREATE TABLE ${DeConstants.TABLE_NAME} (
            ${DeConstants.C_ID} INTEGER PRIMARY KEY AUTOINCREMENT, 
            $C_MODULE TEXT,
            $C_MARK TEXT
            
          )
          """
}