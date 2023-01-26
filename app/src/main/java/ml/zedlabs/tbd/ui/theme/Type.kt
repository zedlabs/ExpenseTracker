package ml.zedlabs.tbd.ui.theme

import androidx.compose.material.Typography
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import ml.zedlabs.tbd.R

// Set of Material typography styles to start with

val fontFamilyRoboto = FontFamily(
    fonts = listOf(
        Font(R.font.roboto_bold, FontWeight.Bold),
        Font(R.font.roboto_light, FontWeight.Light),
        Font(R.font.roboto_medium, FontWeight.Medium),
        Font(R.font.roboto_regular, FontWeight.Normal),
        Font(R.font.roboto_thin, FontWeight.Thin),
    )
)

val fontFamilyMontreal = FontFamily(
    fonts = listOf(
        Font(R.font.montreal_bold, FontWeight.Bold),
        Font(R.font.montreal_light, FontWeight.Light),
        Font(R.font.montreal_regular, FontWeight.Normal),
        Font(R.font.montreal_italic, FontWeight.SemiBold),
    )
)

val MontrealTypography = Typography(
    h1 = TextStyle(
        fontFamily = fontFamilyMontreal,
        fontWeight = FontWeight.Normal,
        letterSpacing = 1.sp,
        fontSize = 64.sp,
    ),
    h2 = TextStyle(
        fontFamily = fontFamilyMontreal,
        fontWeight = FontWeight.Normal,
        letterSpacing = 1.sp,
        fontSize = 32.sp,
    ),
    body1 = TextStyle(
        fontFamily = fontFamilyMontreal,
        fontWeight = FontWeight.Light,
        letterSpacing = 1.sp,
        fontSize = 16.sp
    ),
)

val Typography = Typography(
    body1 = TextStyle(
        fontFamily = fontFamilyRoboto,
        fontWeight = FontWeight.Normal,
        letterSpacing = 1.sp,
        fontSize = 16.sp
    ),
    h1 = TextStyle(
        fontFamily = fontFamilyRoboto,
        fontWeight = FontWeight.Medium,
        letterSpacing = 1.sp,
        fontSize = 32.sp,
    ),
    h2 = TextStyle(
        fontFamily = fontFamilyRoboto,
        fontWeight = FontWeight.Bold,
        letterSpacing = 1.sp,
        fontSize = 32.sp,
    ),
    caption = TextStyle(
        fontFamily = fontFamilyRoboto,
        fontWeight = FontWeight.Light,
        letterSpacing = 1.sp,
        color = BlueGreyPrimaryLight,
        fontSize = 12.sp
    )
    /* Other default text styles to override
    button = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.W500,
        fontSize = 14.sp
    ),
    caption = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp
    )
    */
)

val titleStyle = TextStyle(
    shadow = Shadow(
        color = Color(0xFFE0E0E0),
        blurRadius = 40f
    ),
)

val searchBarStyle = TextStyle(
    fontFamily = FontFamily(
        fonts = listOf(
            Font(R.font.roboto_regular, FontWeight.Light)
        )
    )
)

