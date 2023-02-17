package ml.zedlabs.tbd.ui.home

import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.patrykandpatryk.vico.compose.axis.horizontal.bottomAxis
import com.patrykandpatryk.vico.compose.axis.vertical.startAxis
import com.patrykandpatryk.vico.compose.chart.Chart
import com.patrykandpatryk.vico.compose.chart.line.lineChart
import com.patrykandpatryk.vico.compose.component.marker.markerComponent
import com.patrykandpatryk.vico.compose.component.textComponent
import com.patrykandpatryk.vico.compose.style.ProvideChartStyle
import com.patrykandpatryk.vico.core.component.OverlayingComponent
import com.patrykandpatryk.vico.core.component.shape.DashedShape
import com.patrykandpatryk.vico.core.component.shape.LineComponent
import com.patrykandpatryk.vico.core.component.shape.ShapeComponent
import com.patrykandpatryk.vico.core.component.shape.Shapes.pillShape
import com.patrykandpatryk.vico.core.entry.ChartEntryModelProducer
import com.patrykandpatryk.vico.core.entry.FloatEntry

@Composable
fun LastTenDaysSpendChart(
    lastTenDayExpenses: List<Pair<String, Double>>
) {

    val startAxis = startAxis(
        maxLabelCount = 6,
        label = textComponent(
            color = MaterialTheme.colors.onBackground
        ),
//        valueFormatter = { value, chartValues ->
//            if (value == 0f)
//                ""
//            else {
//                val hours: Int = value.toInt() / 60
//                val minutes: Int = value.toInt() % 60
//                String.format("%dh%02dm", hours, minutes)
//            }
//        }
    )

    val bottomAxis = bottomAxis(
        label = textComponent(
            color = MaterialTheme.colors.onBackground
        ),
        valueFormatter = { value, _ ->
            lastTenDayExpenses.getOrNull(value.toInt())?.first ?: ""
        })

    var index = 0
    val entryModelMinutes = lastTenDayExpenses.map {
        FloatEntry(
            x = (index++).toFloat(),
            y = it.second.toFloat(),
        )
    }

    ProvideChartStyle(/*chartStyle = chartStyle*/) {
        val lineChart = lineChart()
        Chart(
            modifier = Modifier.padding(horizontal = 12.dp),
            chart = lineChart,
            chartModelProducer = ChartEntryModelProducer(entryModelMinutes),
            startAxis = startAxis,
            bottomAxis = bottomAxis,
            marker = markerComponent(
                label = textComponent(),
                indicator = OverlayingComponent(
                    outer = ShapeComponent(
                        shape = pillShape,
                        color = MaterialTheme.colors.onBackground.copy(alpha = 0.3f)
                            .hashCode()
                    ),
                    innerPaddingAllDp = 10f,
                    inner = OverlayingComponent(
                        outer = ShapeComponent(shape = pillShape),
                        inner = ShapeComponent(
                            shape = pillShape,
                            color = MaterialTheme.colors.onBackground.copy(alpha = 0.8f)
                                .hashCode()
                        ),
                        innerPaddingAllDp = 5f,
                    ),
                ),
                guideline = LineComponent(
                    color = MaterialTheme.colors.onBackground.copy(alpha = 0.3f).hashCode(),
                    thicknessDp = 2f,
                    shape = DashedShape(
                        shape = pillShape,
                        dashLengthDp = 8f,
                        gapLengthDp = 4f
                    ),
                ),
            )
        )
    }

}