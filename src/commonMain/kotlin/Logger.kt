package com.epam.drill.logger

import com.soywiz.klock.*
import mu.*
import kotlin.native.concurrent.*

expect class Atom<T>(value_: T) {
    var value: T
}

@SharedImmutable
val logConfig = Atom(
    LoggerConfig().platformFreeze()
)

class DrillLogger(private val name: String) : KLogger {
    override fun trace(msg: () -> Any?) {
        if (logConfig.value.isTraceEnabled)
            KotlinLogging.output("${DateTime.now()} [$platformName][$name][DRILL trace] ${msg()}")
    }

    override fun trace(t: Throwable?, msg: () -> Any?) {
        if (logConfig.value.isTraceEnabled)
            KotlinLogging.output("${DateTime.now()} [$platformName][$name][DRILL trace] ${msg()}", t)
    }

    override fun trace(marker: Marker?, msg: () -> Any?) {
        if (logConfig.value.isTraceEnabled)
            KotlinLogging.output("${DateTime.now()} [$platformName][$name][DRILL trace] ${msg()}")
    }

    override fun trace(marker: Marker?, t: Throwable?, msg: () -> Any?) {
        if (logConfig.value.isTraceEnabled)
            KotlinLogging.output("${DateTime.now()} [$platformName][$name][DRILL trace] ${msg()}", t)
    }

    override fun debug(msg: () -> Any?) {
        if (logConfig.value.isDebugEnabled)
            KotlinLogging.output("${DateTime.now()} [$platformName][$name][DRILL debug] ${msg()}")
    }

    override fun debug(t: Throwable?, msg: () -> Any?) {
        if (logConfig.value.isDebugEnabled)
            KotlinLogging.output("${DateTime.now()} [$platformName][$name][DRILL debug] ${msg()}", t)
    }

    override fun debug(marker: Marker?, msg: () -> Any?) {
        if (logConfig.value.isDebugEnabled)
            KotlinLogging.output("${DateTime.now()} [$platformName][$name][DRILL debug] ${msg()}")
    }

    override fun debug(marker: Marker?, t: Throwable?, msg: () -> Any?) {
        if (logConfig.value.isDebugEnabled)
            KotlinLogging.output("${DateTime.now()} [$platformName][$name][DRILL debug] ${msg()}", t)
    }

    override fun info(msg: () -> Any?) {
        if (logConfig.value.isInfoEnabled)
            KotlinLogging.output("${DateTime.now()} [$platformName][$name][DRILL info] ${msg()}")
    }

    override fun info(t: Throwable?, msg: () -> Any?) {
        if (logConfig.value.isInfoEnabled)
            KotlinLogging.output("${DateTime.now()} [$platformName][$name][DRILL info] ${msg()}", t)
    }

    override fun info(marker: Marker?, msg: () -> Any?) {
        if (logConfig.value.isInfoEnabled)
            KotlinLogging.output("${DateTime.now()} [$platformName][$name][DRILL info] ${msg()}")
    }

    override fun info(marker: Marker?, t: Throwable?, msg: () -> Any?) {
        if (logConfig.value.isInfoEnabled)
            KotlinLogging.output("${DateTime.now()} [$platformName][$name][DRILL info] ${msg()}", t)
    }

    override fun warn(msg: () -> Any?) {
        if (logConfig.value.isWarnEnabled)
            KotlinLogging.output("${DateTime.now()} [$platformName][$name][DRILL warn] ${msg()}")
    }

    override fun warn(t: Throwable?, msg: () -> Any?) {
        if (logConfig.value.isWarnEnabled)
            KotlinLogging.output("${DateTime.now()} [$platformName][$name][DRILL warn] ${msg()}", t)
    }

    override fun warn(marker: Marker?, msg: () -> Any?) {
        if (logConfig.value.isWarnEnabled)
            KotlinLogging.output("${DateTime.now()} [$platformName][$name][DRILL warn] ${msg()}")
    }

    override fun warn(marker: Marker?, t: Throwable?, msg: () -> Any?) {
        if (logConfig.value.isWarnEnabled)
            KotlinLogging.output("${DateTime.now()} [$platformName][$name][DRILL warn] ${msg()}", t)
    }

    override fun error(msg: () -> Any?) {
        KotlinLogging.output("${DateTime.now()} [$platformName][$name][DRILL error] ${msg()}")
    }

    override fun error(t: Throwable?, msg: () -> Any?) {
        KotlinLogging.output("${DateTime.now()} [$platformName][$name][DRILL error] ${msg()}", t)
    }

    override fun error(marker: Marker?, msg: () -> Any?) {
        KotlinLogging.output("${DateTime.now()} [$platformName][$name][DRILL error] ${msg()}")
    }

    override fun error(marker: Marker?, t: Throwable?, msg: () -> Any?) {
        KotlinLogging.output("${DateTime.now()} [$platformName][$name][DRILL error] ${msg()}", t)
    }

    override fun entry(vararg argArray: Any?) {
        throw NotImplementedError("An operation is not implemented")
    }

    override fun exit() {
        throw NotImplementedError("An operation is not implemented")
    }

    override fun <T> exit(result: T): T {
        throw NotImplementedError("An operation is not implemented")
    }

    override fun <T : Throwable> throwing(throwable: T): T {
        throw NotImplementedError("An operation is not implemented")
    }

    override fun <T : Throwable> catching(throwable: T) {
        throw NotImplementedError("An operation is not implemented")
    }

}

expect fun KotlinLogging.output(message: String, throwable: Throwable? = null)
expect fun KotlinLogging.createFd(file: String?)
expect fun LoggerConfig.platformFreeze(): LoggerConfig
expect val platformName: String
