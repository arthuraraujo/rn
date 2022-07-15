package com.reactnativeonespanorchestration.bridge.activation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.reactnativeonespanorchestration.databinding.ActivityActivationBinding
import com.reactnativeonespanorchestration.utils.Status
import org.koin.androidx.viewmodel.ext.android.viewModel

class ActivationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityActivationBinding
    private val viewModel by viewModel<ActivationViewModel>()

    companion object {
        val statusListener = ActivationStatusListener()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        /*binding = ActivityActivationBinding.inflate(layoutInflater).apply {
            viewModel = this@ActivationActivity.viewModel
        }

        setContentView(binding.root)*/

        val userId = intent.getStringExtra("userId") ?: ""
        val userPassword = intent.getStringExtra("userPassword") ?: ""

        initObservers()
        viewModel.registerUser(userId, userPassword)
    }

    private fun initObservers() {
        viewModel.userResponse.observe(this) {
            viewModel.startActivation(this, it)
        }

        viewModel.statusLoading.observe(this) {
            when (it) {
                Status.SUCCESS, Status.ERROR -> exit()
                else -> Unit
            }
        }

        viewModel.statusMessage.observe(this) {
            statusListener.onStatusListener?.invoke(it)
//            binding.tvStatusMessage.text = it
        }
    }

    private fun exit() {
        Handler(Looper.getMainLooper())
            .postDelayed({
                finish()
            }, 600)
    }
}
