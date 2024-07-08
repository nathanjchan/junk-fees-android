package com.nathanjchan.junkfees

import java.sql.Timestamp
import java.util.Calendar

fun lastUpdatedText(timestamp: Timestamp): String {
    val currentTime = Calendar.getInstance().timeInMillis
    val elapsedTime = currentTime - timestamp.time
    val timeAgoText = when {
        elapsedTime < 60 * 60 * 1000 -> {
            val minutesAgo = elapsedTime / (60 * 1000)
            if (minutesAgo == 1L) "1 minute ago" else "$minutesAgo minutes ago"
        }
        elapsedTime < 24 * 60 * 60 * 1000 -> {
            val hoursAgo = elapsedTime / (60 * 60 * 1000)
            if (hoursAgo == 1L) "1 hour ago" else "$hoursAgo hours ago"
        }
        else -> {
            val daysAgo = elapsedTime / (24 * 60 * 60 * 1000)
            if (daysAgo == 1L) "1 day ago" else "$daysAgo days ago"
        }
    }
    return timeAgoText
}