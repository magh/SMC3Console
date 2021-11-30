package simracing

enum class Motor(val n: Int) {
    M1(0), M2(1), M3(2);

    companion object {
        fun fromIndex(index: Int): Motor {
            values().forEach {
                if (it.n == index) {
                    return it
                }
            }
            throw Exception("Motor not found: $index")
        }
    }
}

enum class OneValue(val chars: Array<Char>, val desc: String) {
    KP(arrayOf('D', 'E', 'F'), "Kp (100)"),
    KI(arrayOf('G', 'H', 'I'), "Ki (40)"),
    KD(arrayOf('J', 'K', 'L'), "Kd (40)"),
    KS(arrayOf('M', 'N', 'O'), "Ks (1)"),
    DELTALOOPCOUNT(arrayOf('Z', 'Z', 'Z'), "DeltaLoopCount");

    fun toChar(motor: Motor): Char {
        return chars[motor.n]
    }
}

enum class TwoValue(val chars: Array<Char>, val desc1: String, val desc2: String) {
    FEEDBACK_TARGET(arrayOf('A', 'B', 'C'), "Feedback (512)", "Target (512)"),
    DIVIDER_PWMOUT(arrayOf('a', 'b', 'c'), "PIDProcessDivider/0/D3/D2/D1", "PWMOut (0)"),
    PWMMIN_PWMMAX(arrayOf('P', 'Q', 'R'), "PWMoffset (50)", "PWMmax (100)"),
    MOTORMINMAX_INPUTMINMAX(
        arrayOf('S', 'T', 'U'),
        "CutoffLimitMin/Max (23, 1023-23)",
        "InputClipMin/Max (100, 1023-100)"
    ),
    FEEDBACKDEADZONE_PWMREVERSEDUTY(arrayOf('V', 'W', 'X'), "DeadZone (0)", "PWMrev (200)"),
    DIVIDER_EMPTY(arrayOf('Y', 'Y', 'Y'), "PIDProcessDivider/0/D3/D2/D1", "empty/0"),
    TIMER_EMPTY(arrayOf('~', '~', '~'), "Timer1FreqkHz PWM Frequencies to set", "empty/0");

    fun toChar(motor: Motor): Char {
        return chars[motor.n]
    }
}

object Command {

    fun send(type: OneValue, motor: Motor, value: Int): ByteArray {
        val motorChar = type.toChar(motor)
        return createArray(motorChar, value)
    }

    fun send(type: TwoValue, motor: Motor, b1: Int, b2: Int): ByteArray {
        val motorChar = type.toChar(motor)
        return createArray(motorChar, b1.toUByte(), b2.toUByte())
    }

    fun request(type: OneValue, motor: Motor): ByteArray {
        val motorChar = type.toChar(motor)
        return "[rd$motorChar]".toByteArray()
    }

    fun request(type: TwoValue, motor: Motor): ByteArray {
        val motorChar = type.toChar(motor)
        return "[rd$motorChar]".toByteArray()
    }

    fun sendTarget(motor: Motor, value: Int): ByteArray {
        val motorChar = TwoValue.FEEDBACK_TARGET.toChar(motor)
        return createArray(motorChar, value)
    }

    // [mo0] Disable continuous feedback from previous [mo1],[mo2], or [mo3] command
    fun sendDisableFeedback(): ByteArray {
        return "[mo0]".toByteArray()
    }

    // [ena] Enable all motors
    fun sendEnableAllMotors(): ByteArray {
        return "[ena]".toByteArray()
    }

    // [sav] Save parameters to non-volatile memory
    fun sendSaveParameters(): ByteArray {
        return "[sav]".toByteArray()
    }

    // [ver] Request the SMC3 software version, returned value is mult by 100.  (ie: 101 is ver 1.01)
    // [vyy]
    fun requestVersion(): ByteArray {
        return "[ver]".toByteArray()
    }

    // [mo1],[mo2],[mo3] Request continous motor position, feedback, pwm and status data (all packets sent every 15ms)
    // [Axy][Bxy][Cxy][axy][bxy][cxy]
    fun subscribePosition(motor: Motor): ByteArray {
        val motorChar = when (motor) {
            Motor.M1 -> '1'
            Motor.M2 -> '2'
            Motor.M3 -> '3'
        }
        return "[mo$motorChar]".toByteArray()
    }

    private fun toInt(b1: Byte, b2: Byte): UInt {
        return (b1.toUInt().shl(8) + b2.toUInt())
    }

    private fun createArray(firstChar: Char, value: Int): ByteArray {
        val pair = Pair(value.ushr(8).toUByte(), (value and 255).toUByte())
        return createArray(firstChar, pair.first, pair.second)
    }

    private fun createArray(firstChar: Char, b1: UByte, b2: UByte): ByteArray {
        val array = ByteArray(5)
        array[0] = '['.code.toByte()
        array[1] = firstChar.code.toByte()
        array[2] = b1.toByte()
        array[3] = b2.toByte()
        array[4] = ']'.code.toByte()
        return array
    }

    private fun isDisabled(b: UByte, pos: Int): String {
        return (b.toInt().ushr(pos) and 1).toString()
    }

    open class Response
    data class OneValueResponse(val type: OneValue, val motor: Motor, val value: UInt) : Response() {
        override fun toString(): String {
            return "$type $motor ${type.desc}: $value"
        }
    }

    data class TwoValueResponse(val type: TwoValue, val motor: Motor, val b1: UByte, val b2: UByte) : Response() {
        override fun toString(): String {
            return when (type) {
                TwoValue.FEEDBACK_TARGET -> {
                    "${type.desc1}: ${b1 * 4u} ${type.desc2}: ${b2 * 4u} $motor $type"
                }
                TwoValue.DIVIDER_PWMOUT -> {
                    "Divider: ${b1.toInt().ushr(4)} " +
                            "M3: ${isDisabled(b1, 2)} " +
                            "M2: ${isDisabled(b1, 1)} " +
                            "M1: ${isDisabled(b1, 0)} " +
                            "${type.desc2}: ${b2} $motor $type"
                }
                else -> "$type $motor ${type.desc1}: $b1 ${type.desc2}: $b2"
            }
        }
    }

    data class Version(val version: UInt) : Response()

    fun handleResponse(buffer: ByteArray): Response {
        OneValue.values().forEach { value ->
            value.chars.forEachIndexed { index, ch ->
                if (buffer[0] == ch.code.toByte()) {
                    return OneValueResponse(value, Motor.fromIndex(index), toInt(buffer[1], buffer[2]))
                }
            }
        }
        TwoValue.values().forEach { value ->
            value.chars.forEachIndexed { index, ch ->
                if (buffer[0] == ch.code.toByte()) {
                    return TwoValueResponse(value, Motor.fromIndex(index), buffer[1].toUByte(), buffer[2].toUByte())
                }
            }
        }
        if (buffer[0] == 'v'.code.toByte()) {
            return Version(toInt(buffer[1], buffer[2])) // value is mult by 100.  (ie: 101 is ver 1.01)
        }
        throw IllegalStateException("Unhandled response ${buffer[0]}/${buffer[1]}/${buffer[2]}")
    }
}
