package com.example.model

import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale

/**
 * Commission rates supported: 15%, 17%, 25% (default), 30%
 */
enum class CommissionRate(val percentage: Double, val label: String) {
    RATE_15(0.15, "15%"),
    RATE_17(0.17, "17%"),
    RATE_25(0.25, "25%"),
    RATE_30(0.30, "30%");

    companion object {
        fun fromPercentage(pct: Double): CommissionRate {
            return entries.firstOrNull { it.percentage == pct } ?: RATE_25
        }
    }
}

/**
 * Period of the session (AM / PM)
 */
enum class SessionPeriod(val label: String) {
    NONE(""),
    AM("AM"),
    PM("PM")
}

/**
 * Available round items: ၁ ကြိမ် to ၂၄ ကြိမ်, followed by 2D
 */
object RoundOptions {
    val burmeseDigits = listOf("၀", "၁", "၂", "၃", "၄", "၅", "၆", "၇", "၈", "၉")

    fun toBurmeseNumber(n: Int): String {
        return n.toString().map { ch ->
            if (ch in '0'..'9') burmeseDigits[ch - '0'] else ch.toString()
        }.joinToString("")
    }

    val options: List<String> = buildList {
        for (i in 1..24) {
            add("${toBurmeseNumber(i)} ကြိမ်")
        }
        add("2D")
    }
}

/**
 * Multiplier options for Card 4
 */
object MultiplierOptions {
    val daeMultipliers = listOf(550, 600, 80, 75)
    val apaMultipliers = listOf(3, 8, 15)
}

/**
 * Helper utility for number formatting with thousands separators
 */
object NumberUtils {
    private val formatter = DecimalFormat("#,##0", DecimalFormatSymbols(Locale.US))

    fun formatAmount(amount: Long): String {
        return formatter.format(amount)
    }

    fun formatDoubleAmount(amount: Double): String {
        return if (amount % 1.0 == 0.0) {
            formatter.format(amount.toLong())
        } else {
            val decFormat = DecimalFormat("#,##0.##", DecimalFormatSymbols(Locale.US))
            decFormat.format(amount)
        }
    }

    fun parseAmount(input: String): Long {
        val clean = input.replace(",", "").replace(" ", "").trim()
        return clean.toLongOrNull() ?: 0L
    }
}
