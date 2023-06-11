package com.example.banktest.util

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

object Utils {

    const val SHOPPING = "shopping"
    const val BUSINESS = "business"
    const val ENTERTAINMENT = "entertainment"
    const val GROCERIES = "groceries"
    const val EATINGOUT = "eatingOut"
    const val TRANSPORT = "transport"
    const val CASH = "cash"
    const val UNCATEGORISED = "uncategorised"

    fun refineAmount(amount: String): String {
        return if (amount[0] == '-') {
            amount.replaceRange(1, 1, "$")
        } else {
            buildString {
                append("$")
                append(amount)
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun convertDateFormat(dateString: String): String {
        val inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val outputFormatter = DateTimeFormatter.ofPattern("E d MMM", Locale.ENGLISH)

        val date = LocalDate.parse(dateString, inputFormatter)
        return date.format(outputFormatter)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun howManyDaysAgo(dateString: String): String {
        val currentDate = LocalDate.now()
        val inputDate = LocalDate.parse(dateString)

        val difference = currentDate.toEpochDay() - inputDate.toEpochDay()
        return buildString { append(difference.toString()) } + " days ago"
    }

    fun formatAccountNumber(input: String): String {
        return input.chunked(4).joinToString(" ")
    }

    fun isPendingDescriptionFormat(isPending: Boolean, description: String): String {
        return if (isPending) buildString {
            append("Pending: ")
            append(description)
        } else {
            description
        }
    }


}