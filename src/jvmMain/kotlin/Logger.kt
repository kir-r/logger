package com.epam.drill.logger

import com.epam.drill.logger.mu.KotlinLogging

actual fun LoggerConfig.platformFreeze(): LoggerConfig = this
actual val platformName: String = "java"
actual fun KotlinLogging.output(message: String) {
}

actual fun KotlinLogging.createFd(file: String?) {
}

actual class Atom<T> actual constructor(private var value_: T) {
    actual var value: T = value_
}