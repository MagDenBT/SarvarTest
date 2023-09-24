package ch.magdenbt.sarvartest

import ch.magdenbt.sarvartest.data_source.DataSource
import ch.magdenbt.sarvartest.data_source.DataSourceImpl
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers


object SimpleDiRepository {
    val dataSource: DataSource by lazy { DataSourceImpl() }
    val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
}