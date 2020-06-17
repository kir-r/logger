package com.epam.drill.logger

class LoggerConfig(val level: LogLevel = LogLevel.ERROR) {
    val isTraceEnabled: Boolean = level.code == LogLevel.TRACE.code
    val isDebugEnabled: Boolean = level.code <= LogLevel.DEBUG.code
    val isInfoEnabled: Boolean = level.code <= LogLevel.INFO.code
    val isWarnEnabled: Boolean = level.code <= LogLevel.WARN.code
}

enum class LogLevel(val code: Int) {
    TRACE(0),
    DEBUG(10),
    INFO(20),
    WARN(30),
    ERROR(40);

    companion object {
        fun byCode(code: Int): LogLevel? = values().firstOrNull { it.code == code }
    }
}
