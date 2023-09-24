package ch.magdenbt.sarvartest

import ch.magdenbt.sarvartest.datasource.DataSource
import ch.magdenbt.sarvartest.datasource.DataSourceImpl
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

object SimpleDiRepository {
    val dataSource: DataSource by lazy { DataSourceImpl() }
    val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
}
