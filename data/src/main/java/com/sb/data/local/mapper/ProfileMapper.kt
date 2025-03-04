package com.sb.data.local.mapper

import com.sb.data.local.dbo.ProfileDbo
import com.sb.domain.entity.Profile

internal fun Profile.toEntity() = ProfileDbo(
    id = id,
    name = name,
    gains = gains,
    amplitude = amplitude,
    leftChannel = leftChannel,
    rightChannel = rightChannel
)

internal fun ProfileDbo.toDomain() = Profile(
    id = id,
    name = name,
    gains = gains,
    amplitude = amplitude,
    leftChannel = leftChannel,
    rightChannel = rightChannel
)