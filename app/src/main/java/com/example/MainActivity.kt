package com.example

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.components.BiometricLockScreen
import com.example.ui.screens.AccountingScreen
import com.example.ui.screens.HistoryScreen
import com.example.ui.theme.MyApplicationTheme
import com.example.viewmodel.AccountingViewModel

enum class AppScreen {
    CALCULATOR,
    HISTORY
}

class MainActivity : FragmentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    val context = LocalContext.current
                    val viewModel: AccountingViewModel = viewModel(
                        factory = AccountingViewModel.Factory(context)
                    )
                    val isUnlocked by viewModel.isUnlocked.collectAsState()
                    var currentScreen by rememberSaveable { mutableStateOf(AppScreen.CALCULATOR) }

                    if (isUnlocked) {
                        when (currentScreen) {
                            AppScreen.CALCULATOR -> {
                                AccountingScreen(
                                    viewModel = viewModel,
                                    onLockClicked = { viewModel.setUnlocked(false) },
                                    onNavigateToHistory = { currentScreen = AppScreen.HISTORY }
                                )
                            }
                            AppScreen.HISTORY -> {
                                HistoryScreen(
                                    viewModel = viewModel,
                                    onBackToCalculator = { currentScreen = AppScreen.CALCULATOR }
                                )
                            }
                        }
                    } else {
                        BiometricLockScreen(
                            onUnlocked = { viewModel.setUnlocked(true) }
                        )
                    }
                }
            }
        }
    }
}
