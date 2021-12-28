package simracing

import simracing.Utils.mapRange
import dev.iimetra.assettocorsa4j.telemetry.client.ACClient
import dev.iimetra.assettocorsa4j.telemetry.model.response.CarTelemetry
import dev.iimetra.assettocorsa4j.telemetry.serializer.PoJoToBinarySerializer
import dev.iimetra.assettocorsa4j.telemetry.serializer.Reader
import dev.iimetra.assettocorsa4j.telemetry.serializer.Writer
import mu.KotlinLogging
import java.net.DatagramSocket

private const val LOCAL_IP_ADDRESS = "192.168.2.59" // Connecting...
private const val SERIAL_TTY = "/dev/ttyS33"
//private const val SERIAL_TTY = "/dev/ttyUSB1"

private const val SENSITIVITY_HORIZONTAL_LEFT = -2f
private const val SENSITIVITY_HORIZONTAL_RIGHT = 2f
private const val SENSITIVITY_FRONTAL_REV = -2f
private const val SENSITIVITY_FRONTAL_FORW = 2f
private const val OUTPUT_MIN = 712f
private const val OUTPUT_MAX = 312f

private val log = KotlinLogging.logger {}

class AssettoCorsa {

    fun run() {
        val controller = Smc3Controller()
        controller.connect(SERIAL_TTY) {
            log.info { it }
        }

        val writer = Writer()
        val reader = Reader()
        val serializer = PoJoToBinarySerializer(writer, reader)
        val socket = DatagramSocket()
        val client = ACClient.of(LOCAL_IP_ADDRESS, socket, serializer)

        log.info { "Connecting to Assetto Corsa..." }
        val connection = client.connect()
        log.info { "Connected. Car: ${connection.carName}" }
        client.subscribeCarTelemetry()

        var prev: CarTelemetry? = null
        while (true) {
            val carTelemetry = client.carTelemetry
            if (!carTelemetry.equals(prev)) {
                if (carTelemetry.accGFrontal != prev?.accGFrontal) {
                    //log.info { "accGFrontal: ${carTelemetry.accGFrontal} ${carTelemetry.accGHorizontal}" }
                    calc(carTelemetry.accGFrontal, carTelemetry.accGHorizontal) { m1, m2 ->
                        controller.send(Command.sendTarget(Motor.M1, m1))
                        controller.send(Command.sendTarget(Motor.M2, m2))
                    }
                }
                prev = carTelemetry
            }
        }
    }

    companion object {
        fun calc(gFrontal: Float, gHorizontal: Float, block: (m1: Int, m2: Int) -> Unit) {
            val m1 = mapRange(
                (gFrontal + gHorizontal),
                SENSITIVITY_FRONTAL_REV,
                SENSITIVITY_FRONTAL_FORW,
                OUTPUT_MIN,
                OUTPUT_MAX
            )
            val m2 = mapRange(
                (gFrontal + (-1 * gHorizontal)),
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
    AssettoCorsa().run()
}
