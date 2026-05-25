package com.example.smartmoveiiui.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

val Typography = Typography(

    headlineLarge = TextStyle(
        fontSize = 28.sp,
        fontWeight = FontWeight.Bold,
        fontFamily = FontFamily.Serif
    ),

    headlineMedium = TextStyle(
        fontSize = 22.sp,
        fontWeight = FontWeight.SemiBold
    ),

    titleLarge = TextStyle(
        fontSize = 18.sp,
        fontWeight = FontWeight.SemiBold
    ),

    titleMedium = TextStyle(
        fontSize = 16.sp,
        fontWeight = FontWeight.Medium
    ),

    bodyLarge = TextStyle(
        fontSize = 14.sp,
        fontWeight = FontWeight.Normal
    ),

    bodyMedium = TextStyle(
        fontSize = 13.sp,
        fontWeight = FontWeight.Normal
    ),

    labelLarge = TextStyle(
        fontSize = 12.sp,
        fontWeight = FontWeight.SemiBold
    ),

    labelMedium = TextStyle(
        fontSize = 10.sp,
        fontWeight = FontWeight.Medium
    )
)