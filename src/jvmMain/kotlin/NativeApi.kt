package com.epam.drill.logger

import com.epam.drill.kni.*

@Kni
actual object NativeApi {

    actual external fun getLogLevel(): Int

    actual external fun setLogLevel(code: Int)

    actual external fun setFilename(filename: String?)

    actual external fun output(message: String)
}
