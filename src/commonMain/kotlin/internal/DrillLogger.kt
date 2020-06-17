package com.epam.drill.logger.internal

import com.epam.drill.logger.*
import com.soywiz.klock.*

internal class DrillLogger(private val name: String) : KLogger {
    override fun trace(msg: () -> Any?) = logConfig.value.isTraceEnabled.ifTrue {
        Logging.output(format(LogLevel.TRACE, name, msg))
    }

    override fun trace(t: Throwable?, msg: () -> Any?) = logConfig.value.isTraceEnabled.ifTrue {
        Logging.output(format(LogLevel.TRACE, name, msg), t)
    }

    override fun trace(marker: Marker?, msg: () -> Any?) = logConfig.value.isTraceEnabled.ifTrue {
        Logging.output(format(LogLevel.TRACE, name, msg))
    }

    override fun trace(marker: Marker?, t: Throwable?, msg: () -> Any?) = logConfig.value.isTraceEnabled.ifTrue {
        Logging.output(format(LogLevel.TRACE, name, msg), t)
    }

    override fun debug(msg: () -> Any?) = logConfig.value.isDebugEnabled.ifTrue {
        Logging.output(format(LogLevel.DEBUG, name, msg))
    }

    override fun debug(t: Throwable?, msg: () -> Any?) = logConfig.value.isDebugEnabled.ifTrue {
        Logging.output(format(LogLevel.DEBUG, name, msg), t)
    }

    override fun debug(marker: Marker?, msg: () -> Any?) = logConfig.value.isDebugEnabled.ifTrue {
        Logging.output(format(LogLevel.DEBUG, name, msg))
    }

    override fun debug(marker: Marker?, t: Throwable?, msg: () -> Any?) = logConfig.value.isDebugEnabled.ifTrue {
        Logging.output(format(LogLevel.DEBUG, name, msg), t)
    }

    override fun info(msg: () -> Any?) = logConfig.value.isInfoEnabled.ifTrue {
        Logging.output(format(LogLevel.INFO, name, msg))
    }

    override fun info(t: Throwable?, msg: () -> Any?) = logConfig.value.isInfoEnabled.ifTrue {
        Logging.output(format(LogLevel.INFO, name, msg), t)
    }

    override fun info(marker: Marker?, msg: () -> Any?) = logConfig.value.isInfoEnabled.ifTrue {
        Logging.output(format(LogLevel.INFO, name, msg))
    }

    override fun info(marker: Marker?, t: Throwable?, msg: () -> Any?) = logConfig.value.isInfoEnabled.ifTrue {
        Logging.output(format(LogLevel.INFO, name, msg), t)
    }

    override fun warn(msg: () -> Any?) = logConfig.value.isWarnEnabled.ifTrue {
        Logging.output(format(LogLevel.WARN, name, msg))
    }

    override fun warn(t: Throwable?, msg: () -> Any?) = logConfig.value.isWarnEnabled.ifTrue {
        Logging.output(format(LogLevel.WARN, name, msg), t)
    }

    override fun warn(marker: Marker?, msg: () -> Any?) = logConfig.value.isWarnEnabled.ifTrue {
        Logging.output(format(LogLevel.WARN, name, msg))
    }

    override fun warn(marker: Marker?, t: Throwable?, msg: () -> Any?) = logConfig.value.isWarnEnabled.ifTrue {
        Logging.output(format(LogLevel.WARN, name, msg), t)
    }

    override fun error(msg: () -> Any?) {
        Logging.output(format(LogLevel.ERROR, name, msg))
    }

    override fun error(t: Throwable?, msg: () -> Any?) {
        Logging.output(format(LogLevel.ERROR, name, msg), t)
    }

    override fun error(marker: Marker?, msg: () -> Any?) {
        Logging.output(format(LogLevel.ERROR, name, msg))
    }

    override fun error(marker: Marker?, t: Throwable?, msg: () -> Any?) {
        Logging.output(format(LogLevel.ERROR, name, msg), t)
    }
}

private fun format(
    level: LogLevel,
    name: String,
    msg: () -> Any?
) = "${DateTime.now().toString(DateFormat.FORMAT1)} [DRILL][$platformName][${level.name}][$name] ${msg()}"

private inline fun Boolean.ifTrue(block: () -> Unit) {
    if (this) block()
}
