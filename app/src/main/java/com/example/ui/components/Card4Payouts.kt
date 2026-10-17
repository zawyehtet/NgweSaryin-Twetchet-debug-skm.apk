package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.MultiplierOptions
import com.example.model.NumberUtils
import com.example.ui.theme.DiffNegativeColor
import com.example.ui.theme.DiffPositiveColor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Card4Payouts(
    daeInput: String,
    daeMultiplier: Int,
    thwadInput: String,
    apaInput: String,
    apaMultiplier: Int,
    daeResult: Long,
    thwadResult: Long,
    apaResult: Long,
    totalPayResult: Long,
    finalSign: String,
    finalDiff: Double,
    onDaeInputChanged: (String) -> Unit,
    onDaeMultiplierChanged: (Int) -> Unit,
    onThwadInputChanged: (String) -> Unit,
    onApaInputChanged: (String) -> Unit,
    onApaMultiplierChanged: (Int) -> Unit,
    onCopyValue: (String, String) -> Unit,
    modifier: Modifier = Modifier
) {
    var daeDropdownExpanded by remember { mutableStateOf(false) }
    var apaDropdownExpanded by remember { mutableStateOf(false) }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("card_payouts"),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = MaterialTheme.shapes.large
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Card Title
            Text(
                text = "အလျော်",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold
            )

            // 1. ဒဲ့: amount × multiplier dropdown (550, 600, 80, 75)
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Text(
                    text = "ဒဲ့ (Dae):",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontWeight = FontWeight.SemiBold
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    NumberInputField(
                        value = daeInput,
                        onValueChange = onDaeInputChanged,
                        label = "ဒဲ့ ပမာဏ",
                        placeholder = "၀",
                        suffixText = null,
                        modifier = Modifier.weight(1.5f),
                        testTag = "input_dae_amount"
                    )

                    ExposedDropdownMenuBox(
                        expanded = daeDropdownExpanded,
                        onExpandedChange = { daeDropdownExpanded = it },
                        modifier = Modifier
                            .weight(1.1f)
                            .testTag("dae_multiplier_box")
                    ) {
                        OutlinedTextField(
                            value = "× $daeMultiplier",
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("ဆ") },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = daeDropdownExpanded) },
                            modifier = Modifier
                                .menuAnchor(MenuAnchorType.PrimaryNotEditable)
                                .fillMaxWidth()
                                .testTag("dae_multiplier_field"),
                            shape = MaterialTheme.shapes.medium,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = MaterialTheme.colorScheme.primary,
                                unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)
                            )
                        )

                        ExposedDropdownMenu(
                            expanded = daeDropdownExpanded,
                            onDismissRequest = { daeDropdownExpanded = false }
                        ) {
                            MultiplierOptions.daeMultipliers.forEach { mult ->
                                DropdownMenuItem(
                                    text = {
                                        Text(
                                            text = "× $mult",
                                            fontWeight = if (daeMultiplier == mult) FontWeight.Bold else FontWeight.Normal
                                        )
                                    },
                                    onClick = {
                                        onDaeMultiplierChanged(mult)
                                        daeDropdownExpanded = false
                                    },
                                    modifier = Modifier.heightIn(min = 48.dp)
                                )
                            }
                        }
                    }
                }

                // Dae subtotal result
                val formattedDae = NumberUtils.formatAmount(daeResult)
                SubtotalRow(
                    label = "ဒဲ့ ရလဒ်:",
                    valueText = "$formattedDae ကျပ်"
                )
            }

            // 2. သွဒ်: amount × 10
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Text(
                    text = "သွဒ် (Thwad):",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontWeight = FontWeight.SemiBold
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    NumberInputField(
                        value = thwadInput,
                        onValueChange = onThwadInputChanged,
                        label = "သွဒ် ပမာဏ",
                        placeholder = "၀",
                        suffixText = null,
                        modifier = Modifier.weight(1.5f),
                        testTag = "input_thwad_amount"
                    )

                    Surface(
                        modifier = Modifier
                            .weight(1.1f)
                            .height(56.dp),
                        shape = MaterialTheme.shapes.medium,
                        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                        border = androidx.compose.foundation.BorderStroke(
                            1.dp,
                            MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)
                        )
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text(
                                text = "× 10 ဆ",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }

                // Thwad subtotal result
                val formattedThwad = NumberUtils.formatAmount(thwadResult)
                SubtotalRow(
                    label = "သွဒ် ရလဒ်:",
                    valueText = "$formattedThwad ကျပ်"
                )
            }

            // 3. အပါ: amount × multiplier dropdown (3, 8, 15)
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Text(
                    text = "အပါ (Apa):",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontWeight = FontWeight.SemiBold
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    NumberInputField(
                        value = apaInput,
                        onValueChange = onApaInputChanged,
                        label = "အပါ ပမာဏ",
                        placeholder = "၀",
                        suffixText = null,
                        modifier = Modifier.weight(1.5f),
                        testTag = "input_apa_amount"
                    )

                    ExposedDropdownMenuBox(
                        expanded = apaDropdownExpanded,
                        onExpandedChange = { apaDropdownExpanded = it },
                        modifier = Modifier
                            .weight(1.1f)
                            .testTag("apa_multiplier_box")
                    ) {
                        OutlinedTextField(
                            value = "× $apaMultiplier",
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("ဆ") },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = apaDropdownExpanded) },
                            modifier = Modifier
                                .menuAnchor(MenuAnchorType.PrimaryNotEditable)
                                .fillMaxWidth()
                                .testTag("apa_multiplier_field"),
                            shape = MaterialTheme.shapes.medium,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = MaterialTheme.colorScheme.primary,
                                unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)
                            )
                        )

                        ExposedDropdownMenu(
                            expanded = apaDropdownExpanded,
                            onDismissRequest = { apaDropdownExpanded = false }
                        ) {
                            MultiplierOptions.apaMultipliers.forEach { mult ->
                                DropdownMenuItem(
                                    text = {
                                        Text(
                                            text = "× $mult",
                                            fontWeight = if (apaMultiplier == mult) FontWeight.Bold else FontWeight.Normal
                                        )
                                    },
                                    onClick = {
                                        onApaMultiplierChanged(mult)
                                        apaDropdownExpanded = false
                                    },
                                    modifier = Modifier.heightIn(min = 48.dp)
                                )
                            }
                        }
                    }
                }

                // Apa subtotal result
                val formattedApa = NumberUtils.formatAmount(apaResult)
                SubtotalRow(
                    label = "အပါ ရလဒ်:",
                    valueText = "$formattedApa ကျပ်"
                )
            }

            // Total အလျော် (Read-only total with Copy button)
            val formattedTotalPay = NumberUtils.formatAmount(totalPayResult)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.7f))
                    .padding(horizontal = 14.dp, vertical = 10.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "စုစုပေါင်း အလျော်",
                        style = MaterialTheme.typography.titleSmall,
                        color = MaterialTheme.colorScheme.onSurface,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = "(ဒဲ့ + သွဒ် + အပါ)",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "$formattedTotalPay ကျပ်",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    IconButton(
                        onClick = { onCopyValue("စုစုပေါင်း အလျော်", formattedTotalPay) },
                        modifier = Modifier
                            .size(48.dp)
                            .testTag("copy_total_pay_btn")
                    ) {
                        Icon(
                            imageVector = Icons.Default.ContentCopy,
                            contentDescription = "စုစုပေါင်း အလျော် ကူးယူရန်",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }

            // Large Final "စုစုပေါင်း" inside Card 4
            val formattedDiff = NumberUtils.formatDoubleAmount(finalDiff)
            val finalDisplay = if (finalSign.isNotEmpty()) "$finalSign $formattedDiff" else formattedDiff
            val signColor = when (finalSign) {
                "-" -> DiffPositiveColor
                "+" -> DiffNegativeColor
                else -> MaterialTheme.colorScheme.primary
            }

            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("card_final_total_section"),
                shape = RoundedCornerShape(16.dp),
                color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.35f),
                border = androidx.compose.foundation.BorderStroke(
                    1.5.dp,
                    MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = "စုစုပေါင်း ရှင်းတမ်း",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "(စုစုပေါင်း အပ်ငွေ နှင့် အလျော် ကွာဟချက်)",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "$finalDisplay ကျပ်",
                        style = MaterialTheme.typography.displaySmall,
                        color = signColor,
                        fontWeight = FontWeight.ExtraBold
                    )
                }
            }
        }
    }
}

@Composable
fun SubtotalRow(
    label: String,
    valueText: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp, vertical = 2.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = valueText,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}
