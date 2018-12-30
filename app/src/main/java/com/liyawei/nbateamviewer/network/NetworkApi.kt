package com.liyawei.nbateamviewer.network

import com.liyawei.nbateamviewer.model.Team
import retrofit2.Call
import retrofit2.http.GET

interface NetworkApi {
    @GET("5c27f8ad3300006900a58b2c?mocky-delay=3000ms")
    fun getTeams(): Call<List<Team>>
}