package ml.zedlabs.expenseButler.ui.common

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

@Composable
fun DefaultTopButton(
    imageVector: ImageVector,
    bgColor: Color = MaterialTheme.colors.primary,
    itemColor: Color = MaterialTheme.colors.onSecondary,
    clickAction: () -> Unit,
) {
    Box(
        modifier = Modifier
            .clip(CircleShape)
            .size(42.dp)
            .background(bgColor)
            .clickable { clickAction.invoke() },
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = imageVector,
            contentDescription = "back-button",
            tint = itemColor,
            modifier = Modifier.size(20.dp)
        )
    }
}

@Composable
fun ButtonRoundedStyle(
    click: () -> Unit,
    mod: Modifier = Modifier,
    text: String,
    vector: ImageVector? = null
) {
    Row(
        modifier = mod
            .clip(RoundedCornerShape(12.dp))
            .clickable { click.invoke() }
            .background(MaterialTheme.colors.onSecondary.copy(alpha = 0.2f))
            .padding(vertical = 8.dp, horizontal = 12.dp),
    ) {
        MediumText(
            text = text,
            color = MaterialTheme.colors.onSecondary
        )
        if (vector != null) {
            Icon(
                imageVector = vector,
                tint = MaterialTheme.colors.onSecondary,
                contentDescription = "special button click action"
            )
        }
    }
}