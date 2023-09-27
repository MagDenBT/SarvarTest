package ch.magdenbt.sarvartest.screens.splash

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import ch.magdenbt.sarvartest.common.RequestTimeOutException
import ch.magdenbt.sarvartest.common.Resource
import ch.magdenbt.sarvartest.datasource.DataSource
import ch.magdenbt.sarvartest.model.Config
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeout
import timber.log.Timber

class SplashViewModel(
    private val dataSource: DataSource,
    ioDispatcher: CoroutineDispatcher,
) : ViewModel() {

    private val _config: MutableStateFlow<Resource<Config>> =
        MutableStateFlow(Resource.Loading(null, 0))
    val config = _config.asLiveData()
    private val _allTasksDone = MutableLiveData(false)
    val allTasksDone: LiveData<Boolean> = _allTasksDone

    init {
        CoroutineScope(ioDispatcher).launch {
            try {
                withTimeout(SPLASH_SCREEN_TIMEOUT) {
                    startTimeoutSafetyConfigRequest(LOAD_CONFIG_TIMEOUT)
                    startTimeoutSafetyAsyncTasks(this)
                }
            } finally {
                _allTasksDone.postValue(true)
            }
        }
    }

    private suspend fun startTimeoutSafetyConfigRequest(timeout: Long) {
        try {
            withTimeout(timeout) {
                Timber.d("Запуск получения конфига")

                dataSource.getConfigWithProgress().collect {
                    _config.value = it
                }

                Timber.w("Завершено получение конфига без ошибок")
            }
        } catch (ex: CancellationException) {
            Timber.e("Завершено получение конфига с ошибкой из-за истечения отведенного")
            _config.value = recreateResourceWithError(RequestTimeOutException())
        }
    }

    private fun recreateResourceWithError(error: Throwable) = Resource.Error(
        error,
        _config.value.data,
        _config.value.progress,
    )

    private suspend fun startTimeoutSafetyAsyncTasks(
        coroutineScope: CoroutineScope,
    ) {
        try {
            startAsyncTasks(coroutineScope)
            Timber.w("Все асинхронные задачи завершены")
        } catch (ex: CancellationException) {
            Timber.e("Асинхронные не успели выполниться за отведенное время")
        }
    }

    private suspend fun startAsyncTasks(coroutineScope: CoroutineScope) {
        Timber.d("Запуск асинхронных задач")
        val job1 = coroutineScope.async { launchAsyncTask(1) }
        val job2 = coroutineScope.async { launchAsyncTask(2) }
        val job3 = coroutineScope.async { launchAsyncTask(3) }
        awaitAll(job1, job2, job3)
    }

    private suspend fun launchAsyncTask(taskNumber: Int) {
        val delayMillis = (ASYNC_TASK_TIMEOUT_RANGE_FROM..SPLASH_SCREEN_TIMEOUT).random()
        delay(delayMillis)
        Timber.d("Асинхронная задача $taskNumber завершена")
    }
}
