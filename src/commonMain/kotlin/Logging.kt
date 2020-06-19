package com.epam.drill.logger

import com.epam.drill.logger.api.*
import com.epam.drill.logger.internal.*

object Logging : LoggerFactory {
    fun logger(func: () -> Unit): Logger {
        val name = func::class.qualifiedName?.replace(".${func::class.simpleName}", "")
        return logger(name ?: "unknown logger")
    }

    override fun logger(name: String): Logger = name.namedLogger(::logLevel, ::appendLogMessage)
}

expect var Logging.logLevel: LogLevel

expect var Logging.filename: String?

expect fun Logging.output(message: String)
