package com.epam.drill.logger

import kotlinx.serialization.protobuf.*
import mu.*
import java.io.*

actual fun LoggerConfig.platformFreeze(): LoggerConfig = this
actual val platformName: String = "java"
actual fun KotlinLogging.output(message: String, throwable: Throwable?) {
    val trace = throwable?.let { "\n" + StringWriter().apply { it.printStackTrace(PrintWriter(this)) }.toString() }
    NativeApi.nativeOutput(message + (trace ?: ""))
}

actual fun KotlinLogging.createFd(file: String?) {
    NativeApi.createFd(file)
}

actual class Atom<T> actual constructor(var value_: T) {
    @Suppress("UNCHECKED_CAST")
    actual var value: T
        get() {
            return if (value_ is LoggerConfig)
                ProtoBuf.load(LoggerConfig.serializer(), NativeApi.getConfig()) as T
            else value_

        }
        set(value) {
            if (value_ is LoggerConfig)
                NativeApi.setConfig(ProtoBuf.dump(LoggerConfig.serializer(), value as LoggerConfig))
            else
                value_ = value
        }
}