package simracing

import gnu.io.CommPortIdentifier
import gnu.io.SerialPort

class Smc3Controller {

    private var serialPort: SerialPort? = null

    fun connect(port: String, listener: (response: Command.Response) -> Unit) {
        System.setProperty("java.ext.dirs", ".")
        val portIdentifier = CommPortIdentifier.getPortIdentifier(port)
        //println(portIdentifier.name) // /dev/ttyS33
        //println(portIdentifier.portType) // PORT_SERIAL
        if (portIdentifier.isCurrentlyOwned) {
            println("isCurrentlyOwned")
            return
        }
        val commPort = portIdentifier.open(Smc3Controller::class.java.name, 2000)
        serialPort = commPort as SerialPort
        //500000, 8, 1, NONE
        serialPort?.setSerialPortParams(500000, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE)
        serialPort?.inputStream?.let {
            Thread(SerialReader(it, listener)).start()
        }
    }

    fun send(byteArray: ByteArray) {
        serialPort?.outputStream?.write(byteArray)
    }

}
