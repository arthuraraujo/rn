package com.reactnativeonespanorchestration.utils

import java.net.NetworkInterface
import java.util.*



// return IPv4
fun getIpAddress(): String {
    var ipv4 = ""
    val interfaces = Collections.list(NetworkInterface.getNetworkInterfaces())

    try {
        for (intFace in interfaces) {
            val addresses = Collections.list(intFace.inetAddresses)

            for (address in addresses) {
                if (!address.isLoopbackAddress) {
                    val sAddress = address.hostAddress
                    val isIpv4 = sAddress?.indexOf(':')!! < 0

                    if (isIpv4) ipv4 = sAddress
                }
            }
        }
    } catch (e: Exception) {

    }
    return ipv4
}
