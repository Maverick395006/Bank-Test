package com.example.banktest.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

data class AccountDetailModel(
    var account: Account,
    var transactions: List<Transaction>,
    var atms: List<Atm>,
)

data class Account(
    var bsb: String,
    var accountNumber: String,
    var balance: String,
    var available: String,
    var accountName: String,
)

@Parcelize
data class Transaction(
    var amount: String,
    var id: String,
    var isPending: Boolean,
    var description: String,
    var category: String,
    var atmId: String?,
    var effectiveDate: String
) : Parcelable, ListItem(TYPE_GENERAL)

data class DateItem(
    val date: String,
) : ListItem(TYPE_DATE)

open class ListItem(
    val Type: Int,
) {
    companion object {
        const val TYPE_DATE = 0
        const val TYPE_GENERAL = 1
    }
}

data class Atm(
    var id: String,
    var name: String,
    var location:Location,
    var address: String,
)

data class Location(
    var lat: Float,
    var lon: Float,
)

