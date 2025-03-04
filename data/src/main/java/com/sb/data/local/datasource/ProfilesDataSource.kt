package com.sb.data.local.datasource

import com.sb.data.local.dao.ProfilesDao
import com.sb.data.local.mapper.toDomain
import com.sb.data.local.mapper.toEntity
import com.sb.domain.entity.Profile
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ProfilesDataSource(
    private val profilesDao: ProfilesDao
) {
    suspend fun getProfiles(): List<Profile> = withContext(Dispatchers.IO) {
        profilesDao.getProfiles().map { it.toDomain() }
    }

    suspend fun addProfile(profile: Profile) = withContext(Dispatchers.IO) {
        profilesDao.insertProfile(profile.toEntity())
    }

    suspend fun deleteProfile(id: Long) = withContext(Dispatchers.IO) {
        profilesDao.deleteProfile(id)
    }

    suspend fun getProfile(id: Long): Profile? = withContext(Dispatchers.IO) {
        profilesDao.getProfile(id)?.toDomain()
    }
}
