package ch.magdenbt.sarvartest.common

sealed class Resource<T>(
    val data: T? = null,
    val progress: Int,
    val error: Throwable? = null,

) {
    class Loading<T>(data: T? = null, progress: Int) : Resource<T>(data, progress)
    class Success<T>(data: T) : Resource<T>(data, 100)
    class Error<T>(throwable: Throwable, data: T? = null, progress: Int) :
        Resource<T>(data, progress, throwable)
}
