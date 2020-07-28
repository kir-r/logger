package com.epam.drill.logger

expect object NativeApi {

    fun getLogLevel(): Int

    fun setLogLevel(code: Int)

    fun setFilename(filename: String?)

    fun output(message: String)
}
