package ch.magdenbt.sarvartest.screens.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import ch.magdenbt.sarvartest.common.RequestTimeOutException
import ch.magdenbt.sarvartest.common.Resource
import ch.magdenbt.sarvartest.data_source.DataSource
import ch.magdenbt.sarvartest.model.Config
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeoutOrNull
import timber.log.Timber


class SplashViewModel(private val dataSource: DataSource, private val ioDispatcher: CoroutineDispatcher) : ViewModel() {

    private val _config: MutableStateFlow<Resource<Config>> = MutableStateFlow(Resource.Loading(null, 0))

    val config = _config.asLiveData()

    init {
        CoroutineScope(ioDispatcher).launch {
            val result = withTimeoutOrNull(5000L) {
                Timber.d("Запуск получения конфига")
                dataSource.getConfigWithProgress().collect {
                    _config.value = it
                }
            }
            if (result == null) {
                Timber.e("Завершено получение конфига с ошибкой из-за истечения отведенного")
                _config.value = Resource.Error(RequestTimeOutException(), _config.value.data, _config.value.progress)
            } else {
                Timber.d("Завершено получение конфига без ошибок")
            }
            startAsyncTasks()
        }
    }

    private fun startAsyncTasks() {
        viewModelScope.launch(ioDispatcher) {
            Timber.d("Запуск асинхронных задач")
            val job1 = async { launchAsyncTask(1)
                Timber.d("Асинхронная задача 1 завершена")}
            val job2 = async { launchAsyncTask(2)
                Timber.d("Асинхронная задача 2 завершена")}
            val job3 = async { launchAsyncTask(3)
                Timber.d("Асинхронная задача 3 завершена")}

            awaitAll(job1, job2, job3)
            Timber.d("Все асинхронные задачи завершены")
        }
    }

    private suspend fun launchAsyncTask(taskNumber: Int) {
        val delayMillis = (100.. 10000).random()
        delay(delayMillis.toLong())
    }
}
