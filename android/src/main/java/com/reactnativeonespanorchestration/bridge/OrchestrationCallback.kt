package com.reactnativeonespanorchestration.bridge

import android.util.Log
import com.vasco.orchestration.client.authentication.UserAuthenticationCallback
import com.vasco.orchestration.client.authentication.UserAuthenticationInputCallback
import com.vasco.orchestration.client.errors.*

class OrchestrationCallback : UserAuthenticationCallback, OrchestrationWarningCallback, OrchestrationErrorCallback {

    override fun onUserAuthenticationRequired(
        type: UserAuthenticationCallback.UserAuthentication?,
        inputCallback: UserAuthenticationInputCallback?,
        isEnrollment: Boolean
    ) {
        Log.d("FLMWG", "user type name: " + type?.name)
        Log.d("FLMWG", "user isEnrollment: $isEnrollment")

        inputCallback?.onUserAuthenticationInputSuccess("Ga7fQhPEEgw59cftCJQu")
    }

    override fun onUserAuthenticationInputError(error: InputError?) {
        Log.e("FLMWG", "input error: ", error?.inputException)
    }

    override fun onOrchestrationWarning(warning: OrchestrationWarning?) {
        Log.w("FLMWG", "warning code: ${warning?.warningCode}")
        Log.w("FLMWG", "warning code: ${warning?.exception}")
    }

    override fun onOrchestrationError(error: OrchestrationError?) {
        Log.e("FLMWG", "Exception in onOrchestrationError", error?.exception)
    }

    override fun onOrchestrationServerError(error: OrchestrationServerError?) {
        Log.e("FLMWG", "Payload in onOrchestrationServerError: " + error?.customPayload)
    }
}
