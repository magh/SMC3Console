package simracing.forza

import kotlin.Throws
import simracing.forza.ForzaApi
import java.lang.Exception
import java.nio.ByteBuffer
import java.nio.ByteOrder

class ForzaApi(bytes: ByteArray) {
    private var readOK = false
    var isRaceOn = false
    var timeStampMS: Long? = null
    private var engineMaxRpm: Float? = null
    var engineIdleRpm: Float? = null
    private var currentEngineRpm: Float? = null
    var accelerationX: Float? = null
    var accelerationY: Float? = null
    var accelerationZ: Float? = null
    var velocityX: Float? = null
    var velocityY: Float? = null
    var velocityZ: Float? = null
    var angularVelocityX: Float? = null
    var angularVelocityY: Float? = null
    var angularVelocityZ: Float? = null
    var yaw: Float? = null
    var pitch: Float? = null
    var roll: Float? = null
    var normalizedSuspensionTravelFrontLeft: Float? = null
    var normalizedSuspensionTravelFrontRight: Float? = null
    var normalizedSuspensionTravelRearLeft: Float? = null
    var normalizedSuspensionTravelRearRight: Float? = null
    var tireSlipRatioFrontLeft: Float? = null
    var tireSlipRatioFrontRight: Float? = null
    var tireSlipRatioRearLeft: Float? = null
    var tireSlipRatioRearRight: Float? = null
    var wheelRotationSpeedFrontLeft: Float? = null
    var wheelRotationSpeedFrontRight: Float? = null
    var wheelRotationSpeedRearLeft: Float? = null
    var wheelRotationSpeedRearRight: Float? = null
    var wheelOnRumbleStripFrontLeft: Int? = null
    var wheelOnRumbleStripFrontRight: Int? = null
    var wheelOnRumbleStripRearLeft: Int? = null
    var wheelOnRumbleStripRearRight: Int? = null
    var wheelInPuddleDepthFrontLeft: Float? = null
    var wheelInPuddleDepthFrontRight: Float? = null
    var wheelInPuddleDepthRearLeft: Float? = null
    var wheelInPuddleDepthRearRight: Float? = null
    var surfaceRumbleFrontLeft: Float? = null
    var surfaceRumbleFrontRight: Float? = null
    var surfaceRumbleRearLeft: Float? = null
    var surfaceRumbleRearRight: Float? = null
    var tireSlipAngleFrontLeft: Float? = null
    var tireSlipAngleFrontRight: Float? = null
    var tireSlipAngleRearLeft: Float? = null
    var tireSlipAngleRearRight: Float? = null
    var tireCombinedSlipFrontLeft: Float? = null
    var tireCombinedSlipFrontRight: Float? = null
    var tireCombinedSlipRearLeft: Float? = null
    var tireCombinedSlipRearRight: Float? = null
    var suspensionTravelMetersFrontLeft: Float? = null
    var suspensionTravelMetersFrontRight: Float? = null
    var suspensionTravelMetersRearLeft: Float? = null
    var suspensionTravelMetersRearRight: Float? = null
    private var carClass: Int? = null
    var carPerformanceIndex: Int? = null
    private var drivetrainType: Int? = null
    var numCylinders: Int? = null
    private var carType: Int? = null
    var unknown1: Byte? = null
    var unknown2: Byte? = null
    var unknown3: Byte? = null
    var unknown4: Byte? = null
    var unknown5: Byte? = null
    var unknown6: Byte? = null
    var unknown7: Byte? = null
    var unknown8: Byte? = null
    var carOrdinal: Int? = null
    var positionX: Float? = null
    var positionY: Float? = null
    var positionZ: Float? = null
    var speedMPS: Float? = null
    var power: Float? = null
    var torque: Float? = null
    var tireTempFrontLeft: Float? = null
    var tireTempFrontRight: Float? = null
    var tireTempRearLeft: Float? = null
    var tireTempRearRight: Float? = null
    var boost: Float? = null
    var fuel: Float? = null
    var distanceTraveled: Float? = null
    var bestLap: Float? = null
    var lastLap: Float? = null
    var currentLap: Float? = null
    var currentRaceTime: Float? = null
    var lapNumber: Short? = null
    var racePosition: Byte? = null
    var accel: Byte? = null
    var brake: Byte? = null
    var clutch: Byte? = null
    var handbrake: Byte? = null
    var gear: Byte? = null
    var steer: Byte? = null
    var normalizedDrivingLine: Byte? = null
    var normalizedAIBrakeDifference: Byte? = null
    fun getEngineMaxRpm(): Float {
        return engineMaxRpm!!
    }

    fun getCurrentEngineRpm(): Float {
        return currentEngineRpm!!
    }

    fun getCarClass(): String {
        when (carClass) {
            0 -> return "D"
            1 -> return "C"
            2 -> return "B"
            3 -> return "A"
            4 -> return "S1"
            5 -> return "S2"
            6 -> return "X"
        }
        return "-"
    }

    val drivetrain: String
        get() = when (drivetrainType) {
            0 -> "FWD"
            1 -> "RWD"
            2 -> "AWD"
            else -> "-"
        }

    fun getCarType(): String {
        return when (carType) {
            11 -> "Modern Super Cars"
            12 -> "Retro Super Cars"
            13 -> "Hyper Cars"
            14 -> "Retro Saloons"
            16 -> "Vans & Utility"
            17 -> "Retro Sports Cars"
            18 -> "Modern Sports Cars"
            19 -> "Super Saloons"
            20 -> "Classic Racers"
            21 -> "Cult Cars"
            22 -> "Rare Classics"
            25 -> "Super Hot Hatch"
            29 -> "Rods & Customs"
            30 -> "Retro Muscle"
            31 -> "Modern Muscle"
            32 -> "Retro Rally"
            33 -> "Classic Rally"
            34 -> "Rally Monsters"
            35 -> "Modern Rally"
            36 -> "GT Cars"
            37 -> "Super GT"
            38 -> "Extreme Offroad"
            39 -> "Sports Utility Heroes"
            40 -> "Offroad"
            41 -> "Offroad Buggies"
            42 -> "Classic Sports Cars"
            43 -> "Track Toys"
            44 -> "Vintage Racers"
            45 -> "Trucks"
            else -> "Unknown ($carType)"
        }
    }

    val speedKPH: Int
        get() = Math.round(speedMPS!! * 3.6f)
    val speedMPH: Int
        get() = Math.round(speedMPS!! * 2.23694f)

    // Watts > HP
    val horsePower: Float
        get() = power!! * 0.00134102f

    fun ReadOK(): Boolean {
        return readOK
    }

    val averageVelocity: Float
        get() = getVector3DLength(velocityX!!, velocityY!!, velocityZ!!)
    val tireTempAverageFront: Float
        get() = getAverage(tireTempFrontLeft!!, tireTempFrontRight!!)
    val tireTempAverageRear: Float
        get() = getAverage(tireTempRearLeft!!, tireTempRearRight!!)

    private fun getAverage(valueOne: Float, valueTwo: Float, valueThree: Float, valueFour: Float): Float {
        return (valueOne + valueTwo + valueThree + valueFour) / 4f
    }

    val tireTempAverageLeft: Float
        get() = getAverage(
            tireTempFrontLeft!!,
            tireTempRearLeft!!
        )
    val tireTempAverageRight: Float
        get() = getAverage(tireTempFrontRight!!, tireTempRearRight!!)
    val tireTempAverageTotal: Float
        get() = getAverage(
            tireTempFrontLeft!!,
            tireTempFrontRight!!,
            tireTempRearLeft!!,
            tireTempRearRight!!
        )

    private fun getAverage(valueOne: Float, valueTwo: Float): Float {
        return (valueOne + valueTwo) / 2f
    }

    private fun getVector3DLength(x: Float, y: Float, z: Float): Float {
        return Math.sqrt((x * x + y * y + z * z).toDouble()).toFloat()
    }

    companion object {
        const val PACKET_SIZE = 323
        private fun checkBuffer(buffer: ByteBuffer, size: Int): Boolean {
            return buffer.hasRemaining() && buffer.remaining() >= size
        }

        @Throws(Exception::class)
        private fun <T> getFromBuffer(buffer: ByteBuffer, clazz: Class<T>?): T {
            when (clazz!!.name) {
                "int" -> return (if (checkBuffer(buffer, 4)) buffer.int else 0) as T
                "long" -> return (if (checkBuffer(buffer, 4)) Integer.toUnsignedLong(buffer.int) else 0L) as T
                "byte" -> return (if (checkBuffer(buffer, 1)) buffer.get() else 0) as T
                "float" -> return (if (checkBuffer(buffer, 4)) buffer.float else 0f) as T
                "short" -> return (if (checkBuffer(buffer, 2)) buffer.short else 0) as T
            }
            throw Exception("Invalid Type")
        }
    }

    init {
        readOK = false
        if (bytes.size < PACKET_SIZE) {
            try {
                throw Exception("Invalid len")
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        val bb = ByteBuffer.wrap(bytes)
        bb.order(ByteOrder.LITTLE_ENDIAN)
        try {
            isRaceOn = if (getFromBuffer(bb, Int::class.javaPrimitiveType) == 1) true else false
            timeStampMS = getFromBuffer(bb, Long::class.javaPrimitiveType)
            engineMaxRpm = getFromBuffer(bb, Float::class.javaPrimitiveType)
            engineIdleRpm = getFromBuffer(bb, Float::class.javaPrimitiveType)
            currentEngineRpm = getFromBuffer(bb, Float::class.javaPrimitiveType)
            accelerationX = getFromBuffer(bb, Float::class.javaPrimitiveType)
            accelerationY = getFromBuffer(bb, Float::class.javaPrimitiveType)
            accelerationZ = getFromBuffer(bb, Float::class.javaPrimitiveType)
            velocityX = getFromBuffer(bb, Float::class.javaPrimitiveType)
            velocityY = getFromBuffer(bb, Float::class.javaPrimitiveType)
            velocityZ = getFromBuffer(bb, Float::class.javaPrimitiveType)
            angularVelocityX = getFromBuffer(bb, Float::class.javaPrimitiveType)
            angularVelocityY = getFromBuffer(bb, Float::class.javaPrimitiveType)
            angularVelocityZ = getFromBuffer(bb, Float::class.javaPrimitiveType)
            yaw = getFromBuffer(bb, Float::class.javaPrimitiveType)
            pitch = getFromBuffer(bb, Float::class.javaPrimitiveType)
            roll = getFromBuffer(bb, Float::class.javaPrimitiveType)
            normalizedSuspensionTravelFrontLeft = getFromBuffer(bb, Float::class.javaPrimitiveType)
            normalizedSuspensionTravelFrontRight = getFromBuffer(bb, Float::class.javaPrimitiveType)
            normalizedSuspensionTravelRearLeft = getFromBuffer(bb, Float::class.javaPrimitiveType)
            normalizedSuspensionTravelRearRight = getFromBuffer(bb, Float::class.javaPrimitiveType)
            tireSlipRatioFrontLeft = getFromBuffer(bb, Float::class.javaPrimitiveType)
            tireSlipRatioFrontRight = getFromBuffer(bb, Float::class.javaPrimitiveType)
            tireSlipRatioRearLeft = getFromBuffer(bb, Float::class.javaPrimitiveType)
            tireSlipRatioRearRight = getFromBuffer(bb, Float::class.javaPrimitiveType)
            wheelRotationSpeedFrontLeft = getFromBuffer(bb, Float::class.javaPrimitiveType)
            wheelRotationSpeedFrontRight = getFromBuffer(bb, Float::class.javaPrimitiveType)
            wheelRotationSpeedRearLeft = getFromBuffer(bb, Float::class.javaPrimitiveType)
            wheelRotationSpeedRearRight = getFromBuffer(bb, Float::class.javaPrimitiveType)
            wheelOnRumbleStripFrontLeft = getFromBuffer(bb, Int::class.javaPrimitiveType)
            wheelOnRumbleStripFrontRight = getFromBuffer(bb, Int::class.javaPrimitiveType)
            wheelOnRumbleStripRearLeft = getFromBuffer(bb, Int::class.javaPrimitiveType)
            wheelOnRumbleStripRearRight = getFromBuffer(bb, Int::class.javaPrimitiveType)
            wheelInPuddleDepthFrontLeft = getFromBuffer(bb, Float::class.javaPrimitiveType)
            wheelInPuddleDepthFrontRight = getFromBuffer(bb, Float::class.javaPrimitiveType)
            wheelInPuddleDepthRearLeft = getFromBuffer(bb, Float::class.javaPrimitiveType)
            wheelInPuddleDepthRearRight = getFromBuffer(bb, Float::class.javaPrimitiveType)
            surfaceRumbleFrontLeft = getFromBuffer(bb, Float::class.javaPrimitiveType)
            surfaceRumbleFrontRight = getFromBuffer(bb, Float::class.javaPrimitiveType)
            surfaceRumbleRearLeft = getFromBuffer(bb, Float::class.javaPrimitiveType)
            surfaceRumbleRearRight = getFromBuffer(bb, Float::class.javaPrimitiveType)
            tireSlipAngleFrontLeft = getFromBuffer(bb, Float::class.javaPrimitiveType)
            tireSlipAngleFrontRight = getFromBuffer(bb, Float::class.javaPrimitiveType)
            tireSlipAngleRearLeft = getFromBuffer(bb, Float::class.javaPrimitiveType)
            tireSlipAngleRearRight = getFromBuffer(bb, Float::class.javaPrimitiveType)
            tireCombinedSlipFrontLeft = getFromBuffer(bb, Float::class.javaPrimitiveType)
            tireCombinedSlipFrontRight = getFromBuffer(bb, Float::class.javaPrimitiveType)
            tireCombinedSlipRearLeft = getFromBuffer(bb, Float::class.javaPrimitiveType)
            tireCombinedSlipRearRight = getFromBuffer(bb, Float::class.javaPrimitiveType)
            suspensionTravelMetersFrontLeft = getFromBuffer(bb, Float::class.javaPrimitiveType)
            suspensionTravelMetersFrontRight = getFromBuffer(bb, Float::class.javaPrimitiveType)
            suspensionTravelMetersRearLeft = getFromBuffer(bb, Float::class.javaPrimitiveType)
            suspensionTravelMetersRearRight = getFromBuffer(bb, Float::class.javaPrimitiveType)
            carOrdinal = getFromBuffer(bb, Int::class.javaPrimitiveType)
            carClass = getFromBuffer(bb, Int::class.javaPrimitiveType)
            carPerformanceIndex = getFromBuffer(bb, Int::class.javaPrimitiveType)
            drivetrainType = getFromBuffer(bb, Int::class.javaPrimitiveType)
            numCylinders = getFromBuffer(bb, Int::class.javaPrimitiveType)
            carType = getFromBuffer(bb, Int::class.javaPrimitiveType)
            unknown1 = getFromBuffer(bb, Byte::class.javaPrimitiveType)
            unknown2 = getFromBuffer(bb, Byte::class.javaPrimitiveType)
            unknown3 = getFromBuffer(bb, Byte::class.javaPrimitiveType)
            unknown4 = getFromBuffer(bb, Byte::class.javaPrimitiveType)
            unknown5 = getFromBuffer(bb, Byte::class.javaPrimitiveType)
            unknown6 = getFromBuffer(bb, Byte::class.javaPrimitiveType)
            unknown7 = getFromBuffer(bb, Byte::class.javaPrimitiveType)
            unknown8 = getFromBuffer(bb, Byte::class.javaPrimitiveType)
            positionX = getFromBuffer(bb, Float::class.javaPrimitiveType)
            positionY = getFromBuffer(bb, Float::class.javaPrimitiveType)
            positionZ = getFromBuffer(bb, Float::class.javaPrimitiveType)
            speedMPS = getFromBuffer(bb, Float::class.javaPrimitiveType)
            power = getFromBuffer(bb, Float::class.javaPrimitiveType)
            torque = getFromBuffer(bb, Float::class.javaPrimitiveType)
            tireTempFrontLeft = getFromBuffer(bb, Float::class.javaPrimitiveType)
            tireTempFrontRight = getFromBuffer(bb, Float::class.javaPrimitiveType)
            tireTempRearLeft = getFromBuffer(bb, Float::class.javaPrimitiveType)
            tireTempRearRight = getFromBuffer(bb, Float::class.javaPrimitiveType)
            boost = getFromBuffer(bb, Float::class.javaPrimitiveType)
            fuel = getFromBuffer(bb, Float::class.javaPrimitiveType)
            distanceTraveled = getFromBuffer(bb, Float::class.javaPrimitiveType)
            bestLap = getFromBuffer(bb, Float::class.javaPrimitiveType)
            lastLap = getFromBuffer(bb, Float::class.javaPrimitiveType)
            currentLap = getFromBuffer(bb, Float::class.javaPrimitiveType)
            currentRaceTime = getFromBuffer(bb, Float::class.javaPrimitiveType)
            lapNumber = getFromBuffer(bb, Short::class.javaPrimitiveType)
            racePosition = getFromBuffer(bb, Byte::class.javaPrimitiveType)
            accel = getFromBuffer(bb, Byte::class.javaPrimitiveType)
            brake = getFromBuffer(bb, Byte::class.javaPrimitiveType)
            clutch = getFromBuffer(bb, Byte::class.javaPrimitiveType)
            handbrake = getFromBuffer(bb, Byte::class.javaPrimitiveType)
            gear = getFromBuffer(bb, Byte::class.javaPrimitiveType)
            steer = getFromBuffer(bb, Byte::class.javaPrimitiveType)
            normalizedDrivingLine = getFromBuffer(bb, Byte::class.javaPrimitiveType)
            normalizedAIBrakeDifference = getFromBuffer(bb, Byte::class.javaPrimitiveType)
            readOK = bb.position() == PACKET_SIZE
        } catch (e: Exception) {
            try {
                throw Exception("Failed to parse Packet")
            } catch (exception: Exception) {
                exception.printStackTrace()
            }
        }
    }
}