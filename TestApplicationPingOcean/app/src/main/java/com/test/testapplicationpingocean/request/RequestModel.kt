package com.test.testapplicationpingocean.request

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class RequestModel(
    val email: String,
    val password: String,
): Parcelable