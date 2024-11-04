package com.example.opsc7213_goalignite.utilis

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
//Code adapted from Medium
//Listen To Internet Connection Using BroadcastReceiver in Android — Kotlin by Dilip Suthar (2019)
//https://medium.com/@dilipsuthar97/listen-to-internet-connection-using-broadcastreceiver-in-android-kotlin-6b527426a6f2

// Utility class to check network availability status
object NetworkUtil {

    // Function to check if the network is available on the device
    fun isNetworkAvailable(context: Context): Boolean {
        // Get the ConnectivityManager from the system services
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        // Check network status based on the device's Android version
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // For Android Marshmallow (API 23) and above, use NetworkCapabilities to determine network type
            val networkCapabilities = connectivityManager.activeNetwork?.let {
                connectivityManager.getNetworkCapabilities(it)
            }
            // Return true if either Wi-Fi or Cellular network is available
            networkCapabilities != null && (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                    networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR))
        } else {
            // For Android versions below Marshmallow, use the older method with activeNetworkInfo
            val activeNetworkInfo = connectivityManager.activeNetworkInfo
            // Return true if there is an active network connection
            activeNetworkInfo != null && activeNetworkInfo.isConnected
        }
    }
}
