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
    secondary = DarkPurple,
    tertiary = PurpleGrey,
    background = DarkBlue
)

private val LightColorScheme = lightColorScheme(
    primary = DarkBlue,
    secondary = PureWhite,
    tertiary = PurpleGrey,
    background = LightGrey

)

//private val LightColorScheme = lightColorScheme(
//    primary = light_primary,
//    onPrimary = light_onPrimary,
//    primaryContainer = light_primaryContainer,
//    onPrimaryContainer = light_onPrimaryContainer,
//    secondary = light_secondary,
//    onSecondary = light_onSecondary,
//    background = light_background,
//    onBackground = light_onBackground,
//    surface = light_surface,
//    onSurface = light_onSurface,
//    error = light_error,
//    onError = light_onError
//)

//private val DarkColorScheme = darkColorScheme(
//    primary = dark_primary,
//    onPrimary = dark_onPrimary,
//    primaryContainer = dark_primaryContainer,
//    onPrimaryContainer = dark_onPrimaryContainer,
//    secondary = dark_secondary,
//    onSecondary = dark_onSecondary,
//    background = dark_background,
//    onBackground = dark_onBackground,
//    surface = dark_surface,
//    onSurface = dark_onSurface,
//    error = dark_error,
//    onError = dark_onError
//)\

@Composable
fun LookiesAppTheme(
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