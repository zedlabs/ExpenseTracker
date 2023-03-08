package ml.zedlabs.expenseButler.ui.common

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ml.zedlabs.expenseButler.ui.theme.MontrealTypography

@Composable
fun TitleTextH2(text: AnnotatedString, modifier: Modifier) {
    Text(
        text = text,
        modifier = modifier,
        color = MaterialTheme.colors.primary,
        style = MontrealTypography.h2,
    )
}

@Composable
fun TitleTextH2(text: String, modifier: Modifier) {
    Text(
        text = text,
        modifier = modifier,
        color = MaterialTheme.colors.primary,
        style = MontrealTypography.h2,
    )
}

@Composable
fun DividerWithPadding() {
    Divider(
        color = MaterialTheme.colors.onBackground.copy(alpha = 0.1f),
        thickness = 1.dp,
        modifier = Modifier.padding(horizontal = 20.dp, vertical = 12.dp)
    )
}

@Composable
fun Spacer12() {
    Spacer(modifier = Modifier.height(12.dp))
}

@Composable
fun HSpacer12() {
    Spacer(modifier = Modifier.width(12.dp))
}

@Composable
fun HSpacer24() {
    Spacer(modifier = Modifier.width(24.dp))
}

@Composable
fun Spacer24() {
    Spacer(modifier = Modifier.height(24.dp))
}

@Composable
fun PrimaryText(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colors.primary,
    fontSize: TextUnit = 15.sp,
    fontWeight: FontWeight = FontWeight.Normal,
    align: TextAlign = TextAlign.Start,
    ) {
    Text(
        text = text,
        color = color,
        fontSize = fontSize,
        modifier = modifier,
        fontWeight = fontWeight,
        textAlign = align
    )
}


@Composable
fun SecondaryText(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colors.primary,
    fontSize: TextUnit = 15.sp,
    maxLines: Int = Int.MAX_VALUE,
    overflow: TextOverflow = TextOverflow.Clip,
) {
    Text(
        text = text,
        color = color,
        fontSize = fontSize,
        modifier = modifier,
        maxLines = maxLines,
        overflow = overflow
    )
}

@Composable
fun LargeText(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colors.primary,
    fontSize: TextUnit = 32.sp,
    maxLines: Int = Int.MAX_VALUE,
    overflow: TextOverflow = TextOverflow.Clip,
) {
    Text(
        text = text,
        color = color,
        fontSize = fontSize,
        modifier = modifier,
        maxLines = maxLines,
        overflow = overflow,
        fontWeight = FontWeight.Normal
    )
}

@Composable
fun SemiLargeText(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colors.primary,
    fontSize: TextUnit = 24.sp,
    maxLines: Int = Int.MAX_VALUE,
    overflow: TextOverflow = TextOverflow.Clip,
) {
    Text(
        text = text,
        color = color,
        fontSize = fontSize,
        modifier = modifier,
        maxLines = maxLines,
        overflow = overflow,
        fontWeight = FontWeight.SemiBold
    )
}

@Composable
fun MediumText(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colors.primary,
    fontSize: TextUnit = 16.sp,
    maxLines: Int = Int.MAX_VALUE,
    overflow: TextOverflow = TextOverflow.Clip,
    fontWeight: FontWeight = FontWeight.Light,
    style: TextStyle? = null
) {
    if (style == null) {
        Text(
            text = text,
            color = color,
            fontSize = fontSize,
            modifier = modifier,
            maxLines = maxLines,
            overflow = overflow,
            fontWeight = fontWeight
        )
    } else {
        Text(
            text = text,
            color = color,
            fontSize = fontSize,
            modifier = modifier,
            maxLines = maxLines,
            overflow = overflow,
            fontWeight = fontWeight,
            style = style
        )
    }

}









