package simracing.forza

import simracing.Command
import simracing.Motor
import simracing.Utils.mapRange
import simracing.Smc3Controller
import kotlin.math.abs
import mu.KotlinLogging
import java.io.IOException
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.SocketException
import java.util.*

private const val SERIAL_TTY = "/dev/ttyS33"
//private const val SERIAL_TTY = "/dev/ttyUSB1"

private const val SENSITIVITY_HORIZONTAL_LEFT = -20f
private const val SENSITIVITY_HORIZONTAL_RIGHT = 20f
private const val SENSITIVITY_FRONTAL_REV = -20f
private const val SENSITIVITY_FRONTAL_FORW = 20f
private const val OUTPUT_MIN = 712f
private const val OUTPUT_MAX = 312f

private val logger = KotlinLogging.logger {}

class ForzaMain {
    fun run() {
        logger.info("Starting")
        val controller = Smc3Controller()
        controller.connect(SERIAL_TTY) {
            logger.info { it }
        }

        var api: ForzaApi //ForzaApi class, UDP data is streamed and parsed here eventually
        val UDP_PORT = 5300 //This can be any port you want, just be sure to set it in your game settings
        var ds: DatagramSocket? = null //Initialize UDP stream socket
        ds = try {
            DatagramSocket(UDP_PORT)
        } catch (e: SocketException) {
            logger.info(e.message, e)
            throw RuntimeException(e)
        }
        logger.info("Listning on port: $UDP_PORT")
        var receive = ByteArray(323) //Create packet buffer size, which in this case is 312
        while (true) {  //This will loop and receive data realtime
            val dp = DatagramPacket(receive, receive.size) //Pass bytes and packet size
            try {
                ds?.receive(dp) //receive UDP data stream and pass it to DatagramPacket
            } catch (e: IOException) {
                logger.info(e.message, e)
            }
            api = ForzaApi(dp.data) //Initialize ForzaApi and pass stream data to be parsed
            //logger.info("accX: $api.accelerationX accY: $api.accelerationY accZ: $api.accelerationZ")
            receive = ByteArray(323) //Clear byte buffer

            //calc
            val aX: Float? = api.accelerationX
            val aY: Float? = api.accelerationY
            val aZ: Float? = api.accelerationZ
            if(aX != null && aY != null && aZ != null){
                calc(aZ, aX) { m1, m2 ->
                    controller.send(Command.sendTarget(Motor.M1, m1))
                    controller.send(Command.sendTarget(Motor.M2, m2))
                }
            }
        }
    }

    companion object {
        fun calc(gFrontal: Float, gHorizontal: Float, block: (m1: Int, m2: Int) -> Unit) {
            val right: Boolean = gHorizontal < 0
            val habs: Float = abs(gHorizontal)
            val m1add: Float = if (!right) habs else 0f
            val m2add: Float = if (right) habs else 0f
            val m1 = mapRange(
                (gFrontal + m1add),
                SENSITIVITY_FRONTAL_REV,
                SENSITIVITY_FRONTAL_FORW,
                OUTPUT_MIN,
                OUTPUT_MAX
            )
            val m2 = mapRange(
                (gFrontal + m2add),
                SENSITIVITY_FRONTAL_REV,
                SENSITIVITY_FRONTAL_FORW,
                OUTPUT_MIN,
                OUTPUT_MAX
            )
            block.invoke(m1.toInt(), m2.toInt())
        }
    }

}

fun main() {
    ForzaMain().run()
}
