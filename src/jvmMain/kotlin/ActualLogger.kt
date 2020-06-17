package com.epam.drill.logger

import java.io.*

actual fun LoggerConfig.platformFreeze(): LoggerConfig = this
actual val platformName: String = "jvm"
actual fun Logging.output(message: String, throwable: Throwable?) {
    val trace = throwable?.let { "\n" + StringWriter().apply { it.printStackTrace(PrintWriter(this)) }.toString() }
    NativeApi.nativeOutput(message + (trace ?: ""))
}

actual fun Logging.createFd(file: String?) {
    NativeApi.createFd(file)
}

actual class Atom<T> actual constructor(private var value_: T) {
    actual var value: T
        get() = value_.let {
            if (it is LoggerConfig) {
                val config = LoggerConfig(LogLevel.byCode(NativeApi.getConfig()) ?: LogLevel.ERROR)
                @Suppress("UNCHECKED_CAST")
                config as T
            } else it

        }
        set(value) {
            (value as? LoggerConfig)?.run {
                NativeApi.setConfig(level.code)
            } ?: run { value_ = value }
        }
}
