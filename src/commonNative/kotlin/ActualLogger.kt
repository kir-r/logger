package com.epam.drill.logger

import io.ktor.utils.io.streams.*
import platform.posix.*
import kotlin.native.concurrent.*


actual fun LoggerConfig.platformFreeze(): LoggerConfig = freeze()
actual val platformName: String = Platform.osFamily.name

actual typealias Atom<LoggerConfig> = AtomicReference<LoggerConfig>

actual fun Logging.createFd(file: String?) {
    _fd.value = file?.let {
        open(it, O_WRONLY or O_CREAT or O_TRUNC, S_IRUSR or S_IWUSR or S_IRGRP or S_IROTH)
    }
}

actual fun Logging.output(message: String, throwable: Throwable?) {
    _fd.value?.let {
        Output(it).apply {
            append("$message\n")
            throwable?.getStackTrace()?.forEach {
                append("$it\n")
            }
            flush()
        }
    } ?: println("$message ${throwable?.getStackTrace()?.joinToString("\n  ") ?: ""}")
}
