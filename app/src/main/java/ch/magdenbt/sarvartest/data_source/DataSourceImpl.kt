package ch.magdenbt.sarvartest.data_source

import ch.magdenbt.sarvartest.common.RequestTimeOutException
import ch.magdenbt.sarvartest.common.Resource
import ch.magdenbt.sarvartest.model.Config
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class DataSourceImpl : DataSource {

    override fun getConfigWithProgress(): Flow<Resource<Config>> = flow {
        var progress = 0
        emit(Resource.Loading(null, progress))
        while (true) {
            var progressChange =  (1..10).random()
            val requestPortionDuration =   700L / progressChange + (0..10).random() - (0..10).random()

            delay(requestPortionDuration)


            progress += progressChange
            if (progress >= 100) {
                emit(Resource.Success(Config("Некий конфиг, который очень нужен",12)))
                break
            }else{
                emit(Resource.Loading(null, progress))
            }
        }
    }

}