package com.db.fetchfacts.utils

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.math.BigInteger

class Base62Test {
    @Test
    fun `given number when called encode then should convert a number to a Base62 string`() {
        val number: Long = 12345
        val encoded = Base62.encode(number)
        assertEquals("3D7", encoded)
    }

    @Test
    fun `given base62 string when called decode then should convert a Base62 string back to a number`() {
        val base62 = "3D7"
        val decoded = Base62.decode(base62)
        assertEquals(12345, decoded)
    }

    @Test
    fun `given number when called encode and decode then should be reversible`() {
        val number: Long = 987654321
        val encoded = Base62.encode(number)
        val decoded = Base62.decode(encoded)
        assertEquals(number, decoded)
    }

    @Test
    fun `given invalid string when called decode then should throw exception for invalid Base62 string`() {
        val invalidBase62 = "3D@7"
        val exception = assertThrows(IllegalArgumentException::class.java) {
            Base62.decode(invalidBase62)
        }
        assertEquals("Invalid character in Base62 string", exception.message)
    }

    @Test
    fun `given hexString when called stringToHashCode then should convert a hex string to a hash code`() {
        val hexString = "1a2b3c"
        val expectedHashCode = BigInteger(hexString, 16).mod(BigInteger.valueOf(Long.MAX_VALUE)).toLong()
        val hashCode = Base62.stringToHashCode(hexString)
        assertEquals(expectedHashCode, hashCode)
    }

    @Test
    fun `given hashCode when called hashCodeToString then should convert a hash code back to a hex string`() {
        val hashCode = 112233445566L
        val expectedHexString = BigInteger.valueOf(hashCode).toString(16)
        val hexString = Base62.hashCodeToString(hashCode)
        assertEquals(expectedHexString, hexString)
    }

    @Test
    fun `given hashString when called stringToHashCode and hashCodeToString then should be reversible`() {
        val hexString = "deadbeef"
        val hashCode = Base62.stringToHashCode(hexString)
        val reconstructedHexString = Base62.hashCodeToString(hashCode)
        assertEquals(hexString, reconstructedHexString)
    }
}