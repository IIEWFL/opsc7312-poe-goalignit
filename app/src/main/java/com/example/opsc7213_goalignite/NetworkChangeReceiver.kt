package com.example.opsc7213_goalignite

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast
import com.example.opsc7213_goalignite.utilis.NetworkUtil.isNetworkAvailable
//Code adapted from Medium
//Listen To Internet Connection Using BroadcastReceiver in Android — Kotlin by Dilip Suthar (2019)
//https://medium.com/@dilipsuthar97/listen-to-internet-connection-using-broadcastreceiver-in-android-kotlin-6b527426a6f2
class NetworkChangeReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        // Logic to sync tasks can be triggered here when the network is available
        if (isNetworkAvailable(context)) {

            Toast.makeText(context, "Network is back online! Syncing tasks...", Toast.LENGTH_SHORT).show()
             }
    }
}