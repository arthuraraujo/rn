package com.reactnativeonespanorchestration.di

import android.app.Application
import com.reactnativeonespanorchestration.bridge.activation.ActivationViewModel
import com.reactnativeonespanorchestration.data.repository.OneSpanRepository
import com.reactnativeonespanorchestration.data.repository.OneSpanRepositoryImpl
import com.reactnativeonespanorchestration.data.service.OneSpanService
import com.reactnativeonespanorchestration.data.service.OneSpanServiceImpl
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.dsl.module

val appModule = module {

    viewModel { ActivationViewModel(get()) }

    single<OneSpanRepository> { OneSpanRepositoryImpl(get()) }

    single<OneSpanService> { OneSpanServiceImpl.getService() }
}

fun start(myApplication: Application) {
    startKoin {
        androidLogger()
        androidContext(myApplication)
        modules(appModule)
    }
}
