package com.example.opsc7213_goalignite

import android.content.DialogInterface


//THIS WHOLE CODE WAS TAKEN FROM YOUTUBE
//https://www.youtube.com/watch?v=7u5_NNrbQos&list=PLzEWSvaHx_Z2MeyGNQeUCEktmnJBp8136
//Penguin Coders - TO-DO-LIST APPLICATION



// Interface to handle the closing of dialog interactions
interface DialogCloseListener {
    // Method to be implemented for handling dialog close events
    fun handleDialogClose(dialog: DialogInterface)
}