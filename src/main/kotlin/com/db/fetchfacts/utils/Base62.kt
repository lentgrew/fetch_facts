package com.db.fetchfacts.utils

import java.math.BigInteger

object Base62 {
    private const val CHARSET = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz"

    fun encode(number: Long): String {
        var num = number
        val encoded = StringBuilder()
        while (num != 0L) {
            val remainder = (num % 62).toInt()
            encoded.append(CHARSET[remainder])
            num /= 62
        }
        return encoded.reverse().toString()
    }

    fun decode(base62: String): Long {
        var decoded: Long = 0
        for (char in base62) {
            val index = CHARSET.indexOf(char)
            if (index == -1) throw IllegalArgumentException("Invalid character in Base62 string")
            decoded = decoded * 62 + index
        }
        return decoded
    }

    fun stringToHashCode(hexString: String): Long {
        val bigInt = BigInteger(hexString, 16)

        // Map to a Long (64 bits) by taking modulo Long.MAX_VALUE
        return bigInt.mod(BigInteger.valueOf(Long.MAX_VALUE)).toLong()
    }

    fun hashCodeToString(hashCode: Long): String {
        // Convert the Long back to a Hexadecimal String
        return BigInteger.valueOf(hashCode).toString(16)
    }
}