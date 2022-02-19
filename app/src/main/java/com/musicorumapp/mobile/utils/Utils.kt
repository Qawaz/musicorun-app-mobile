package com.musicorumapp.mobile.utils

import java.math.BigInteger
import java.security.MessageDigest
import kotlin.math.max
import kotlin.math.min


class Utils {
    companion object {
        fun md5Hash(string: String): String {
            val md = MessageDigest.getInstance("MD5")
            return BigInteger(1, md.digest(string.toByteArray())).toString(16).padStart(32, '0')
        }

        fun anyToInt(x: Any): Int {
            return when (x) {
                is Int -> x
                is String -> x.toInt()
                is Double -> x.toInt()
                else -> 0
            }
        }

        fun interpolateValues(value: Float, x1: Float, x2: Float, y1: Float, y2: Float): Float {
            val fraction = (value - x1) / (x2 - x1)
            return max(
                min(
                    fraction * (y2 - y1) + y1,
                    y2
                ),
                y1
            )
        }
    }
}