@file:Suppress("ObjectPropertyName")

package com.epam.drill.logger

import com.epam.drill.logger.api.*
import com.epam.drill.logger.internal.*
import io.ktor.utils.io.streams.*
import platform.posix.*
import kotlin.native.concurrent.*

@SharedImmutable
private val _logLevel = AtomicReference(LogLevel.ERROR)

@SharedImmutable
private val _filename = AtomicReference<String?>(null)

actual var Logging.logLevel: LogLevel
    get() = _logLevel.value
    set(value) {
        _logLevel.value = value
    }

actual var Logging.filename: String?
    get() = _filename.value
    set(value) {
        _fileDescriptor.value?.let { Output(it).close() }
        val descriptor = value?.let { f ->
            open(
                f,
                O_WRONLY or O_CREAT or O_TRUNC,
                S_IRUSR or S_IWUSR or S_IRGRP or S_IROTH
            ).takeIf { it > STDERR_FILENO }
        }
        _fileDescriptor.value = descriptor
        _filename.value = descriptor?.let { value.freeze() }
    }

actual fun Logging.output(message: String) {
    output { it.append(message).append('\n') }
}
