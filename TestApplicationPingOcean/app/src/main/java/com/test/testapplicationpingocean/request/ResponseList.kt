package com.test.testapplicationpingocean.request

import android.os.Parcelable
import com.github.kittinunf.fuel.core.Body
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ResponseList(
    val token: String,
): Parcelable