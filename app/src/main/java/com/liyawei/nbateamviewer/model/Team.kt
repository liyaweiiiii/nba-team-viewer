package com.liyawei.nbateamviewer.model

import com.google.gson.annotations.SerializedName

data class Team(
    val id: Int,
    @SerializedName("full_name") val fullName: String,
    val wins: Int,
    val losses: Int
)