package simracing

private val SERIAL_TTY = "/dev/ttyS33"
//private val SERIAL_TTY = "/dev/ttyUSB1"

class Smc3Console(val controller: Smc3Controller) {
    fun run() {
        while (true) {
            try {
                println("Input: ")
                val byteArray: ByteArray = when (readLine()) {
                    // div
                    "01" -> Command.sendEnableAllMotors()
                    "02" -> Command.sendDisableFeedback()
                    "03" -> Command.requestVersion()
                    // M1
                    "11" -> Command.request(TwoValue.FEEDBACK_TARGET, Motor.M1)
                    "12" -> Command.request(TwoValue.DIVIDER_PWMOUT, Motor.M1)
                    "13" -> Command.subscribePosition(Motor.M1)
                    "14" -> Command.sendTarget(Motor.M1, readInt("Target: "))
                    // M2
                    "21" -> Command.request(TwoValue.FEEDBACK_TARGET, Motor.M2)
                    "22" -> Command.request(TwoValue.DIVIDER_PWMOUT, Motor.M2)
                    "23" -> Command.subscribePosition(Motor.M2)
                    "24" -> Command.sendTarget(Motor.M2, readInt("Target: "))
                    // M1 div request
                    "31" -> Command.request(TwoValue.PWMMIN_PWMMAX, Motor.M1)
                    "32" -> Command.request(TwoValue.MOTORMINMAX_INPUTMINMAX, Motor.M1)
                    "33" -> Command.request(TwoValue.FEEDBACKDEADZONE_PWMREVERSEDUTY, Motor.M1)
                    "34" -> Command.request(OneValue.KP, Motor.M1)
                    "35" -> Command.request(OneValue.KI, Motor.M1)
                    "36" -> Command.request(OneValue.KD, Motor.M1)
                    "37" -> Command.request(OneValue.KS, Motor.M1)
                    // M1 div send
                    "41" -> Command.send(OneValue.KP, Motor.M1, readInt("Kp: "))
                    "42" -> Command.send(OneValue.KI, Motor.M1, readInt("Ki: "))
                    "43" -> Command.send(OneValue.KD, Motor.M1, readInt("Kd: "))
                    "44" -> Command.send(OneValue.KS, Motor.M1, readInt("Ks: "))
                    "45" -> Command.send(TwoValue.DIVIDER_PWMOUT, Motor.M1, readInt("Divider: "), readInt("PWM Out: "))
                    "46" -> Command.send(TwoValue.PWMMIN_PWMMAX, Motor.M1, readInt("PWM Min: "), readInt("PWM Max: "))
                    "47" -> Command.send(
                        TwoValue.MOTORMINMAX_INPUTMINMAX,
                        Motor.M1,
                        readInt("Motor Min/Max: "),
                        readInt("Input Min/Max: ")
                    )
                    "48" -> Command.send(
                        TwoValue.FEEDBACKDEADZONE_PWMREVERSEDUTY,
                        Motor.M1,
                        readInt("Feedback deadzone: "),
                        readInt("PWM reverse duty: ")
                    )
                    // M2 div request
                    "51" -> Command.request(TwoValue.PWMMIN_PWMMAX, Motor.M1)
                    "52" -> Command.request(TwoValue.MOTORMINMAX_INPUTMINMAX, Motor.M1)
                    "53" -> Command.request(TwoValue.FEEDBACKDEADZONE_PWMREVERSEDUTY, Motor.M1)
                    "54" -> Command.request(OneValue.KP, Motor.M1)
                    "55" -> Command.request(OneValue.KI, Motor.M1)
                    "56" -> Command.request(OneValue.KD, Motor.M1)
                    "57" -> Command.request(OneValue.KS, Motor.M1)
                    // M2 div send
                    "61" -> Command.send(OneValue.KP, Motor.M1, readInt("Kp: "))
                    "62" -> Command.send(OneValue.KI, Motor.M1, readInt("Ki: "))
                    "63" -> Command.send(OneValue.KD, Motor.M1, readInt("Kd: "))
                    "64" -> Command.send(OneValue.KS, Motor.M1, readInt("Ks: "))
                    "65" -> Command.send(TwoValue.DIVIDER_PWMOUT, Motor.M1, readInt("Divider: "), readInt("PWM Out: "))
                    "66" -> Command.send(TwoValue.PWMMIN_PWMMAX, Motor.M1, readInt("PWM Min: "), readInt("PWM Max: "))
                    "67" -> Command.send(
                        TwoValue.MOTORMINMAX_INPUTMINMAX,
                        Motor.M1,
                        readInt("Motor Min/Max: "),
                        readInt("Input Min/Max: ")
                    )
                    "68" -> Command.send(
                        TwoValue.FEEDBACKDEADZONE_PWMREVERSEDUTY,
                        Motor.M1,
                        readInt("Feedback deadzone: "),
                        readInt("PWM reverse duty: ")
                    )
                    // else
                    else -> throw IllegalArgumentException()
                }
                controller.send(byteArray)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun readInt(s: String): Int {
        println(s)
        return readLine()?.toInt() ?: throw Exception("Invalid $s")
    }

}

fun main() {
    val controller = Smc3Controller()
    controller.connect(SERIAL_TTY){
        println("Response: $it")
    }
    Smc3Console(controller).run()
}
