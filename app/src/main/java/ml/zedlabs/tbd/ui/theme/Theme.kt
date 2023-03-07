package ml.zedlabs.tbd.ui.theme

import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable

private val DarkColorPalette = darkColors(
    primary = BlueGreyPrimary,
    primaryVariant = BlueGreyPrimaryDark,
    secondary = TealSecondary,
    secondaryVariant = BlueGreyPrimaryLight,
)

private val LightColorPalette = lightColors(
    primary = Purple500,
    primaryVariant = Purple700,
    secondary = Teal200,
    secondaryVariant = BlueGreyPrimaryLight,
    background = BackgroundLight

    /* Other default colors to override
    background = Color.White,
    surface = Color.White,
    onPrimary = Color.White,
    onSecondary = Color.Black,
    onBackground = Color.Black,
    onSurface = Color.Black,
    */
)

val RedThemeColors = lightColors(
    primary = WhiteFF,
    secondary = WhiteCC,
    onSecondary = Black00,
    background = RedBF
)

val LightAlternateColors = lightColors(
    primary = WhiteFF,
    secondary = WhiteCC,
    onSecondary = altOneText,
    background = altOneBg
)

val DarkThemeColors = lightColors(
    primary = WhiteFF,
    secondary = WhiteCC,
    onSecondary = darkText,
    background = darkBg
)

/**
 * Can have an enum with multiple possible themes here as well
 * instead on light and dark only
 */
@Composable
fun ExpenseTheme(
    appTheme: String,
    content: @Composable () -> Unit
) {
    val colors = when (appTheme) {
        AppThemeType.LightAlternate.name -> LightAlternateColors
        AppThemeType.Dark.name -> DarkThemeColors
        else -> RedThemeColors
    }

    MaterialTheme(
        colors = colors,
        typography = MontrealTypography,
        shapes = Shapes,
        content = content
    )
}