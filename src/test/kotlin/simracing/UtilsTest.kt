package simracing

import simracing.Utils.mapRange
import kotlin.test.Test
import kotlin.test.assertEquals

class UtilsTest {

    @Test
    fun testMapRange() {
        assertEquals(expected = 912f, actual = mapRange(-20f, -10f, 10f, 712f, 312f))
        assertEquals(expected = 712f, actual = mapRange(-10f, -10f, 10f, 712f, 312f))
        assertEquals(expected = 512f, actual = mapRange(0f, -10f, 10f, 712f, 312f))
        assertEquals(expected = 312f, actual = mapRange(10f, -10f, 10f, 712f, 312f))
        assertEquals(expected = 112f, actual = mapRange(20f, -10f, 10f, 712f, 312f))
    }

}