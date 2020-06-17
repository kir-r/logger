package com.epam.drill.logger

import kotlin.native.concurrent.*

expect class Atom<T>(value_: T) {
    var value: T
}

@SharedImmutable
val logConfig = Atom(
    LoggerConfig().platformFreeze()
)


expect fun Logging.output(message: String, throwable: Throwable? = null)
expect fun Logging.createFd(file: String?)
expect fun LoggerConfig.platformFreeze(): LoggerConfig
expect val platformName: String
