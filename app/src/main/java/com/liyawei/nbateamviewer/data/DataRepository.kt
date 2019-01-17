package com.liyawei.nbateamviewer.data

import android.arch.lifecycle.LiveData
import com.liyawei.nbateamviewer.model.Team
import com.liyawei.nbateamviewer.network.INetworkClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class DataRepository(private val network: INetworkClient, private val teamDao: TeamDao) {

    val teams: LiveData<List<Team>> by lazy {
        teamDao.loadTeams()
    }

    suspend fun refreshTeams() {
        val result = network.getTeams(Dispatchers.IO).await()
        withContext(Dispatchers.IO) {
            try {
                result?.let {
                    teamDao.insertTeams(it)
                }
            } catch (e: Exception) {
                throw e
            }

        }
    }
}