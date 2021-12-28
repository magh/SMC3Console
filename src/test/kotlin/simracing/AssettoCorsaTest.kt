package simracing

import kotlin.test.Test
import kotlin.test.assertEquals

class AssettoCorsaTest {

    @Test
    fun testCalc() {
        //rev
        AssettoCorsa.calc(-1f, 0f) { m1, m2 ->
            assertEquals(expected = 712, actual = m1)
            assertEquals(expected = 712, actual = m2)
        }
        //forw
        AssettoCorsa.calc(1f, 0f) { m1, m2 ->
            assertEquals(expected = 312, actual = m1)
            assertEquals(expected = 312, actual = m2)
        }
        //left
        AssettoCorsa.calc(0f, -1f) { m1, m2 ->
            assertEquals(expected = 712, actual = m1)
            assertEquals(expected = 312, actual = m2)
        }
        //right
        AssettoCorsa.calc(0f, 1f) { m1, m2 ->
            assertEquals(expected = 312, actual = m1)
            assertEquals(expected = 712, actual = m2)
        }
        //rev, left
        AssettoCorsa.calc(-1f, -1f) { m1, m2 ->
            assertEquals(expected = 912, actual = m1)
            assertEquals(expected = 512, actual = m2)
        }
        //forw, right
        AssettoCorsa.calc(1f, 1f) { m1, m2 ->
            assertEquals(expected = 112, actual = m1)
            assertEquals(expected = 512, actual = m2)
        }
        //rev, right
        AssettoCorsa.calc(-1f, 1f) { m1, m2 ->
            assertEquals(expected = 512, actual = m1)
            assertEquals(expected = 912, actual = m2)
        }
        //forw, left
        AssettoCorsa.calc(1f, -1f) { m1, m2 ->
            assertEquals(expected = 512, actual = m1)
            assertEquals(expected = 112, actual = m2)
        }
    }

}