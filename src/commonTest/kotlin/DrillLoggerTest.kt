package com.epam.drill.logger

import kotlin.test.*

class DrillLoggerTest {

    @Test
    fun `level trace and exception stacktrace`() {
        val msg = "xx"
        Logging.logger("trace-test").apply {
            logConfig.value = LoggerConfig(LogLevel.TRACE).platformFreeze()
            trace { msg }
            debug { msg }
            info { msg }
            warn { msg }
            error { msg }
            val t = RuntimeException("wwww")
            warn(t) { msg }
            error(t) { msg }
            logConfig.value = LoggerConfig().platformFreeze()
        }
    }

    @Test
    fun `level info`() {
        val msg = "yy"
        Logging.logger("info-test").apply {
            logConfig.value = LoggerConfig(LogLevel.INFO).platformFreeze()
            trace { msg }
            debug { msg }
            info { msg }
            warn { msg }
            error { msg }
            logConfig.value = LoggerConfig().platformFreeze()
        }
    }
}
