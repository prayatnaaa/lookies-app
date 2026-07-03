package com.prayatna.lookiesapp.ui.theme

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
    primary = PureWhite,
    onPrimary = AmoledBlack,
    primaryContainer = SoftGrey,
    onPrimaryContainer = PureWhite,

    secondary = GreyText,
    onSecondary = AmoledBlack,
    secondaryContainer = DarkGrey,
    onSecondaryContainer = PureWhite,

    tertiary = Gold,
    onTertiary = AmoledBlack,
    tertiaryContainer = Gold.copy(alpha = 0.2f),
    onTertiaryContainer = PureWhite,

    background = AmoledBlack,
    onBackground = PureWhite,
    surface = DeepCharcoal,
    onSurface = PureWhite,
    surfaceVariant = DarkGrey,
    onSurfaceVariant = GreyText,

    error = Error,
    onError = OnError,
    errorContainer = Error.copy(alpha = 0.3f),
    onErrorContainer = ErrorContainer,

    outline = GreyText,
    outlineVariant = SoftGrey
)

private val LightColorScheme = lightColorScheme(
    primary = BlackText,
    onPrimary = PureWhite,
    primaryContainer = LightGrey,
    onPrimaryContainer = BlackText,

    secondary = GreyTextLight,
    onSecondary = PureWhite,
    secondaryContainer = SoftWhite,
    onSecondaryContainer = BlackText,

    tertiary = Gold,
    onTertiary = AmoledBlack,
    tertiaryContainer = GoldContainer,
    onTertiaryContainer = PureWhite,

    background = OffWhite,
    onBackground = BlackText,
    surface = PureWhite,
    onSurface = BlackText,
    surfaceVariant = SoftWhite,
    onSurfaceVariant = GreyTextLight,

    error = Error,
    onError = OnError,
    errorContainer = ErrorContainer,
    onErrorContainer = OnErrorContainer,

    outline = GreyTextLight,
    outlineVariant = LightGrey
)

@Composable
fun LookiesAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = false,
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