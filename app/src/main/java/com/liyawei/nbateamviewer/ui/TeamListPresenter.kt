package com.liyawei.nbateamviewer.ui

import com.liyawei.nbateamviewer.network.INetworkClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class TeamListPresenter(
    val view: TeamListContract.View,
    val networkClient: INetworkClient,
    val mainContext: CoroutineContext,
    val IOContext: CoroutineContext
) : TeamListContract.Presenter {

    override fun loadTeams() {
        CoroutineScope(mainContext).launch {
            try {
                view.showloading()
                val result = networkClient.getTeams(IOContext)
                result.await()?.let {
                    view.showTeamList(it)
                }
            } catch (e: Exception) {
                view.showError()
            }
        }
    }

}