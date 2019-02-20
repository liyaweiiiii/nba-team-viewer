package com.liyawei.nbateamviewer.model

import android.arch.persistence.room.Entity
import android.arch.persistence.room.Ignore
import android.arch.persistence.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity
data class Team(
    @PrimaryKey val id: Int,
    @SerializedName("full_name") val fullName: String,
    val wins: Int,
    val losses: Int
) {
    var selected = false
}