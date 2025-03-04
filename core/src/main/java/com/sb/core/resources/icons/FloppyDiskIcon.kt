package com.sb.core.resources.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

val FloppyDiskIcon: ImageVector
    get() {
        if (_FloppyDiskIcon != null) {
            return _FloppyDiskIcon!!
        }
        _FloppyDiskIcon = ImageVector.Builder(
            name = "FloppyDiskIcon",
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
                moveTo(9f, 3f)
                verticalLineTo(5.4f)
                curveTo(9f, 5.96f, 9f, 6.24f, 9.109f, 6.454f)
                curveTo(9.205f, 6.642f, 9.358f, 6.795f, 9.546f, 6.891f)
                curveTo(9.76f, 7f, 10.04f, 7f, 10.6f, 7f)
                horizontalLineTo(12.4f)
                curveTo(12.96f, 7f, 13.24f, 7f, 13.454f, 6.891f)
                curveTo(13.642f, 6.795f, 13.795f, 6.642f, 13.891f, 6.454f)
                curveTo(14f, 6.24f, 14f, 5.96f, 14f, 5.4f)
                verticalLineTo(3.004f)
                moveTo(19f, 9.123f)
                verticalLineTo(17.8f)
                curveTo(19f, 18.92f, 19f, 19.48f, 18.782f, 19.908f)
                curveTo(18.59f, 20.284f, 18.284f, 20.59f, 17.908f, 20.782f)
                curveTo(17.48f, 21f, 16.92f, 21f, 15.8f, 21f)
                horizontalLineTo(8.2f)
                curveTo(7.08f, 21f, 6.52f, 21f, 6.092f, 20.782f)
                curveTo(5.716f, 20.59f, 5.41f, 20.284f, 5.218f, 19.908f)
                curveTo(5f, 19.48f, 5f, 18.92f, 5f, 17.8f)
                verticalLineTo(6.2f)
                curveTo(5f, 5.08f, 5f, 4.52f, 5.218f, 4.092f)
                curveTo(5.41f, 3.716f, 5.716f, 3.41f, 6.092f, 3.218f)
                curveTo(6.52f, 3f, 7.08f, 3f, 8.2f, 3f)
                horizontalLineTo(13.462f)
                curveTo(14.027f, 3f, 14.309f, 3f, 14.57f, 3.072f)
                curveTo(14.801f, 3.135f, 15.019f, 3.24f, 15.213f, 3.381f)
                curveTo(15.432f, 3.539f, 15.608f, 3.76f, 15.961f, 4.201f)
                lineTo(18.299f, 7.123f)
                curveTo(18.559f, 7.448f, 18.689f, 7.611f, 18.781f, 7.792f)
                curveTo(18.863f, 7.952f, 18.923f, 8.123f, 18.959f, 8.299f)
                curveTo(19f, 8.498f, 19f, 8.706f, 19f, 9.123f)
                close()
                moveTo(14f, 15.5f)
                curveTo(14f, 16.881f, 12.881f, 18f, 11.5f, 18f)
                curveTo(10.119f, 18f, 9f, 16.881f, 9f, 15.5f)
                curveTo(9f, 14.119f, 10.119f, 13f, 11.5f, 13f)
                curveTo(12.881f, 13f, 14f, 14.119f, 14f, 15.5f)
                close()
            }
        }.build()

        return _FloppyDiskIcon!!
    }

@Suppress("ObjectPropertyName")
private var _FloppyDiskIcon: ImageVector? = null
