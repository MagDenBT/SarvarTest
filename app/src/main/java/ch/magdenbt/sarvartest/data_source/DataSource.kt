package ch.magdenbt.sarvartest.data_source

import ch.magdenbt.sarvartest.common.Resource
import ch.magdenbt.sarvartest.model.Config
import kotlinx.coroutines.flow.Flow

interface DataSource {
   fun getConfigWithProgress() : Flow<Resource<Config>>
}