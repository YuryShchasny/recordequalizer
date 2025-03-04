package com.sb.data.local.converter

import androidx.room.TypeConverter

class FloatListConverter {
    @TypeConverter
    fun fromList(list: List<Float>?): String {
        return list?.joinToString(",") ?: ""
    }

    @TypeConverter
    fun toList(data: String?): List<Float> {
        if (data.isNullOrEmpty()) return emptyList()
        return data.split(",").mapNotNull { it.toFloatOrNull() }
    }
}
