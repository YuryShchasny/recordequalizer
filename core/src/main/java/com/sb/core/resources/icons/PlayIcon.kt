package com.sb.core.resources.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

val PlayIcon: ImageVector
    get() {
        if (_PlayIcon != null) {
            return _PlayIcon!!
        }
        _PlayIcon = ImageVector.Builder(
            name = "PlayIcon",
            defaultWidth = 25.dp,
            defaultHeight = 25.dp,
            viewportWidth = 25f,
            viewportHeight = 25f
        ).apply {
            path(
                stroke = SolidColor(Color(0xFFEAF0FF)),
                strokeLineWidth = 2.08334f,
                strokeLineJoin = StrokeJoin.Round
            ) {
                moveTo(9f, 21f)
                lineTo(9f, 5f)
                lineTo(20.2f, 13f)
                lineTo(9f, 21f)
                close()
            }
        }.build()

        return _PlayIcon!!
    }

@Suppress("ObjectPropertyName")
private var _PlayIcon: ImageVector? = null
