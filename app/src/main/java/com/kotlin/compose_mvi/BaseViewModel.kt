package com.lulumedic.common.android.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lulumedic.common.android.BuildConfig
import com.lulumedic.error.handling.ClientException
import com.lulumedic.error.handling.ErrorExposureType
import com.lulumedic.error.handling.RemoteError
import com.lulumedic.error.handling.UnknownMessage
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext


abstract class BaseViewModel : ViewModel() {

    private val _errorEffect = MutableSharedFlow<Pair<String, ErrorExposureType>>(
        replay = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST,
    )
    val errorEffect = _errorEffect.asSharedFlow()

    /**
     * 안전한 코루틴 사용을 위한 [CoroutineExceptionHandler]
     * [Throwable]을 [handleUnknownException]으로 전송해 에러를 처리합니다
     */
    protected val ceh = CoroutineExceptionHandler { _, throwable ->
        handleClientException(throwable)
    }

    /**
     * [ceh]를 적용한 [viewModelScope]를 제공
     * 안전한 호출을 위해 [viewModelScope]대신 사용 할 것을 권장합니다
     *
     * @param action [viewModelScope]에서 호출 할 람다 함수
     */
    protected inline fun launch(
        context: CoroutineContext = EmptyCoroutineContext,
        start: CoroutineStart = CoroutineStart.DEFAULT,
        @ViewModelScoped crossinline action: suspend CoroutineScope.() -> Unit,
    ): Job {
        return viewModelScope.launch(context + ceh, start = start) {
            action()
        }
    }

    /**
     * [_errorEffect]는 에러 전송을 목적으로 하는 사이드 이펙트 입니다.
     * [postToastEffect]는 Toast 전송울 목적으로 [_errorEffect]를 방출합니다
     */
    fun postToastEffect(throwable: Throwable, remoteError: RemoteError?) = viewModelScope.launch {
        _errorEffect.emit(Pair(getMessage(throwable, remoteError), ErrorExposureType.TOAST))
    }

    /**
     * [_errorEffect]는 에러 전송을 목적으로 하는 사이드 이펙트 입니다.
     * [postDialogEffect]는 Dialog 전송울 목적으로 [_errorEffect]를 방출합니다
     */
    fun postDialogEffect(throwable: Throwable, remoteError: RemoteError?) = viewModelScope.launch {
        _errorEffect.emit(Pair(getMessage(throwable, remoteError), ErrorExposureType.DIALOG))
    }

    /**
     * 상황에 맞게 에러를 필터링 합니다.
     * [throwable]의 타입에 따라, 그에 맞는 하위 필터링 메서드로 전달합니다.
     */
    private fun handleClientException(throwable: Throwable) {
        if (BuildConfig.DEBUG) throwable.printStackTrace()
        when (val exception = throwable as Exception) {
            is ClientException -> {
                if (exception.statusCode == 200) postToastEffect(throwable, exception.remoteError)
                else postDialogEffect(exception, exception.remoteError)
            }

            else -> postDialogEffect(throwable, null)
        }
    }
    private fun getMessage(throwable: Throwable, remoteError: RemoteError?): String {
        return remoteError?.message ?: when (throwable) {
            is ClientException -> throwable.remoteError?.message ?: throwable.message
            else -> UnknownMessage
        }
    }
}
