package com.reactnativeonespanorchestration.data.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class UserResponse(
    val activationPassword: String,
    val serialNumber: String,
    val riskResponseCode: Int
): Parcelable
