package com.liyawei.nbateamviewer.network

import com.liyawei.nbateamviewer.model.Team
import kotlinx.coroutines.Deferred
import kotlin.coroutines.CoroutineContext

interface INetworkClient {
    fun getTeams(coroutineContext: CoroutineContext) : Deferred<List<Team>?>
}