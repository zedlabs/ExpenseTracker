package ml.zedlabs.expenseButler.ui.common

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import ml.zedlabs.expenseButler.R

@Composable
fun AnimatedShimmer() {
//    val shimmerColors = listOf(
//        colorResource(id = R.color.ld_4),
//        colorResource(id = R.color.ld_4),
//        colorResource(id = R.color.ld_4),
//    )
//
//    val transition = rememberInfiniteTransition()
//    val translateAnim = transition.animateFloat(
//        initialValue = 0f,
//        targetValue = 1000f,
//        animationSpec = infiniteRepeatable(
//            animation = tween(
//                durationMillis = 1000,
//                easing = FastOutSlowInEasing
//            ),
//            repeatMode = RepeatMode.Reverse
//        )
//    )
//
//    val brush = Brush.linearGradient(
//        colors = shimmerColors,
//        start = Offset.Zero,
//        end = Offset(x = translateAnim.value, y = translateAnim.value)
//    )

    ShimmerGridItem()
}

@Composable
fun ShimmerGridItem() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Spacer(modifier = Modifier.width(10.dp))
        Spacer(
            modifier = Modifier
                .padding(vertical = 10.dp)
                .width(160.dp)
                .height(245.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(colorResource(id = R.color.ld_4))
                .border(
                    width = 2.dp,
                    color = MaterialTheme.colors.onBackground,
                    shape = RoundedCornerShape(12.dp)
                )
        )
        Spacer(modifier = Modifier.width(20.dp))
        Spacer(
            modifier = Modifier
                .padding(vertical = 10.dp)
                .width(160.dp)
                .height(245.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(colorResource(id = R.color.ld_4))
                .border(
                    width = 2.dp,
                    color = MaterialTheme.colors.onBackground,
                    shape = RoundedCornerShape(12.dp)
                )
        )
        Spacer(modifier = Modifier.width(20.dp))
        Spacer(
            modifier = Modifier
                .padding(vertical = 10.dp)
                .width(160.dp)
                .height(245.dp)
                .clip(RoundedCornerShape(topStart = 12.dp, bottomStart = 12.dp))
                .background(colorResource(id = R.color.ld_4))
                .border(
                    width = 2.dp,
                    color = MaterialTheme.colors.onBackground,
                    shape = RoundedCornerShape(topStart = 12.dp, bottomStart = 12.dp)
                )
        )
    }
}