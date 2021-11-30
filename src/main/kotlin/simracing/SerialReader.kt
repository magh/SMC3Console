package simracing

import java.io.InputStream

class SerialReader(private val ins: InputStream, private val listener: (Command.Response) -> Unit) : Runnable {

    private val store = ByteArray(9)

    private var storePos = 0

    private fun append(b: Byte) {
        store[storePos] = b
        storePos++
    }

    private fun clear() {
        storePos = 0
    }

    private fun complete() {
        if (storePos != 3) {
            println("ERROR: ${String(store, 0, storePos)}")
            for (i in 0..storePos) {
                print("${store[i]} ")
            }
            println(" EOL")
        } else {
            val response = Command.handleResponse(store)
            listener.invoke(response)
        }
        storePos = 0
    }

    override fun run() {
        val buffer = ByteArray(9)
        var len = -1
        do {
            try {
                len = ins.read(buffer)
                if (len > -1) {
                    for (i in 0..len) {
                        when (val b = buffer[i]) {
                            '['.code.toByte() -> {
                                clear()
                                break
                            }
                            //0.toByte() -> {}
                            ']'.code.toByte() -> complete()
                            else -> append(b)
                        }
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        } while (len > -1)
    }

}
