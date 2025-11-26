package com.msomu.androidkt.presentation.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = Purple80,
    onPrimary = OnPrimaryContainer,
    primaryContainer = PrimaryContainer,
    onPrimaryContainer = Purple80,
    secondary = PurpleGrey80,
    onSecondary = OnSecondaryContainer,
    secondaryContainer = SecondaryContainer,
    onSecondaryContainer = PurpleGrey80,
    tertiary = Pink80,
    onTertiary = OnTertiaryContainer,
    tertiaryContainer = TertiaryContainer,
    onTertiaryContainer = Pink80,
    error = Error,
    errorContainer = ErrorContainer,
    surface = SurfaceDark,
    onSurface = androidx.compose.ui.graphics.Color.White,
    surfaceVariant = SurfaceVariantDark,
    onSurfaceVariant = androidx.compose.ui.graphics.Color.LightGray,
    outline = Outline,
    outlineVariant = OutlineVariant,
    surfaceDim = SurfaceDimDark,
    surfaceBright = SurfaceBrightDark
)

private val LightColorScheme = lightColorScheme(
    primary = Primary,
    onPrimary = androidx.compose.ui.graphics.Color.White,
    primaryContainer = PrimaryContainer,
    onPrimaryContainer = OnPrimaryContainer,
    secondary = Secondary,
    onSecondary = androidx.compose.ui.graphics.Color.White,
    secondaryContainer = SecondaryContainer,
    onSecondaryContainer = OnSecondaryContainer,
    tertiary = Tertiary,
    onTertiary = androidx.compose.ui.graphics.Color.White,
    tertiaryContainer = TertiaryContainer,
    onTertiaryContainer = OnTertiaryContainer,
    error = Error,
    onError = androidx.compose.ui.graphics.Color.White,
    errorContainer = ErrorContainer,
    onErrorContainer = androidx.compose.ui.graphics.Color(0xFF410002),
    background = Surface,
    onBackground = androidx.compose.ui.graphics.Color(0xFF1C1B1F),
    surface = Surface,
    onSurface = androidx.compose.ui.graphics.Color(0xFF1C1B1F),
    surfaceVariant = SurfaceVariant,
    onSurfaceVariant = androidx.compose.ui.graphics.Color(0xFF49454F),
    outline = Outline,
    outlineVariant = OutlineVariant,
    surfaceDim = SurfaceDim,
    surfaceBright = SurfaceBright,
    surfaceContainerLowest = SurfaceContainerLowest,
    surfaceContainerLow = SurfaceContainerLow,
    surfaceContainer = SurfaceContainer,
    surfaceContainerHigh = SurfaceContainerHigh,
    surfaceContainerHighest = SurfaceContainerHighest
)

@Composable
fun BaseMVVMAndroidKtTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}