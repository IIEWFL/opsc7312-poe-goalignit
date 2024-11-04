package com.example.opsc7213_goalignite

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.opsc7213_goalignite.utilis.NetworkUtil
//Code adapted from Medium
//Listen To Internet Connection Using BroadcastReceiver in Android — Kotlin by Dilip Suthar (2019)
//https://medium.com/@dilipsuthar97/listen-to-internet-connection-using-broadcastreceiver-in-android-kotlin-6b527426a6f2
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
