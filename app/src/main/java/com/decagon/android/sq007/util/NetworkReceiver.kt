package com.decagon.android.sq007.util

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.widget.Toast
import com.decagon.android.sq007.R

class NetworkReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val conn = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo: NetworkInfo? = conn.activeNetworkInfo

        // Checks the network connection. Based on the result, decides whether
        // to refresh the display or keep the current display.
        if (networkInfo != null) {
            refreshDisplay = true
            Toast.makeText(context, R.string.connected, Toast.LENGTH_SHORT).show()
            // Otherwise, the app can't download content--either because there is no network
            // connection (mobile or Wi-Fi)
            // Sets refreshDisplay to false.
        } else {
            refreshDisplay = false
            Toast.makeText(context, R.string.lost_connection, Toast.LENGTH_SHORT).show()
        }
    }

    companion object {

        const val WIFI = "Wi-Fi"

        // Whether there is a Wi-Fi connection.
        var wifiConnected = false

        // Whether there is a mobile connection.
        var mobileConnected = false

        // Whether the display should be refreshed.
        var refreshDisplay = true
    }
}
