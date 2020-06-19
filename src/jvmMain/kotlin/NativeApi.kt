package com.epam.drill.logger

object NativeApi {
    external fun getLogLevel(): Int
    external fun setLogLevel(code: Int)

    external fun setFilename(filename: String?)

    external fun output(message: String)
}
