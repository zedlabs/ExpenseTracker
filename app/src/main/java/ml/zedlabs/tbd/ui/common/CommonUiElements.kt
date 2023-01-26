package ml.zedlabs.tbd.ui.common

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Person
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

@Composable
fun DefaultTopButton(imageVector: ImageVector, clickAction: () -> Unit) {
    Box(
        modifier = Modifier
            .clip(CircleShape)
            .size(42.dp)
            .background(MaterialTheme.colors.primary)
            .clickable { clickAction.invoke() },
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = imageVector,
            contentDescription = "back-button",
            tint = MaterialTheme.colors.onSecondary,
            modifier = Modifier.size(20.dp)
        )
    }
}