package com.sb.core.resources.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

val MoonIcon: ImageVector
    get() {
        if (_MoonIcon != null) {
            return _MoonIcon!!
        }
        _MoonIcon = ImageVector.Builder(
            name = "MoonIcon",
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
                moveTo(3.32f, 11.684f)
                curveTo(3.32f, 16.654f, 7.35f, 20.684f, 12.32f, 20.684f)
                curveTo(16.108f, 20.684f, 19.348f, 18.344f, 20.677f, 15.032f)
                curveTo(19.64f, 15.449f, 18.506f, 15.683f, 17.32f, 15.683f)
                curveTo(12.35f, 15.683f, 8.32f, 11.654f, 8.32f, 6.683f)
                curveTo(8.32f, 5.503f, 8.552f, 4.363f, 8.965f, 3.33f)
                curveTo(5.656f, 4.66f, 3.32f, 7.899f, 3.32f, 11.684f)
                close()
            }
        }.build()

        return _MoonIcon!!
    }

@Suppress("ObjectPropertyName")
private var _MoonIcon: ImageVector? = null
