package com.example.opsc7213_goalignite

data class Badge(
    val dayOfWeek: Int, // 1 = Sunday, 2 = Monday, ..., 7 = Saturday
    var isActive: Boolean
) {
    // Add a name for the day of the week
    val dayName: String
        get() = when (dayOfWeek) {
            1 -> "Sun"
            2 -> "Mon"
            3 -> "Tue"
            4 -> "Wed"
            5 -> "Thu"
            6 -> "Fri"
            7 -> "Sat"
            else -> ""
        }

    // Add a resource ID for the icon
    val iconResId: Int
        get() = if (isActive) R.drawable.college else R.drawable.ic_empty // Replace with your icon resources
}

