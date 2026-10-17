package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.model.CommissionRate
import com.example.model.NumberUtils

@Composable
fun Card2SalesAndCommission(
    salesInput: String,
    commissionRate: CommissionRate,
    commissionAmount: Double,
    netAmount: Double,
    onSalesChanged: (String) -> Unit,
    onCommissionRateSelected: (CommissionRate) -> Unit,
    onCopyValue: (String, String) -> Unit, // (label, formattedValue)
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("card_sales_commission"),
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
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Card Title
            Text(
                text = "ရောင်းငွေ နှင့် ကော်၊ အပ်ငွေ",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold
            )

            // Number field "ရောင်းငွေ (ကျပ်)" with live thousands separators
            NumberInputField(
                value = salesInput,
                onValueChange = onSalesChanged,
                label = "ရောင်းငွေ (ကျပ်)",
                placeholder = "၀",
                suffixText = "ကျပ်",
                testTag = "input_sales"
            )

            // Commission rate selector: 15%, 17%, 25% (default), 30%
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Text(
                    text = "ကော်မရှင် ရာခိုင်နှုန်း (%):",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    CommissionRate.entries.forEach { rate ->
                        val isSelected = rate == commissionRate
                        FilterChip(
                            selected = isSelected,
                            onClick = { onCommissionRateSelected(rate) },
                            label = {
                                Text(
                                    text = rate.label,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                                )
                            },
                            modifier = Modifier
                                .weight(1f)
                                .heightIn(min = 48.dp)
                                .testTag("chip_rate_${rate.label}"),
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                                selectedLabelColor = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        )
                    }
                }
            }

            // Results: ကော် and အပ်ငွေ
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                    .padding(12.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // ကော် (Commission)
                val formattedCommission = NumberUtils.formatDoubleAmount(commissionAmount)
                ResultRowWithCopy(
                    label = "ကော် (${commissionRate.label}):",
                    valueText = "$formattedCommission ကျပ်",
                    onCopy = { onCopyValue("ကော်", formattedCommission) },
                    copyTag = "copy_commission_btn"
                )

                // အပ်ငွေ (Net)
                val formattedNet = NumberUtils.formatDoubleAmount(netAmount)
                ResultRowWithCopy(
                    label = "အပ်ငွေ:",
                    valueText = "$formattedNet ကျပ်",
                    valueColor = MaterialTheme.colorScheme.primary,
                    isBold = true,
                    onCopy = { onCopyValue("အပ်ငွေ", formattedNet) },
                    copyTag = "copy_net_btn"
                )
            }
        }
    }
}

@Composable
fun ResultRowWithCopy(
    label: String,
    valueText: String,
    onCopy: () -> Unit,
    modifier: Modifier = Modifier,
    copyTag: String = "copy_btn",
    isBold: Boolean = false,
    valueColor: androidx.compose.ui.graphics.Color = MaterialTheme.colorScheme.onSurface
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.End
        ) {
            Text(
                text = valueText,
                style = if (isBold) MaterialTheme.typography.titleMedium else MaterialTheme.typography.bodyLarge,
                fontWeight = if (isBold) FontWeight.Bold else FontWeight.Medium,
                color = valueColor
            )
            Spacer(modifier = Modifier.width(4.dp))
            IconButton(
                onClick = onCopy,
                modifier = Modifier
                    .size(48.dp)
                    .testTag(copyTag)
            ) {
                Icon(
                    imageVector = Icons.Default.ContentCopy,
                    contentDescription = "$label ကူးယူရန်",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(18.dp)
                )
            }
        }
    }
}
