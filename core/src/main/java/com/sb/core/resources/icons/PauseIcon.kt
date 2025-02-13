package com.sb.core.resources.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

val PauseIcon: ImageVector
    get() {
        if (_PauseIcon != null) {
            return _PauseIcon!!
        }
        _PauseIcon = ImageVector.Builder(
            name = "PauseIcon",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            path(
                stroke = SolidColor(Color(0xFFEAF0FF)),
                strokeLineWidth = 2.08334f
            ) {
                moveTo(8.8f, 4.8f)
                verticalLineTo(19.2f)
                moveTo(15.2f, 4.8f)
                verticalLineTo(19.2f)
            }
        }.build()

        return _PauseIcon!!
    }

@Suppress("ObjectPropertyName")
private var _PauseIcon: ImageVector? = null
