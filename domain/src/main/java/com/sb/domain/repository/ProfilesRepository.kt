package com.sb.domain.repository

import com.sb.domain.entity.Profile

interface ProfilesRepository {
    suspend fun getProfiles(): List<Profile>
    suspend fun addProfile(profile: Profile)
    suspend fun deleteProfile(id: Long)
    suspend fun getProfile(id: Long): Profile?
    suspend fun setSelectedProfile(id: Long)
    suspend fun getSelectedProfile(): Profile?
    fun getDefaultFrequencies(): List<Pair<Int, Float>>
}
