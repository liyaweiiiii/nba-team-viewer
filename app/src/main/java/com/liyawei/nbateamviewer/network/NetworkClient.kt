package com.liyawei.nbateamviewer.network

import com.liyawei.nbateamviewer.model.Team
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.coroutines.CoroutineContext

object NetworkClient : INetworkClient {
    private val NetworkApi = Retrofit.Builder()
        .baseUrl("http://www.mocky.io/v2/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(NetworkApi::class.java)

    override fun getTeams(coroutineContext: CoroutineContext): Deferred<List<Team>?> {
        return CoroutineScope(coroutineContext).async {
            NetworkApi.getTeams().execute().body()
        }
    }
}