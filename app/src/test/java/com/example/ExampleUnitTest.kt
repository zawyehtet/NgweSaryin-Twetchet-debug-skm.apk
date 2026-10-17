package com.example

import androidx.lifecycle.SavedStateHandle
import com.example.model.CommissionRate
import com.example.model.NumberUtils
import com.example.viewmodel.AccountingViewModel
import org.junit.Assert.assertEquals
import org.junit.Test

class ExampleUnitTest {

    @Test
    fun testLiveCalculations() {
        val savedStateHandle = SavedStateHandle()
        val vm = AccountingViewModel(savedStateHandle)

        // Sales: 100,000, Commission: 25%
        vm.setSales("100000")
        vm.setCommissionRate(CommissionRate.RATE_25)
        assertEquals(100000L, vm.getSales())
        assertEquals(25000.0, vm.getCommission(), 0.001)
        assertEquals(75000.0, vm.getNet(), 0.001)

        // Additional deposits: Debt 5,000, Lump 2,000
        vm.setDebt("5000")
        vm.setLump("2000")
        assertEquals(5000L, vm.getDebt())
        assertEquals(2000L, vm.getLump())
        // all = net + debt + lump = 75000 + 5000 + 2000 = 82000
        assertEquals(82000.0, vm.getAll(), 0.001)

        // Payouts:
        // Dae: 100 * 550 = 55,000
        vm.setDaeAmount("100")
        vm.setDaeMultiplier(550)
        assertEquals(55000L, vm.getDae())

        // Thwad: 500 * 10 = 5,000
        vm.setThwadAmount("500")
        assertEquals(5000L, vm.getThwad())

        // Apa: 1000 * 15 = 15,000
        vm.setApaAmount("1000")
        vm.setApaMultiplier(15)
        assertEquals(15000L, vm.getApa())

        // pay = 55000 + 5000 + 15000 = 75000
        assertEquals(75000L, vm.getPay())

        // all (82000) > pay (75000) => diff = 7000, sign = "-"
        assertEquals(7000.0, vm.getDiff(), 0.001)
        assertEquals("-", vm.getSign())

        // Test reverse scenario: all < pay => sign = "+"
        vm.setDaeAmount("200") // 200 * 550 = 110,000 => pay = 130,000
        // all (82000) < pay (130000) => diff = 48000, sign = "+"
        assertEquals(48000.0, vm.getDiff(), 0.001)
        assertEquals("+", vm.getSign())

        // Test lock / unlock state
        assertEquals(false, vm.isUnlocked.value)
        vm.setUnlocked(true)
        assertEquals(true, vm.isUnlocked.value)
        vm.setUnlocked(false)
        assertEquals(false, vm.isUnlocked.value)
    }

    @Test
    fun testNumberFormatting() {
        assertEquals("1,000,000", NumberUtils.formatAmount(1000000L))
        assertEquals("500", NumberUtils.formatAmount(500L))
        assertEquals(1000000L, NumberUtils.parseAmount("1,000,000"))
        assertEquals(500L, NumberUtils.parseAmount(" 500 "))
    }
}
