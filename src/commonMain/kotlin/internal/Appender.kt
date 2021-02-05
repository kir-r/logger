/**
 * Copyright 2020 EPAM Systems
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.epam.drill.logger.internal

import com.epam.drill.logger.*
import com.epam.drill.logger.api.*
import com.soywiz.klock.*
import kotlin.native.concurrent.*

internal expect val platformName: String

internal fun appendLogMessage(
    name: String,
    level: LogLevel,
    t: Throwable? = null,
    @Suppress("UNUSED_PARAMETER") marker: Marker? = null,
    msg: () -> Any?
) = run {
    val message = "${timestamp()} [$platformName][${level.name}][$name] ${msg.toStringSafe()}"
    val exception = t?.stackTraceToString() ?: ""
    Logging.output("$message $exception")
}

@SharedImmutable
private val timestampFmt = DateFormat("yyyy-MM-dd HH:mm:ss:SSS")

private fun timestamp(): String = DateTime.now().toString(timestampFmt)

private fun (() -> Any?).toStringSafe(): String = try {
    invoke().toString()
} catch (e: Exception) {
    "Log message invocation failed: $e"
}
