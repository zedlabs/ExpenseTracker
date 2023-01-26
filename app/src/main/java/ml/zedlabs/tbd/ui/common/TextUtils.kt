package ml.zedlabs.tbd.ui.common

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ml.zedlabs.tbd.ui.theme.MontrealTypography
import ml.zedlabs.tbd.ui.theme.Typography

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
    ) {
    Text(
        text = text,
        color = color,
        fontSize = fontSize,
        modifier = modifier,
        fontWeight = fontWeight,
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









