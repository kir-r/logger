package com.epam.drill.logger

import com.epam.drill.logger.api.*

actual object NativeApi {

    actual fun getLogLevel(): Int = Logging.logLevel.code

    actual fun setLogLevel(code: Int) {
        Logging.logLevel = code.toLogLevel()
    }

    actual fun setFilename(filename: String?) {
        Logging.filename = filename
    }

    actual fun output(message: String) {
        Logging.output(message)
    }
}
