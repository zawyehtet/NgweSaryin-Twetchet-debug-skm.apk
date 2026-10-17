package com.example.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R

/**
 * Gold logo component: "ShweKhinMyay"
 * Features a small gold diamond icon plus bold serif text with a gold gradient.
 * 
 * To swap with a custom brand drawable:
 * Set [useCustomDrawable] to true, or replace `res/drawable/ic_shwe_khin_myay_logo.xml`
 * with your own PNG / SVG file.
 */
@Composable
fun ShweKhinMyayLogo(
    modifier: Modifier = Modifier,
    useCustomDrawable: Boolean = false
) {
    if (useCustomDrawable) {
        Image(
            painter = painterResource(id = R.drawable.ic_shwe_khin_myay_logo),
            contentDescription = "ShweKhinMyay Brand Logo",
            modifier = modifier.height(32.dp)
        )
    } else {
        val goldGradient = Brush.linearGradient(
            colors = listOf(
                Color(0xFFFFE082),
                Color(0xFFFFD54F),
                Color(0xFFFFB300),
                Color(0xFFD4AF37),
                Color(0xFFAA7C11)
            )
        )

        Row(
            modifier = modifier,
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.End
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_gold_diamond),
                contentDescription = "Gold Diamond",
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = "ShweKhinMyay",
                style = TextStyle(
                    fontFamily = FontFamily.Serif,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    brush = goldGradient,
                    letterSpacing = 0.5.sp
                )
            )
        }
    }
}
