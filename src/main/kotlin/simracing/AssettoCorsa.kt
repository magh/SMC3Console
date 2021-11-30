package simracing

import simracing.Utils.mapRange
import dev.iimetra.assettocorsa4j.telemetry.client.ACClient
import dev.iimetra.assettocorsa4j.telemetry.model.response.CarTelemetry
import dev.iimetra.assettocorsa4j.telemetry.serializer.PoJoToBinarySerializer
import dev.iimetra.assettocorsa4j.telemetry.serializer.Reader
import dev.iimetra.assettocorsa4j.telemetry.serializer.Writer
import mu.KotlinLogging
import java.net.DatagramSocket

private const val LOCAL_IP_ADDRESS = "192.168.2.54" // Connecting...
private const val SERIAL_TTY = "/dev/ttyS33"
//private const val SERIAL_TTY = "/dev/ttyUSB1"

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
                    log.info { "accGFrontal: ${carTelemetry.accGFrontal}" }
                    val r = mapRange(carTelemetry.accGFrontal, -1f, 1f, 712f, 312f)
                    controller.send(Command.sendTarget(Motor.M1, r.toInt()))
                    controller.send(Command.sendTarget(Motor.M2, r.toInt()))
                }
                prev = carTelemetry
            }
        }
    }

}

fun main() {
    AssettoCorsa().run()
}
