package com.example.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.model.CommissionRate
import com.example.model.SessionPeriod
import com.example.ui.components.Card1HeaderAndRound
import com.example.ui.components.Card2SalesAndCommission
import com.example.ui.components.Card3AdditionalDeposits
import com.example.ui.components.Card4Payouts
import com.example.ui.components.ShweKhinMyayLogo
import com.example.ui.components.StickyBottomSummary
import com.example.viewmodel.AccountingViewModel
import kotlinx.coroutines.launch
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccountingScreen(
    viewModel: AccountingViewModel,
    modifier: Modifier = Modifier,
    onLockClicked: () -> Unit = {},
    onNavigateToHistory: () -> Unit = {}
) {
    val clipboardManager = LocalClipboardManager.current
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    var showResetDialog by remember { mutableStateOf(false) }

    val savedRecords by viewModel.savedRecords.collectAsState()

    // State collections from ViewModel
    val dateText by viewModel.dateText.collectAsState()
    val sessionPeriod by viewModel.sessionPeriod.collectAsState()
    val roundSelection by viewModel.roundSelection.collectAsState()
    val nameText by viewModel.nameText.collectAsState()

    val salesInput by viewModel.salesInput.collectAsState()
    val commissionRateStr by viewModel.commissionRate.collectAsState()
    val debtInput by viewModel.debtInput.collectAsState()
    val lumpInput by viewModel.lumpInput.collectAsState()

    val daeInput by viewModel.daeInput.collectAsState()
    val daeMultiplier by viewModel.daeMultiplier.collectAsState()
    val thwadInput by viewModel.thwadInput.collectAsState()
    val apaInput by viewModel.apaInput.collectAsState()
    val apaMultiplier by viewModel.apaMultiplier.collectAsState()

    val currentCommissionRate = remember(commissionRateStr) {
        viewModel.getCommissionRateEnum()
    }

    // Calculations
    val commissionAmount = viewModel.getCommission()
    val netAmount = viewModel.getNet()
    val allDepositAmount = viewModel.getAll()
    val daeResult = viewModel.getDae()
    val thwadResult = viewModel.getThwad()
    val apaResult = viewModel.getApa()
    val totalPayResult = viewModel.getPay()
    val finalSign = viewModel.getSign()
    val finalDiff = viewModel.getDiff()

    fun copyToClipboard(label: String, textToCopy: String) {
        clipboardManager.setText(AnnotatedString(textToCopy))
        scope.launch {
            snackbarHostState.currentSnackbarData?.dismiss()
            snackbarHostState.showSnackbar(
                message = "$label ကူးယူပြီးပါပြီ ($textToCopy)",
                duration = SnackbarDuration.Short
            )
        }
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "ငွေစာရင်းတွက်ချက်မှု",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.testTag("app_title")
                        )

                        ShweKhinMyayLogo(
                            modifier = Modifier
                                .padding(end = 4.dp)
                                .testTag("shwe_khin_myay_logo")
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = onNavigateToHistory,
                        modifier = Modifier
                            .size(48.dp)
                            .testTag("history_button")
                    ) {
                        BadgedBox(
                            badge = {
                                if (savedRecords.isNotEmpty()) {
                                    Badge(
                                        containerColor = MaterialTheme.colorScheme.primary,
                                        contentColor = MaterialTheme.colorScheme.onPrimary
                                    ) {
                                        Text("${savedRecords.size}")
                                    }
                                }
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.History,
                                contentDescription = "ယခင်မှတ်တမ်းများ",
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                    IconButton(
                        onClick = onLockClicked,
                        modifier = Modifier
                            .size(48.dp)
                            .testTag("lock_app_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Lock,
                            contentDescription = "သော့ခတ်ရန်",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    IconButton(
                        onClick = { showResetDialog = true },
                        modifier = Modifier
                            .size(48.dp)
                            .testTag("reset_all_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = "အသစ်ပြန်စရန်",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        },
        bottomBar = {
            StickyBottomSummary(
                finalSign = finalSign,
                finalDiff = finalDiff,
                onCopyAllClicked = {
                    val fullSummary = viewModel.buildFullSummary()
                    clipboardManager.setText(AnnotatedString(fullSummary))
                    scope.launch {
                        snackbarHostState.currentSnackbarData?.dismiss()
                        snackbarHostState.showSnackbar(
                            message = "စာရင်းရှင်းတမ်း အပြည့်အစုံ ကူးယူပြီးပါပြီ",
                            duration = SnackbarDuration.Short
                        )
                    }
                },
                onSaveClicked = {
                    viewModel.saveCurrentCalculation { id ->
                        scope.launch {
                            snackbarHostState.currentSnackbarData?.dismiss()
                            snackbarHostState.showSnackbar(
                                message = "ငွေစာရင်း သိမ်းဆည်းပြီးပါပြီ (မှတ်တမ်း #${id})",
                                duration = SnackbarDuration.Short
                            )
                        }
                    }
                }
            )
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentAlignment = Alignment.TopCenter
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .widthIn(max = 640.dp)
                    .testTag("accounting_cards_list"),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 14.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                state = rememberLazyListState()
            ) {
                // CARD 1: ခေါင်းစဉ်နှင့် အကြိမ်
                item(key = "card1") {
                    Card1HeaderAndRound(
                        dateText = dateText,
                        sessionPeriod = sessionPeriod,
                        roundSelection = roundSelection,
                        nameText = nameText,
                        onDateSelected = { viewModel.setDateFromMillis(it) },
                        onSessionPeriodToggle = { viewModel.setSessionPeriod(it) },
                        onRoundSelected = { viewModel.setRoundSelection(it) },
                        onNameChanged = { viewModel.setName(it) }
                    )
                }

                // CARD 2: ရောင်းငွေ နှင့် ကော်၊ အပ်ငွေ
                item(key = "card2") {
                    Card2SalesAndCommission(
                        salesInput = salesInput,
                        commissionRate = currentCommissionRate,
                        commissionAmount = commissionAmount,
                        netAmount = netAmount,
                        onSalesChanged = { viewModel.setSales(it) },
                        onCommissionRateSelected = { viewModel.setCommissionRate(it) },
                        onCopyValue = { label, formattedVal ->
                            copyToClipboard(label, formattedVal)
                        }
                    )
                }

                // CARD 3: ပေါင်းထည့်ငွေများ
                item(key = "card3") {
                    Card3AdditionalDeposits(
                        debtInput = debtInput,
                        lumpInput = lumpInput,
                        allDepositAmount = allDepositAmount,
                        onDebtChanged = { viewModel.setDebt(it) },
                        onLumpChanged = { viewModel.setLump(it) },
                        onCopyValue = { label, formattedVal ->
                            copyToClipboard(label, formattedVal)
                        }
                    )
                }

                // CARD 4: အလျော်
                item(key = "card4") {
                    Card4Payouts(
                        daeInput = daeInput,
                        daeMultiplier = daeMultiplier,
                        thwadInput = thwadInput,
                        apaInput = apaInput,
                        apaMultiplier = apaMultiplier,
                        daeResult = daeResult,
                        thwadResult = thwadResult,
                        apaResult = apaResult,
                        totalPayResult = totalPayResult,
                        finalSign = finalSign,
                        finalDiff = finalDiff,
                        onDaeInputChanged = { viewModel.setDaeAmount(it) },
                        onDaeMultiplierChanged = { viewModel.setDaeMultiplier(it) },
                        onThwadInputChanged = { viewModel.setThwadAmount(it) },
                        onApaInputChanged = { viewModel.setApaAmount(it) },
                        onApaMultiplierChanged = { viewModel.setApaMultiplier(it) },
                        onCopyValue = { label, formattedVal ->
                            copyToClipboard(label, formattedVal)
                        }
                    )
                }

                // Bottom spacer for scroll comfort above sticky bar
                item(key = "bottom_spacer") {
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }

    // Confirmation dialog before resetting all inputs
    if (showResetDialog) {
        AlertDialog(
            onDismissRequest = { showResetDialog = false },
            title = {
                Text(
                    text = "စာရင်းများ အသစ်ပြန်စမည်လား?",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Text(
                    text = "ရိုက်ထည့်ထားသော အချက်အလက်များနှင့် ဂဏန်းများအားလုံး ပယ်ဖျက်ပြီး အသစ်ပြန်စပါမည်။",
                    style = MaterialTheme.typography.bodyMedium
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.resetAll()
                        showResetDialog = false
                        scope.launch {
                            snackbarHostState.showSnackbar("စာရင်းများ ပြန်လည်ရှင်းလင်းပြီးပါပြီ")
                        }
                    },
                    modifier = Modifier.testTag("confirm_reset_button")
                ) {
                    Text(
                        text = "ရှင်းလင်းမည်",
                        color = MaterialTheme.colorScheme.error,
                        fontWeight = FontWeight.Bold
                    )
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showResetDialog = false },
                    modifier = Modifier.testTag("cancel_reset_button")
                ) {
                    Text("မလုပ်တော့ပါ")
                }
            }
        )
    }
}
