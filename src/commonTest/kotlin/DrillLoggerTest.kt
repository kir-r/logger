package com.epam.drill.logger

import com.epam.drill.logger.api.*
import com.soywiz.klock.*
import kotlin.test.*


class DrillLoggerTest {
    companion object {
        val timestamp: String = DateTime.now().toString(DateFormat.FORMAT1)
            .replace(':', '-')
            .apply { println("Timestamp: $this") }
    }

    @Test
    fun `level trace - messages and one with stacktrace`() {
        println("$timestamp Test: level trace - messages and one with stacktrace\n")
        withLevel(LogLevel.TRACE) {
            val msg = "xx"
            Logging.logger("trace-test").apply {
                trace { msg }
                debug { msg }
                info { msg }
                warn { msg }
                error { msg }
                val t = RuntimeException("wwww")
                warn(t) { msg }
                error(t) { msg }
            }
        }
    }

    @Test
    fun `level info - messages`() {
        println("$timestamp  Test: level info - messages\n")
        val msg = "yy"
        Logging.logLevel = LogLevel.TRACE
        withLevel(LogLevel.TRACE) {
            Logging.logger("info-test").apply {
                trace { msg }
                debug { msg }
                info { msg }
                warn { msg }
                error { msg }
            }
        }
    }


    @Test
    fun `level trace - file descriptor`() {
        println("$timestamp Test: level trace - file descriptor\n")
        val msg = "yy"
        withLevel(LogLevel.TRACE) {
            Logging.filename = "./build/test-results/test-${timestamp}.log"
            Logging.logger("info-test").apply {
                trace { msg }
                debug { msg }
                info { msg }
                warn { msg }
                error(RuntimeException("something")) { msg }
            }
        }
        withLevel(LogLevel.INFO) {
            Logging.filename = "./build/test-results/test-${timestamp}-1.log"
            Logging.logger("info-test").apply {
                info { msg }
                warn { msg }
            }
        }
    }
}

private fun withLevel(level: LogLevel, block: () -> Unit) {
    val currentLogLevel = Logging.logLevel
    Logging.logLevel = level
    block()
    Logging.logLevel = currentLogLevel
}
