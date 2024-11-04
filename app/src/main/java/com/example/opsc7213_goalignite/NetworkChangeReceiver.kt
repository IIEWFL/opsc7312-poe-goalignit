package com.example.opsc7213_goalignite

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast
import com.example.opsc7213_goalignite.utilis.DatabaseHandler
import com.example.opsc7213_goalignite.utilis.NetworkUtil.isNetworkAvailable

class NetworkChangeReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        // Logic to sync tasks can be triggered here when the network is available
        if (isNetworkAvailable(context)) {
            val dbHandler = DatabaseHandler(context)
            val unsyncedTasks = dbHandler.getUnsyncedTasks()
            Toast.makeText(context, "Network is back online! Syncing tasks...", Toast.LENGTH_SHORT).show()
            // Call your sync function here if you have one
        }
    }
}