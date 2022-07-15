package com.reactnativeonespanorchestration.utils

import java.util.*

object SessionHelper {
    var accountIdentifier: String? = null
    var cloudServerUrl: String? = null
    val domain: String get() = accountIdentifier?.lowercase(Locale.getDefault()) ?: ""
    val endpointUrl: String get() = "https://$accountIdentifier$cloudServerUrl/v1/orchestration-commands"
    val backendUrl: String get() = "https://$accountIdentifier$cloudServerUrl/"
    const val saltStorage = "38af4675075cb1971a5fe79d59e702d711577b40a6e06ab75696bbd4aaddebdc"
    const val saltDigipass = "5910c093a9e6777c8291679ed655328da20958f2c4a11e03a3768b9e12e36d73"
    const val serverCommandKey = "command"
}
