package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "accounting_records")
data class AccountingRecord(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val timestamp: Long = System.currentTimeMillis(),
    val dateText: String,
    val sessionPeriod: String,
    val roundSelection: String,
    val nameText: String,
    // Sales & Commission
    val salesAmount: Long,
    val commissionRatePercent: Double,
    val commissionAmount: Double,
    val netAmount: Double,
    // Deposits
    val debtAmount: Long,
    val lumpAmount: Long,
    val allDepositAmount: Double,
    // Payouts
    val daeAmount: Long,
    val daeMultiplier: Int,
    val daeResult: Long,
    val thwadAmount: Long,
    val thwadResult: Long,
    val apaAmount: Long,
    val apaMultiplier: Int,
    val apaResult: Long,
    val totalPayResult: Long,
    // Final
    val finalSign: String,
    val finalDiff: Double
)
