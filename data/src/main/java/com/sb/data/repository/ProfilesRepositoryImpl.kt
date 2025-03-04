package com.sb.data.repository

import com.sb.data.local.DefaultFrequencies
import com.sb.data.local.datasource.ProfilesDataSource
import com.sb.data.local.datasource.SettingsDataSource
import com.sb.domain.entity.Profile
import com.sb.domain.repository.ProfilesRepository

class ProfilesRepositoryImpl(
    private val profilesDataSource: ProfilesDataSource,
    private val settingsDataSource: SettingsDataSource,
) : ProfilesRepository {
    override suspend fun getProfiles(): List<Profile> = profilesDataSource.getProfiles()

    override suspend fun addProfile(profile: Profile) = profilesDataSource.addProfile(profile)

    override suspend fun deleteProfile(id: Long) = profilesDataSource.deleteProfile(id)

    override suspend fun getProfile(id: Long): Profile? = profilesDataSource.getProfile(id)

    override suspend fun setSelectedProfile(id: Long) = settingsDataSource.setSelectedProfile(id)

    override suspend fun getSelectedProfile(): Profile? {
        val selectedProfileId = settingsDataSource.getSettings().selectedProfileId
        return profilesDataSource.getProfile(selectedProfileId)
    }

    override fun getDefaultFrequencies(): List<Pair<Int, Float>> = DefaultFrequencies.get()
}
