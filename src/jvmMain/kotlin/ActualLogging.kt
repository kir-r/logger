package com.epam.drill.logger

import com.epam.drill.logger.api.*

actual var Logging.logLevel: LogLevel
    get() = NativeApi.getLogLevel().toLogLevel()
    set(value) {
        NativeApi.setLogLevel(value.code)
    }

actual var Logging.filename: String?
    get() = null //not used
    set(value) = NativeApi.setFilename(value)

actual fun Logging.output(message: String) {
    NativeApi.output(message)
}
