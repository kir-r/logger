package com.epam.drill.logger.internal

actual val platformName: String = "jvm"

internal actual val Throwable.commonStackTrace: Array<out Any>
    get() = stackTrace
