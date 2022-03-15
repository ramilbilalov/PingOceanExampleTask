package com.test.testapplicationpingocean.request

import android.os.Parcelable
import android.provider.ContactsContract
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ResponseListProfile(
    val id: String,
    val name: String,
    val email: String,
    val phone: String,
    val avatar: String,
    val position: String,
    val company_name: String,
    val name_eng: String,
    val timezone: String,
    ): Parcelable