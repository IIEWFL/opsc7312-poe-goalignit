package com.example.opsc7213_goalignite

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.opsc7213_goalignite.utilis.NetworkUtil

class ConnectivityReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if (NetworkUtil.isNetworkAvailable(context!!)) {
            // Sync pending contacts
            syncPendingContacts(context)
        }
    }

    private fun syncPendingContacts(context: Context) {
        val dbHelper = DbHelper(context)
        // Get all pending contacts from local database
        val pendingContacts = dbHelper.getAllData() // Assuming these are contacts waiting to sync
        for (contact in pendingContacts) {
            dbHelper.syncContactToCloud(contact)
        }
    }
}
