package com.example

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.example.data.AccountingRecord
import com.example.data.AppDatabase
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class ExampleRobolectricTest {

    private lateinit var db: AppDatabase

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
            .allowMainThreadQueries()
            .build()
    }

    @After
    fun closeDb() {
        db.close()
    }

    @Test
    fun `read string from context`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val appName = context.getString(R.string.app_name)
        assertEquals("ငွေစာရင်းတွက်ချက်မှု", appName)
    }

    @Test
    fun `test room insert and read records`() = runBlocking {
        val dao = db.accountingDao()
        val record = AccountingRecord(
            dateText = "17.10.26",
            sessionPeriod = "AM",
            roundSelection = "၁ ကြိမ်",
            nameText = "ဦးမောင်",
            salesAmount = 100000L,
            commissionRatePercent = 0.25,
            commissionAmount = 25000.0,
            netAmount = 75000.0,
            debtAmount = 5000L,
            lumpAmount = 2000L,
            allDepositAmount = 82000.0,
            daeAmount = 100L,
            daeMultiplier = 550,
            daeResult = 55000L,
            thwadAmount = 500L,
            thwadResult = 5000L,
            apaAmount = 1000L,
            apaMultiplier = 15,
            apaResult = 15000L,
            totalPayResult = 75000L,
            finalSign = "-",
            finalDiff = 7000.0
        )

        val id = dao.insertRecord(record)
        val records = dao.getAllRecords().first()
        assertEquals(1, records.size)
        assertEquals("17.10.26", records[0].dateText)
        assertEquals("ဦးမောင်", records[0].nameText)
        assertEquals(100000L, records[0].salesAmount)
        assertEquals("-", records[0].finalSign)
        assertEquals(7000.0, records[0].finalDiff, 0.001)

        dao.deleteRecordById(id)
        val emptyRecords = dao.getAllRecords().first()
        assertEquals(0, emptyRecords.size)
    }
}
