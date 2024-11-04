package com.example.opsc7213_goalignite

import android.content.Context
import android.content.SharedPreferences
import java.util.*

//SharedPreferences code adapted from Android Developers
//https://developer.android.com/training/data-storage/shared-preferences
//Android Developers-Save simple data with SharedPreferences (2024)


class BadgeManager(private val context: Context) {
    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("badge_prefs", Context.MODE_PRIVATE)

    fun getBadges(): List<Badge> {
        val badges = mutableListOf<Badge>()
        val currentWeek = Calendar.getInstance().get(Calendar.WEEK_OF_YEAR)

        for (i in 1..7) { // 1 to 7 for Sunday to Saturday
            val isActive = sharedPreferences.getBoolean("badge_$i", false)
            badges.add(Badge(dayOfWeek = i, isActive = isActive))
        }

        // Check if a new week has started
        val lastWeek = sharedPreferences.getInt("last_week", -1)
        if (lastWeek != currentWeek) {
            resetBadges()
            sharedPreferences.edit().putInt("last_week", currentWeek).apply()
        }

        return badges
    }

    fun setBadgeActive(dayOfWeek: Int) {
        // Update the active status of the badge in SharedPreferences
        sharedPreferences.edit().putBoolean("badge_$dayOfWeek", true).apply()
    }

    // Method to reset all badges to inactive (called at the start of a new week)
    private fun resetBadges() {
        // Begin a SharedPreferences edit transaction
        sharedPreferences.edit().apply {
            for (i in 1..7) {
                // Set each badge's active status to false
                putBoolean("badge_$i", false)
            }
            apply() // Apply the changes
        }
    }
}
