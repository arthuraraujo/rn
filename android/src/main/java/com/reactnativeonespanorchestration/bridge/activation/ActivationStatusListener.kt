package com.reactnativeonespanorchestration.bridge.activation

class ActivationStatusListener() {

    var onStatusListener: ((errorCode: String)-> Unit)? = null

}
