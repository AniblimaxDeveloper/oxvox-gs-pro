package com.oxvox.app.ui.theme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val VillainColorScheme = darkColorScheme(
    primary = ComicPurple,
    onPrimary = TextWhite,
    secondary = NeonCyan,
    onSecondary = VillainBlack,
    tertiary = BloodRed,
    background = VillainBlack,
    onBackground = TextWhite,
    surface = VillainDarkGray,
    onSurface = TextWhite,
    error = BloodRed
)

@Composable
fun OXVOXTheme(content: @Composable () -> Unit) {
    MaterialTheme(colorScheme = VillainColorScheme, content = content)
}
