package com.sb.core.resources.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

val SunIcon: ImageVector
    get() {
        if (_SunIcon != null) {
            return _SunIcon!!
        }
        _SunIcon = ImageVector.Builder(
            name = "SunIcon",
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
                moveTo(12f, 3f)
                verticalLineTo(4f)
                moveTo(12f, 20f)
                verticalLineTo(21f)
                moveTo(4f, 12f)
                horizontalLineTo(3f)
                moveTo(6.314f, 6.314f)
                lineTo(5.5f, 5.5f)
                moveTo(17.686f, 6.314f)
                lineTo(18.5f, 5.5f)
                moveTo(6.314f, 17.69f)
                lineTo(5.5f, 18.5f)
                moveTo(17.686f, 17.69f)
                lineTo(18.5f, 18.5f)
                moveTo(21f, 12f)
                horizontalLineTo(20f)
                moveTo(16f, 12f)
                curveTo(16f, 14.209f, 14.209f, 16f, 12f, 16f)
                curveTo(9.791f, 16f, 8f, 14.209f, 8f, 12f)
                curveTo(8f, 9.791f, 9.791f, 8f, 12f, 8f)
                curveTo(14.209f, 8f, 16f, 9.791f, 16f, 12f)
                close()
            }
        }.build()

        return _SunIcon!!
    }

@Suppress("ObjectPropertyName")
private var _SunIcon: ImageVector? = null
