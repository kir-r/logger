package com.epam.drill.logger.internal

import com.epam.drill.logger.*
import com.epam.drill.logger.api.*
import com.soywiz.klock.*
import kotlin.native.concurrent.*

internal expect val platformName: String

internal expect val Throwable.commonStackTrace: Array<out Any>

internal fun appendLogMessage(
    name: String,
    level: LogLevel,
    t: Throwable? = null,
    @Suppress("UNUSED_PARAMETER") marker: Marker? = null,
    msg: () -> Any?
) = run {
    val message = "${timestamp()} [$platformName][${level.name}][$name] ${msg.toStringSafe()}"
    val exception = t?.run { " ${t.message}\n${commonStackTrace.joinToString("\n  ")}" } ?: ""
    Logging.output("$message$exception")
}

@SharedImmutable
private val timestampFmt = DateFormat("yyyy-MM-dd HH:mm:ss:SSS")

private fun timestamp(): String = DateTime.now().toString(timestampFmt)

private fun (() -> Any?).toStringSafe(): String = try {
    invoke().toString()
} catch (e: Exception) {
    "Log message invocation failed: $e"
}
