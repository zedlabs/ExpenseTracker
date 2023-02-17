package ml.zedlabs.tbd.ui.home

import android.graphics.Typeface
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.patrykandpatryk.vico.compose.axis.horizontal.bottomAxis
import com.patrykandpatryk.vico.compose.axis.vertical.startAxis
import com.patrykandpatryk.vico.compose.chart.Chart
import com.patrykandpatryk.vico.compose.chart.line.lineChart
import com.patrykandpatryk.vico.compose.component.marker.markerComponent
import com.patrykandpatryk.vico.compose.component.shape.shader.fromBrush
import com.patrykandpatryk.vico.compose.component.textComponent
import com.patrykandpatryk.vico.compose.style.ChartStyle
import com.patrykandpatryk.vico.compose.style.ProvideChartStyle
import com.patrykandpatryk.vico.core.DefaultAlpha
import com.patrykandpatryk.vico.core.DefaultDimens
import com.patrykandpatryk.vico.core.chart.line.LineChart
import com.patrykandpatryk.vico.core.component.OverlayingComponent
import com.patrykandpatryk.vico.core.component.shape.DashedShape
import com.patrykandpatryk.vico.core.component.shape.LineComponent
import com.patrykandpatryk.vico.core.component.shape.ShapeComponent
import com.patrykandpatryk.vico.core.component.shape.Shapes
import com.patrykandpatryk.vico.core.component.shape.Shapes.pillShape
import com.patrykandpatryk.vico.core.component.shape.shader.DynamicShaders
import com.patrykandpatryk.vico.core.dimensions.MutableDimensions
import com.patrykandpatryk.vico.core.entry.ChartEntryModelProducer
import com.patrykandpatryk.vico.core.entry.FloatEntry

@Composable
fun LastTenDaysSpendChart(
    lastTenDayExpenses: List<Pair<String, Double>>
) {

    val startAxis = startAxis(
        maxLabelCount = 6,
        label = null,
        guideline = null,
        axis = null,
        tick = null,
    )

    val bottomAxis = bottomAxis(
        tick = null,
        axis = null,
        label = textComponent(
            color = MaterialTheme.colors.primary,
            typeface = Typeface.MONOSPACE,
            textSize = 11.sp,
            margins = MutableDimensions(0f, 4f)
        ),
        guideline = null,
        valueFormatter = { value, _ ->
            lastTenDayExpenses.getOrNull(value.toInt())?.first ?: ""
        })

    var index = 0
    val entryModelExpenses = lastTenDayExpenses.map {
        FloatEntry(
            x = (index++).toFloat(),
            y = it.second.toFloat(),
        )
    }

    val chartColors = listOf<Color>(MaterialTheme.colors.primary)

    ProvideChartStyle(rememberChartStyle(chartColors)) {
        val lineChart = lineChart()
        Chart(
            chart = lineChart,
            chartModelProducer = ChartEntryModelProducer(entryModelExpenses),
            startAxis = startAxis,
            bottomAxis = bottomAxis,
            marker = markerComponent(
                label = textComponent(
                    typeface = Typeface.MONOSPACE,
                    textSize = 11.sp
                ),
                indicator = OverlayingComponent(
                    outer = ShapeComponent(
                        shape = pillShape,
                        color = MaterialTheme.colors.primary.copy(alpha = 0.3f).toArgb()
                    ),
                    innerPaddingAllDp = 10f,
                    inner = OverlayingComponent(
                        outer = ShapeComponent(shape = pillShape),
                        inner = ShapeComponent(
                            shape = pillShape,
                            color = MaterialTheme.colors.primary.copy(alpha = 0.8f).toArgb()
                        ),
                        innerPaddingAllDp = 5f,
                    ),
                ),
                guideline = LineComponent(
                    color = MaterialTheme.colors.primary.copy(alpha = 0.3f).hashCode(),
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

@Composable
internal fun rememberChartStyle(columnChartColors: List<Color>, lineChartColors: List<Color>): ChartStyle {
    return remember(columnChartColors, lineChartColors) {
        ChartStyle(
            ChartStyle.Axis(
                axisLabelColor = columnChartColors.first(),
                axisGuidelineColor = columnChartColors.first(),
                axisLineColor = columnChartColors.first(),
            ),
            ChartStyle.ColumnChart(
                columnChartColors.map { columnChartColor ->
                    LineComponent(
                        columnChartColor.toArgb(),
                        DefaultDimens.COLUMN_WIDTH,
                        Shapes.roundedCornerShape(DefaultDimens.COLUMN_ROUNDNESS_PERCENT),
                    )
                },
            ),
            ChartStyle.LineChart(
                lineChartColors.map { lineChartColor ->
                    LineChart.LineSpec(
                        lineColor = lineChartColor.toArgb(),
                        lineBackgroundShader = DynamicShaders.fromBrush(
                            Brush.verticalGradient(
                                listOf(
                                    lineChartColor.copy(DefaultAlpha.LINE_BACKGROUND_SHADER_START),
                                    lineChartColor.copy(DefaultAlpha.LINE_BACKGROUND_SHADER_END),
                                ),
                            ),
                        ),
                    )
                },
            ),
            ChartStyle.Marker(),
            columnChartColors.first(),
        )
    }
}

@Composable
internal fun rememberChartStyle(chartColors: List<Color>) =
    rememberChartStyle(columnChartColors = chartColors, lineChartColors = chartColors)

private const val COLOR_1_CODE = 0xffa485e0
private const val PERSISTENT_MARKER_X = 10f

private val color1 = Color(COLOR_1_CODE)
//private val chartColors = listOf(color1)