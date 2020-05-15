package com.epam.drill.logger
import kotlinx.serialization.*

@Serializable
data class LoggerConfig(
    val isTraceEnabled: Boolean = false,
    val isDebugEnabled: Boolean = false,
    val isInfoEnabled: Boolean = false,
    val isWarnEnabled: Boolean = false
)

@Serializable
enum class LogLevel {
    TRACE, DEBUG, INFO, WARNING, ERROR
}

fun configByLoggerLevel(level: LogLevel) = when (level) {
    LogLevel.TRACE -> LoggerConfig(
        isTraceEnabled = true,
        isDebugEnabled = true,
        isInfoEnabled = true,
        isWarnEnabled = true
    )
    LogLevel.DEBUG -> LoggerConfig(
        isDebugEnabled = true,
        isInfoEnabled = true,
        isWarnEnabled = true
    )
    LogLevel.INFO -> LoggerConfig(
        isInfoEnabled = true,
        isWarnEnabled = true
    )

    LogLevel.WARNING -> LoggerConfig(
        isWarnEnabled = true
    )
    else -> LoggerConfig()

}