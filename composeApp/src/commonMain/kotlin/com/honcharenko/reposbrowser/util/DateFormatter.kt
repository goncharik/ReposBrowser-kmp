package com.honcharenko.reposbrowser.util

import kotlin.math.abs
import kotlin.time.Clock

/**
 * Utility object for formatting dates in various formats.
 */
@OptIn(kotlin.time.ExperimentalTime::class)
object DateFormatter {

    /**
     * Formats an ISO 8601 date string to a formatted date like "Jan 15, 2023"
     * Example: "2023-01-15T10:30:00Z" -> "Jan 15, 2023"
     */
    fun formatCreatedDate(isoString: String): String {
        return try {
            val date = parseIsoDate(isoString)
            val monthName = getMonthName(date.month)
            "${monthName} ${date.day}, ${date.year}"
        } catch (e: Exception) {
            "Unknown date"
        }
    }

    /**
     * Formats an ISO 8601 date string to relative time like "2 months ago"
     * Example: "2024-10-15T10:30:00Z" -> "3 weeks ago"
     */
    fun formatRelativeTime(isoString: String): String {
        return try {
            val date = parseIsoDate(isoString)
            val now = getCurrentDate()

            val diffSeconds = (now.timestamp - date.timestamp) / 1000
            val diffMinutes = diffSeconds / 60
            val diffHours = diffMinutes / 60
            val diffDays = diffHours / 24
            val diffWeeks = diffDays / 7
            val diffMonths = diffDays / 30
            val diffYears = diffDays / 365

            when {
                diffYears > 0 -> "${diffYears} ${if (diffYears == 1L) "year" else "years"} ago"
                diffMonths > 0 -> "${diffMonths} ${if (diffMonths == 1L) "month" else "months"} ago"
                diffWeeks > 0 -> "${diffWeeks} ${if (diffWeeks == 1L) "week" else "weeks"} ago"
                diffDays > 0 -> "${diffDays} ${if (diffDays == 1L) "day" else "days"} ago"
                diffHours > 0 -> "${diffHours} ${if (diffHours == 1L) "hour" else "hours"} ago"
                diffMinutes > 0 -> "${diffMinutes} ${if (diffMinutes == 1L) "minute" else "minutes"} ago"
                else -> "just now"
            }
        } catch (e: Exception) {
            "recently"
        }
    }

    /**
     * Simple date data class
     */
    private data class SimpleDate(
        val year: Int,
        val month: Int,
        val day: Int,
        val hour: Int,
        val minute: Int,
        val second: Int,
        val timestamp: Long
    )

    /**
     * Parses an ISO 8601 date string (e.g., "2023-01-15T10:30:00Z")
     */
    private fun parseIsoDate(isoString: String): SimpleDate {
        // Remove 'Z' suffix and 'T' separator
        val cleaned = isoString.replace("Z", "").replace("T", "-")

        // Split into date and time parts
        val parts = cleaned.split("-", ":")

        if (parts.size < 6) {
            throw IllegalArgumentException("Invalid ISO date format")
        }

        val year = parts[0].toInt()
        val month = parts[1].toInt()
        val day = parts[2].toInt()
        val hour = parts[3].toInt()
        val minute = parts[4].toInt()
        val second = parts[5].split(".")[0].toInt() // Handle milliseconds if present

        // Calculate approximate timestamp (milliseconds since epoch)
        val timestamp = calculateTimestamp(year, month, day, hour, minute, second)

        return SimpleDate(year, month, day, hour, minute, second, timestamp)
    }

    /**
     * Calculates approximate timestamp in milliseconds since epoch
     */
    private fun calculateTimestamp(
        year: Int,
        month: Int,
        day: Int,
        hour: Int,
        minute: Int,
        second: Int
    ): Long {
        // Days since epoch (1970-01-01)
        var days = 0L

        // Add years
        for (y in 1970 until year) {
            days += if (isLeapYear(y)) 366 else 365
        }

        // Add months
        for (m in 1 until month) {
            days += daysInMonth(m, year)
        }

        // Add days
        days += day - 1

        // Convert to milliseconds and add time
        return days * 24 * 60 * 60 * 1000L +
               hour * 60 * 60 * 1000L +
               minute * 60 * 1000L +
               second * 1000L
    }

    /**
     * Gets current date/time
     */
    private fun getCurrentDate(): SimpleDate {
        // Using current time millis and converting to date
        val currentTimeMillis = getCurrentTimeMillis()

        // Simple calculation from epoch
        val totalSeconds = currentTimeMillis / 1000
        val totalMinutes = totalSeconds / 60
        val totalHours = totalMinutes / 60
        val totalDays = totalHours / 24

        var year = 1970
        var remainingDays = totalDays

        while (remainingDays >= (if (isLeapYear(year)) 366 else 365)) {
            remainingDays -= if (isLeapYear(year)) 366 else 365
            year++
        }

        var month = 1
        while (remainingDays >= daysInMonth(month, year)) {
            remainingDays -= daysInMonth(month, year)
            month++
        }

        val day = remainingDays.toInt() + 1
        val hour = (totalHours % 24).toInt()
        val minute = (totalMinutes % 60).toInt()
        val second = (totalSeconds % 60).toInt()

        return SimpleDate(year, month, day, hour, minute, second, currentTimeMillis)
    }

    /**
     * Gets current time in milliseconds using Kotlin time API
     */
    private fun getCurrentTimeMillis(): Long {
        return Clock.System.now().toEpochMilliseconds()
    }

    /**
     * Checks if a year is a leap year
     */
    private fun isLeapYear(year: Int): Boolean {
        return (year % 4 == 0 && year % 100 != 0) || (year % 400 == 0)
    }

    /**
     * Gets number of days in a month
     */
    private fun daysInMonth(month: Int, year: Int): Long {
        return when (month) {
            1, 3, 5, 7, 8, 10, 12 -> 31
            4, 6, 9, 11 -> 30
            2 -> if (isLeapYear(year)) 29 else 28
            else -> 30
        }
    }

    /**
     * Converts month number to month name
     */
    private fun getMonthName(month: Int): String {
        return when (month) {
            1 -> "Jan"
            2 -> "Feb"
            3 -> "Mar"
            4 -> "Apr"
            5 -> "May"
            6 -> "Jun"
            7 -> "Jul"
            8 -> "Aug"
            9 -> "Sep"
            10 -> "Oct"
            11 -> "Nov"
            12 -> "Dec"
            else -> "Unknown"
        }
    }
}
