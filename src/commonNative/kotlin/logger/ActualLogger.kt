package com.epam.drill.logger

import io.ktor.utils.io.streams.*
import mu.*
import platform.posix.*
import kotlin.native.concurrent.*


actual fun LoggerConfig.platformFreeze(): LoggerConfig = freeze()
actual val platformName: String = Platform.osFamily.name

actual typealias Atom<LoggerConfig> = AtomicReference<LoggerConfig>

actual fun KotlinLogging.createFd(file: String?) {
    _fd.value = file?.let {
        open(it, O_WRONLY or O_CREAT or O_TRUNC, S_IRUSR or S_IWUSR or S_IRGRP or S_IROTH)
    }
}

actual fun KotlinLogging.output(message: String, throwable: Throwable?) {
    if (_fd.value != null) {
        Output(_fd.value!!).apply {
            append("message\n")
            throwable?.getStackTrace()?.forEach {
                append("$it\n")
            }

            flush()
        }
    } else
        println(message + (throwable?.getStackTrace()
            ?.filter { !it.startsWith("at  (0x") }
            ?.joinToString("\n", prefix = "\n") ?: ""))
}


