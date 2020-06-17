package com.epam.drill.logger

object NativeApi {

    external fun nativeOutput(message: String)
    external fun createFd(file: String?)
    external fun getConfig(): Int
    external fun setConfig(rawConfig: Int)
}
