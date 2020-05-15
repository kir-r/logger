package com.epam.drill.logger

object NativeApi {

    external fun nativeOutput(message: String)
    external fun createFd(file: String?)
    external fun getConfig(): ByteArray
    external fun setConfig(rawConfig: ByteArray)
}