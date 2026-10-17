package com.example.viewmodel

import android.content.Context
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.data.AccountingRecord
import com.example.data.AccountingRepository
import com.example.data.AppDatabase
import com.example.model.CommissionRate
import com.example.model.NumberUtils
import com.example.model.SessionPeriod
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class AccountingViewModel(
    private val savedStateHandle: SavedStateHandle,
    private val repository: AccountingRepository? = null
) : ViewModel() {

    companion object {
        private const val KEY_DATE = "accounting_date"
        private const val KEY_SESSION_PERIOD = "accounting_session_period"
        private const val KEY_ROUND = "accounting_round"
        private const val KEY_NAME = "accounting_name"
        private const val KEY_SALES = "accounting_sales"
        private const val KEY_COMMISSION_RATE = "accounting_commission_rate"
        private const val KEY_DEBT = "accounting_debt"
        private const val KEY_LUMP = "accounting_lump"
        private const val KEY_DAE_AMOUNT = "accounting_dae_amount"
        private const val KEY_DAE_MULTIPLIER = "accounting_dae_multiplier"
        private const val KEY_THWAD_AMOUNT = "accounting_thwad_amount"
        private const val KEY_APA_AMOUNT = "accounting_apa_amount"
        private const val KEY_APA_MULTIPLIER = "accounting_apa_multiplier"
        private const val KEY_UNLOCKED = "accounting_is_unlocked"

        fun getTodayDateFormatted(): String {
            val sdf = SimpleDateFormat("dd.MM.yy", Locale.US)
            return sdf.format(Date())
        }
    }

    class Factory(
        private val context: Context
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
            val savedStateHandle = extras.createSavedStateHandle()
            val database = AppDatabase.getDatabase(context)
            val repo = AccountingRepository(database.accountingDao())
            return AccountingViewModel(savedStateHandle, repo) as T
        }
    }

    // Room Database Flow
    val savedRecords: StateFlow<List<AccountingRecord>> = repository?.allRecords?.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    ) ?: MutableStateFlow(emptyList())

    // State flows with SavedStateHandle retention
    val dateText: StateFlow<String> = savedStateHandle.getStateFlow(KEY_DATE, getTodayDateFormatted())
    val sessionPeriod: StateFlow<String> = savedStateHandle.getStateFlow(KEY_SESSION_PERIOD, SessionPeriod.NONE.name)
    val roundSelection: StateFlow<String> = savedStateHandle.getStateFlow(KEY_ROUND, "")
    val nameText: StateFlow<String> = savedStateHandle.getStateFlow(KEY_NAME, "")

    // Card 2
    val salesInput: StateFlow<String> = savedStateHandle.getStateFlow(KEY_SALES, "")
    val commissionRate: StateFlow<String> = savedStateHandle.getStateFlow(KEY_COMMISSION_RATE, CommissionRate.RATE_25.name)

    // Card 3
    val debtInput: StateFlow<String> = savedStateHandle.getStateFlow(KEY_DEBT, "")
    val lumpInput: StateFlow<String> = savedStateHandle.getStateFlow(KEY_LUMP, "")

    // Card 4
    val daeInput: StateFlow<String> = savedStateHandle.getStateFlow(KEY_DAE_AMOUNT, "")
    val daeMultiplier: StateFlow<Int> = savedStateHandle.getStateFlow(KEY_DAE_MULTIPLIER, 550)
    val thwadInput: StateFlow<String> = savedStateHandle.getStateFlow(KEY_THWAD_AMOUNT, "")
    val apaInput: StateFlow<String> = savedStateHandle.getStateFlow(KEY_APA_AMOUNT, "")
    val apaMultiplier: StateFlow<Int> = savedStateHandle.getStateFlow(KEY_APA_MULTIPLIER, 15)
    val isUnlocked: StateFlow<Boolean> = savedStateHandle.getStateFlow(KEY_UNLOCKED, false)

    fun setUnlocked(unlocked: Boolean) {
        savedStateHandle[KEY_UNLOCKED] = unlocked
    }

    // Setters
    fun setDate(date: String) {
        savedStateHandle[KEY_DATE] = date
    }

    fun setDateFromMillis(millis: Long) {
        val sdf = SimpleDateFormat("dd.MM.yy", Locale.US)
        savedStateHandle[KEY_DATE] = sdf.format(Date(millis))
    }

    fun setSessionPeriod(period: SessionPeriod) {
        val current = sessionPeriod.value
        if (current == period.name) {
            savedStateHandle[KEY_SESSION_PERIOD] = SessionPeriod.NONE.name
        } else {
            savedStateHandle[KEY_SESSION_PERIOD] = period.name
        }
    }

    fun setRoundSelection(round: String) {
        savedStateHandle[KEY_ROUND] = round
    }

    fun setName(name: String) {
        savedStateHandle[KEY_NAME] = name
    }

    fun setSales(rawText: String) {
        val formatted = formatNumberInput(rawText)
        savedStateHandle[KEY_SALES] = formatted
    }

    fun setCommissionRate(rate: CommissionRate) {
        savedStateHandle[KEY_COMMISSION_RATE] = rate.name
    }

    fun setDebt(rawText: String) {
        val formatted = formatNumberInput(rawText)
        savedStateHandle[KEY_DEBT] = formatted
    }

    fun setLump(rawText: String) {
        val formatted = formatNumberInput(rawText)
        savedStateHandle[KEY_LUMP] = formatted
    }

    fun setDaeAmount(rawText: String) {
        val formatted = formatNumberInput(rawText)
        savedStateHandle[KEY_DAE_AMOUNT] = formatted
    }

    fun setDaeMultiplier(mult: Int) {
        savedStateHandle[KEY_DAE_MULTIPLIER] = mult
    }

    fun setThwadAmount(rawText: String) {
        val formatted = formatNumberInput(rawText)
        savedStateHandle[KEY_THWAD_AMOUNT] = formatted
    }

    fun setApaAmount(rawText: String) {
        val formatted = formatNumberInput(rawText)
        savedStateHandle[KEY_APA_AMOUNT] = formatted
    }

    fun setApaMultiplier(mult: Int) {
        savedStateHandle[KEY_APA_MULTIPLIER] = mult
    }

    fun resetAll() {
        savedStateHandle[KEY_DATE] = getTodayDateFormatted()
        savedStateHandle[KEY_SESSION_PERIOD] = SessionPeriod.NONE.name
        savedStateHandle[KEY_ROUND] = ""
        savedStateHandle[KEY_NAME] = ""
        savedStateHandle[KEY_SALES] = ""
        savedStateHandle[KEY_COMMISSION_RATE] = CommissionRate.RATE_25.name
        savedStateHandle[KEY_DEBT] = ""
        savedStateHandle[KEY_LUMP] = ""
        savedStateHandle[KEY_DAE_AMOUNT] = ""
        savedStateHandle[KEY_DAE_MULTIPLIER] = 550
        savedStateHandle[KEY_THWAD_AMOUNT] = ""
        savedStateHandle[KEY_APA_AMOUNT] = ""
        savedStateHandle[KEY_APA_MULTIPLIER] = 15
    }

    /**
     * Filter only digits and format with comma live thousands separators
     */
    private fun formatNumberInput(raw: String): String {
        val digitsOnly = raw.filter { it.isDigit() }
        if (digitsOnly.isEmpty()) return ""
        val num = digitsOnly.toLongOrNull() ?: return digitsOnly
        return NumberUtils.formatAmount(num)
    }

    // --- LIVE CALCULATIONS ---

    fun getSales(): Long = NumberUtils.parseAmount(salesInput.value)

    fun getCommissionRateEnum(): CommissionRate {
        return try {
            CommissionRate.valueOf(commissionRate.value)
        } catch (_: Exception) {
            CommissionRate.RATE_25
        }
    }

    // commission = sales × rate
    fun getCommission(): Double {
        return getSales() * getCommissionRateEnum().percentage
    }

    // net (အပ်ငွေ) = sales − commission
    fun getNet(): Double {
        return getSales() - getCommission()
    }

    fun getDebt(): Long = NumberUtils.parseAmount(debtInput.value)

    fun getLump(): Long = NumberUtils.parseAmount(lumpInput.value)

    // all (စုစုပေါင်း အပ်ငွေ) = net + debt + lump
    fun getAll(): Double {
        return getNet() + getDebt() + getLump()
    }

    // dae = daeAmount × daeMultiplier
    fun getDaeAmount(): Long = NumberUtils.parseAmount(daeInput.value)
    fun getDae(): Long = getDaeAmount() * daeMultiplier.value

    // thwad = thwadAmount × 10
    fun getThwadAmount(): Long = NumberUtils.parseAmount(thwadInput.value)
    fun getThwad(): Long = getThwadAmount() * 10L

    // apa = apaAmount × apaMultiplier
    fun getApaAmount(): Long = NumberUtils.parseAmount(apaInput.value)
    fun getApa(): Long = getApaAmount() * apaMultiplier.value.toLong()

    // pay (အလျော်) = dae + thwad + apa
    fun getPay(): Long {
        return getDae() + getThwad() + getApa()
    }

    // diff = |all − pay|
    fun getDiff(): Double {
        return kotlin.math.abs(getAll() - getPay().toDouble())
    }

    // sign = "-" if all > pay, "+" if all < pay
    fun getSign(): String {
        val all = getAll()
        val pay = getPay().toDouble()
        return when {
            all > pay -> "-"
            all < pay -> "+"
            else -> ""
        }
    }

    // --- ROOM DATABASE OPERATIONS ---

    fun saveCurrentCalculation(onCompleted: (Long) -> Unit) {
        viewModelScope.launch {
            val record = AccountingRecord(
                dateText = dateText.value,
                sessionPeriod = sessionPeriod.value,
                roundSelection = roundSelection.value,
                nameText = nameText.value,
                salesAmount = getSales(),
                commissionRatePercent = getCommissionRateEnum().percentage,
                commissionAmount = getCommission(),
                netAmount = getNet(),
                debtAmount = getDebt(),
                lumpAmount = getLump(),
                allDepositAmount = getAll(),
                daeAmount = getDaeAmount(),
                daeMultiplier = daeMultiplier.value,
                daeResult = getDae(),
                thwadAmount = getThwadAmount(),
                thwadResult = getThwad(),
                apaAmount = getApaAmount(),
                apaMultiplier = apaMultiplier.value,
                apaResult = getApa(),
                totalPayResult = getPay(),
                finalSign = getSign(),
                finalDiff = getDiff()
            )
            val newId = repository?.saveRecord(record) ?: 0L
            onCompleted(newId)
        }
    }

    fun deleteSavedRecord(id: Long) {
        viewModelScope.launch {
            repository?.deleteRecord(id)
        }
    }

    fun clearAllSavedRecords() {
        viewModelScope.launch {
            repository?.clearAll()
        }
    }

    fun loadRecordIntoCurrent(record: AccountingRecord) {
        savedStateHandle[KEY_DATE] = record.dateText
        savedStateHandle[KEY_SESSION_PERIOD] = record.sessionPeriod
        savedStateHandle[KEY_ROUND] = record.roundSelection
        savedStateHandle[KEY_NAME] = record.nameText

        savedStateHandle[KEY_SALES] = if (record.salesAmount > 0) NumberUtils.formatAmount(record.salesAmount) else ""
        val rateEnum = CommissionRate.entries.firstOrNull { it.percentage == record.commissionRatePercent } ?: CommissionRate.RATE_25
        savedStateHandle[KEY_COMMISSION_RATE] = rateEnum.name

        savedStateHandle[KEY_DEBT] = if (record.debtAmount > 0) NumberUtils.formatAmount(record.debtAmount) else ""
        savedStateHandle[KEY_LUMP] = if (record.lumpAmount > 0) NumberUtils.formatAmount(record.lumpAmount) else ""

        savedStateHandle[KEY_DAE_AMOUNT] = if (record.daeAmount > 0) NumberUtils.formatAmount(record.daeAmount) else ""
        savedStateHandle[KEY_DAE_MULTIPLIER] = record.daeMultiplier

        savedStateHandle[KEY_THWAD_AMOUNT] = if (record.thwadAmount > 0) NumberUtils.formatAmount(record.thwadAmount) else ""

        savedStateHandle[KEY_APA_AMOUNT] = if (record.apaAmount > 0) NumberUtils.formatAmount(record.apaAmount) else ""
        savedStateHandle[KEY_APA_MULTIPLIER] = record.apaMultiplier
    }

    /**
     * Generate full Burmese summary slip for Copy All
     */
    fun buildFullSummary(): String {
        val period = when (sessionPeriod.value) {
            SessionPeriod.AM.name -> " (AM)"
            SessionPeriod.PM.name -> " (PM)"
            else -> ""
        }
        val roundStr = roundSelection.value.ifEmpty { "-" }
        val nameStr = nameText.value.ifEmpty { "-" }

        val rateLabel = getCommissionRateEnum().label
        val salesStr = NumberUtils.formatAmount(getSales())
        val commStr = NumberUtils.formatDoubleAmount(getCommission())
        val netStr = NumberUtils.formatDoubleAmount(getNet())

        val debtStr = NumberUtils.formatAmount(getDebt())
        val lumpStr = NumberUtils.formatAmount(getLump())
        val allStr = NumberUtils.formatDoubleAmount(getAll())

        val daeAmt = NumberUtils.formatAmount(getDaeAmount())
        val daeTotal = NumberUtils.formatAmount(getDae())
        val thwadAmt = NumberUtils.formatAmount(getThwadAmount())
        val thwadTotal = NumberUtils.formatAmount(getThwad())
        val apaAmt = NumberUtils.formatAmount(getApaAmount())
        val apaTotal = NumberUtils.formatAmount(getApa())
        val payTotal = NumberUtils.formatAmount(getPay())

        val sign = getSign()
        val diffStr = NumberUtils.formatDoubleAmount(getDiff())
        val finalStr = if (sign.isNotEmpty()) "$sign $diffStr" else diffStr

        return buildString {
            appendLine("ငွေစာရင်းတွက်ချက်မှု")
            appendLine("====================")
            appendLine("ရက်စွဲ: ${dateText.value}$period")
            appendLine("အကြိမ်: $roundStr")
            appendLine("အမည်: $nameStr")
            appendLine("--------------------")
            appendLine("၁။ ရောင်းငွေနှင့် အပ်ငွေ:")
            appendLine("  ရောင်းငွေ: $salesStr ကျပ်")
            appendLine("  ကော် ($rateLabel): $commStr ကျပ်")
            appendLine("  အပ်ငွေ: $netStr ကျပ်")
            appendLine("--------------------")
            appendLine("၂။ ပေါင်းထည့်ငွေများ:")
            appendLine("  အကြွေးကျန်: $debtStr ကျပ်")
            appendLine("  အလုံး: $lumpStr ကျပ်")
            appendLine("  စုစုပေါင်း အပ်ငွေ: $allStr ကျပ်")
            appendLine("--------------------")
            appendLine("၃။ အလျော်:")
            appendLine("  ဒဲ့ ($daeAmt × ${daeMultiplier.value}): $daeTotal ကျပ်")
            appendLine("  သွဒ် ($thwadAmt × 10): $thwadTotal ကျပ်")
            appendLine("  အပါ ($apaAmt × ${apaMultiplier.value}): $apaTotal ကျပ်")
            appendLine("  စုစုပေါင်း အလျော်: $payTotal ကျပ်")
            appendLine("====================")
            appendLine("စုစုပေါင်း: $finalStr ကျပ်")
            appendLine("ShweKhinMyay")
        }
    }

    fun buildRecordSummary(record: AccountingRecord): String {
        val period = if (record.sessionPeriod.isNotEmpty() && record.sessionPeriod != SessionPeriod.NONE.name) {
            " (${record.sessionPeriod})"
        } else ""
        val rateLabel = CommissionRate.entries.firstOrNull { it.percentage == record.commissionRatePercent }?.label ?: "25%"
        val finalStr = if (record.finalSign.isNotEmpty()) "${record.finalSign} ${NumberUtils.formatDoubleAmount(record.finalDiff)}" else NumberUtils.formatDoubleAmount(record.finalDiff)

        return buildString {
            appendLine("ငွေစာရင်းမှတ်တမ်း")
            appendLine("====================")
            appendLine("ရက်စွဲ: ${record.dateText}$period")
            appendLine("အကြိမ်: ${record.roundSelection.ifEmpty { "-" }}")
            appendLine("အမည်: ${record.nameText.ifEmpty { "-" }}")
            appendLine("--------------------")
            appendLine("ရောင်းငွေ: ${NumberUtils.formatAmount(record.salesAmount)} ကျပ်")
            appendLine("ကော် ($rateLabel): ${NumberUtils.formatDoubleAmount(record.commissionAmount)} ကျပ်")
            appendLine("အပ်ငွေ: ${NumberUtils.formatDoubleAmount(record.netAmount)} ကျပ်")
            appendLine("စုစုပေါင်း အပ်ငွေ: ${NumberUtils.formatDoubleAmount(record.allDepositAmount)} ကျပ်")
            appendLine("စုစုပေါင်း အလျော်: ${NumberUtils.formatAmount(record.totalPayResult)} ကျပ်")
            appendLine("====================")
            appendLine("စုစုပေါင်း ရှင်းတမ်း: $finalStr ကျပ်")
            appendLine("ShweKhinMyay")
        }
    }
}
