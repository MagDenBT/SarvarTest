package ch.magdenbt.sarvartest.common

open class AppException : Exception()

class RequestTimeOutException(
) : AppException()