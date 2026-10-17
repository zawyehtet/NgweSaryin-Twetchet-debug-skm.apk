package com.example.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.R

// Custom FontFamily from bundled Noto Sans Myanmar font resource
val NotoSansMyanmarFont = FontFamily(
    Font(R.font.noto_sans_myanmar, FontWeight.Normal),
    Font(R.font.noto_sans_myanmar, FontWeight.Medium),
    Font(R.font.noto_sans_myanmar, FontWeight.SemiBold),
    Font(R.font.noto_sans_myanmar, FontWeight.Bold)
)

// App-wide Typography with generous line height (~1.9 multiplier) to prevent Burmese diacritics clipping
val Typography = Typography(
    displayLarge = TextStyle(
        fontFamily = NotoSansMyanmarFont,
        fontWeight = FontWeight.Bold,
        fontSize = 34.sp,
        lineHeight = 62.sp,
        letterSpacing = 0.sp
    ),
    displayMedium = TextStyle(
        fontFamily = NotoSansMyanmarFont,
        fontWeight = FontWeight.Bold,
        fontSize = 28.sp,
        lineHeight = 52.sp,
        letterSpacing = 0.sp
    ),
    displaySmall = TextStyle(
        fontFamily = NotoSansMyanmarFont,
        fontWeight = FontWeight.SemiBold,
        fontSize = 24.sp,
        lineHeight = 44.sp,
        letterSpacing = 0.sp
    ),
    headlineLarge = TextStyle(
        fontFamily = NotoSansMyanmarFont,
        fontWeight = FontWeight.SemiBold,
        fontSize = 22.sp,
        lineHeight = 40.sp,
        letterSpacing = 0.sp
    ),
    headlineMedium = TextStyle(
        fontFamily = NotoSansMyanmarFont,
        fontWeight = FontWeight.SemiBold,
        fontSize = 20.sp,
        lineHeight = 38.sp,
        letterSpacing = 0.sp
    ),
    headlineSmall = TextStyle(
        fontFamily = NotoSansMyanmarFont,
        fontWeight = FontWeight.Medium,
        fontSize = 18.sp,
        lineHeight = 34.sp,
        letterSpacing = 0.sp
    ),
    titleLarge = TextStyle(
        fontFamily = NotoSansMyanmarFont,
        fontWeight = FontWeight.SemiBold,
        fontSize = 18.sp,
        lineHeight = 34.sp,
        letterSpacing = 0.sp
    ),
    titleMedium = TextStyle(
        fontFamily = NotoSansMyanmarFont,
        fontWeight = FontWeight.Medium,
        fontSize = 16.sp,
        lineHeight = 30.sp,
        letterSpacing = 0.15.sp
    ),
    titleSmall = TextStyle(
        fontFamily = NotoSansMyanmarFont,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        lineHeight = 26.sp,
        letterSpacing = 0.1.sp
    ),
    bodyLarge = TextStyle(
        fontFamily = NotoSansMyanmarFont,
        fontWeight = FontWeight.Normal,
        fontSize = 15.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.25.sp
    ),
    bodyMedium = TextStyle(
        fontFamily = NotoSansMyanmarFont,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 26.sp,
        letterSpacing = 0.25.sp
    ),
    bodySmall = TextStyle(
        fontFamily = NotoSansMyanmarFont,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp,
        lineHeight = 22.sp,
        letterSpacing = 0.4.sp
    ),
    labelLarge = TextStyle(
        fontFamily = NotoSansMyanmarFont,
        fontWeight = FontWeight.SemiBold,
        fontSize = 14.sp,
        lineHeight = 26.sp,
        letterSpacing = 0.1.sp
    ),
    labelMedium = TextStyle(
        fontFamily = NotoSansMyanmarFont,
        fontWeight = FontWeight.Medium,
        fontSize = 12.sp,
        lineHeight = 22.sp,
        letterSpacing = 0.5.sp
    ),
    labelSmall = TextStyle(
        fontFamily = NotoSansMyanmarFont,
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.5.sp
    )
)
