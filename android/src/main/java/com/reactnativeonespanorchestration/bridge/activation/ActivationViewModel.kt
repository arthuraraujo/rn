package com.reactnativeonespanorchestration.bridge.activation

import android.util.Log
import androidx.lifecycle.*
import com.reactnativeonespanorchestration.bridge.OrchestrationCallback
import com.reactnativeonespanorchestration.data.model.UserRequest
import com.reactnativeonespanorchestration.data.model.UserResponse
import com.reactnativeonespanorchestration.data.repository.OneSpanRepository
import com.reactnativeonespanorchestration.utils.*
import com.vasco.orchestration.client.Orchestrator
import com.vasco.orchestration.client.errors.OrchestrationErrorCodes
import com.vasco.orchestration.client.flows.activation.online.OnlineActivationCallback
import com.vasco.orchestration.client.flows.activation.online.OnlineActivationInputError
import com.vasco.orchestration.client.flows.activation.online.OnlineActivationParams
import com.vasco.orchestration.client.user.OrchestrationUser
import kotlinx.coroutines.launch
import java.lang.ref.WeakReference
import java.util.concurrent.ExecutionException
import java.util.concurrent.Executors
import java.util.concurrent.Future

class ActivationViewModel(
    private val repository: OneSpanRepository
) : ViewModel(), OnlineActivationCallback {

    private val tag = "ACTIVATION"

    private val _statusLoading = MutableLiveData<Status>()
    val statusLoading: LiveData<Status> get() = _statusLoading

    private val _statusMessage = MutableLiveData<String>()
    val statusMessage: LiveData<String> get() = _statusMessage

    private var steps = 1

    private val _userResponse = MutableLiveData<UserResponse>()
    val userResponse: LiveData<UserResponse> get() = _userResponse

    private lateinit var userRequest: UserRequest
    private lateinit var orchestrationCallback: OrchestrationCallback
    private lateinit var orchestrator: Orchestrator


    fun registerUser(userId: String, userPassword: String) = viewModelScope.launch {
        setStatusLoading(Status.LOADING, "registering")

        userRequest = UserRequest(
            objectType = "RegisterUserInputEx",
            clientIP = getIpAddress(),
            staticPassword = userPassword,
            userID = userId
        )

        val response = repository.postUser(userRequest)

        if (response.isSuccessful) {
            _userResponse.value = response.body()

        } else {
            setStatusLoading(Status.ERROR, response.message())
        }
    }

    fun startActivation(activity: ActivationActivity, userResponse: UserResponse) {
        orchestrationCallback = OrchestrationCallback()
        HTTPUtils().enableTLSv12(activity)

        val builder = Orchestrator.Builder()

        orchestrator = builder
            .setDigipassSalt(SessionHelper.saltDigipass)
            .setStorageSalt(SessionHelper.saltStorage)
            .setContext(activity)
            .setActivityProvider { WeakReference(activity) }
            .setDefaultDomain(SessionHelper.domain)
            .setCDDCParams(CDDCUtils.getCDDCParams())
            .setErrorCallback(orchestrationCallback)
            .setWarningCallback(orchestrationCallback)
            .build()

        CDDCUtils.configure(orchestrator.cddcDataFeeder)

        val activationParams = OnlineActivationParams()
        activationParams.setActivationCallback(this)
        activationParams.orchestrationUser = OrchestrationUser(userRequest.userID)
        activationParams.activationPassword = userResponse.activationPassword

        setStatusLoading(Status.LOADING, "activating")
        orchestrator.startActivation(activationParams)
    }

    override fun onActivationSuccess() {
        setStatusLoading(Status.SUCCESS, "activated")
    }

    override fun onActivationInputError(error: OnlineActivationInputError?) {
        Log.e(tag, "onActivationInputError:", error?.activationInputException)

        val errorMessage = when (error!!.errorCode) {
            OrchestrationErrorCodes.USER_ID_NULL_OR_EMPTY -> "User ID is null or empty"
            OrchestrationErrorCodes.USER_ID_WRONG_FORMAT -> "User ID contains invalid characters"
            OrchestrationErrorCodes.ACTIVATION_PASSWORD_NULL_OR_EMPTY -> "The activation password is null or empty"
            OrchestrationErrorCodes.ACTIVATION_PASSWORD_WRONG_LENGTH -> "The activation password has a wrong length"
            OrchestrationErrorCodes.ACTIVATION_PASSWORD_WRONG_CHECKSUM -> "The activation password is invalid"
            else -> "Unknown error"
        }

        setStatusLoading(Status.ERROR, errorMessage)
    }

    override fun onActivationAborted() {
        Log.d(tag, "onActivationAborted")
        setStatusLoading(Status.ERROR, "activation_aborted")
    }

    override fun onActivationStepComplete(command: String?) {
        Log.d(tag, "onActivationStepComplete: $command")
        setStatusLoading(Status.LOADING, "activating_step_${steps++}")

        val executor = Executors.newSingleThreadExecutor()
        val commandSender = CommandSender(command ?: "")
        val future: Future<String> = executor.submit(commandSender)

        executor.submit {
            try {
                val serverCommand = future.get()

                if (serverCommand == null) {
                    Log.e(tag, "serverCommand == null")
                    setStatusLoading(Status.ERROR, "activation_step_error")

                } else {
                    orchestrator.execute(serverCommand)
                }

            } catch (e: ExecutionException) {
                Log.e(tag, "activation exception: $e")
                setStatusLoading(Status.ERROR, "activation_step_exception")
                Thread.currentThread().interrupt()
            }
        }
    }

    private fun setStatusLoading(status: Status, message: String) {
        _statusMessage.value = message
        _statusLoading.value = status
    }
}
