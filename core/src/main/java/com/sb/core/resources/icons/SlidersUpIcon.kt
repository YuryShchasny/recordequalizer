package com.sb.core.resources.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

val SlidersUpIcon: ImageVector
    get() {
        if (_SlidersUpIcon != null) {
            return _SlidersUpIcon!!
        }
        _SlidersUpIcon = ImageVector.Builder(
            name = "SlidersUpSvgrepoCom",
            defaultWidth = 800.dp,
            defaultHeight = 800.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            path(
                stroke = SolidColor(Color(0xFF000000)),
                strokeLineWidth = 2f,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round
            ) {
                moveTo(19.5f, 12f)
                curveTo(18.119f, 12f, 17f, 10.881f, 17f, 9.5f)
                curveTo(17f, 8.119f, 18.119f, 7f, 19.5f, 7f)
                moveTo(19.5f, 12f)
                curveTo(20.881f, 12f, 22f, 10.881f, 22f, 9.5f)
                curveTo(22f, 8.119f, 20.881f, 7f, 19.5f, 7f)
                moveTo(19.5f, 12f)
                verticalLineTo(21f)
                moveTo(19.5f, 7f)
                verticalLineTo(3f)
                moveTo(12f, 19f)
                curveTo(10.619f, 19f, 9.5f, 17.881f, 9.5f, 16.5f)
                curveTo(9.5f, 15.119f, 10.619f, 14f, 12f, 14f)
                moveTo(12f, 19f)
                curveTo(13.381f, 19f, 14.5f, 17.881f, 14.5f, 16.5f)
                curveTo(14.5f, 15.119f, 13.381f, 14f, 12f, 14f)
                moveTo(12f, 19f)
                verticalLineTo(21f)
                moveTo(12f, 14f)
                verticalLineTo(3f)
                moveTo(4.5f, 10f)
                curveTo(3.119f, 10f, 2f, 8.881f, 2f, 7.5f)
                curveTo(2f, 6.119f, 3.119f, 5f, 4.5f, 5f)
                moveTo(4.5f, 10f)
                curveTo(5.881f, 10f, 7f, 8.881f, 7f, 7.5f)
                curveTo(7f, 6.119f, 5.881f, 5f, 4.5f, 5f)
                moveTo(4.5f, 10f)
                verticalLineTo(21f)
                moveTo(4.5f, 5f)
                verticalLineTo(3f)
            }
        }.build()

        return _SlidersUpIcon!!
    }

@Suppress("ObjectPropertyName")
private var _SlidersUpIcon: ImageVector? = null
