package com.example.opsc7213_goalignite
//THIS WHOLE CODE WAS TAKEN FROM YOUTUBE
//https://www.youtube.com/watch?v=7u5_NNrbQos&list=PLzEWSvaHx_Z2MeyGNQeUCEktmnJBp8136
//Penguin Coders - TO-DO-LIST APPLICATION
object Constants {

    // Database name and version
    const val DATABASE_NAME = "Contact_DB"
    const val DATABASE_VERSION = 1

    // Table column or field names
    const val TABLE_NAME = "Contacts"
    const val C_ID = "ID"
    const val C_NAME = "NAME"
    const val C_PHONE = "PHONE"
    const val C_EMAIL = "EMAIL"
    const val C_SUBJECT = "SUBJECT"

    // SQL query to create the table
    const val CREATE_TABLE = """
        CREATE TABLE $TABLE_NAME (
            $C_ID INTEGER PRIMARY KEY AUTOINCREMENT, 
            $C_NAME TEXT,
            $C_PHONE TEXT,
            $C_EMAIL TEXT,
            $C_SUBJECT TEXT
        )
    """
}