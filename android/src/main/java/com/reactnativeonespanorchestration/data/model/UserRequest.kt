package com.reactnativeonespanorchestration.data.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class UserRequest(
    val objectType: String,
    val clientIP: String,
    val staticPassword: String,
    val userID: String,
    val cddc: BrowserCDDC? = null
) : Parcelable {

    @Parcelize
    data class BrowserCDDC(
        val fingerprintRaw: String,
        val fingerprintHash: String
    ) : Parcelable

}
