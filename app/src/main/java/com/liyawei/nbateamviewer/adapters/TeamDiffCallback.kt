package com.liyawei.nbateamviewer.adapters

import android.os.Bundle
import android.support.v7.util.DiffUtil
import com.liyawei.nbateamviewer.model.Team

class TeamDiffCallback : DiffUtil.ItemCallback<Team>() {
    override fun areItemsTheSame(oldTeam: Team, newTeam: Team) =
        oldTeam.id == newTeam.id

    override fun areContentsTheSame(oldTeam: Team, newTeam: Team) =
        oldTeam == newTeam

    override fun getChangePayload(oldItem: Team, newItem: Team): Any? {
        val bundle = Bundle()
        if (newItem.fullName != oldItem.fullName)
            bundle.putString(UPDATE_NAME, newItem.fullName)

        if (newItem.wins != oldItem.wins)
            bundle.putInt(UPDATE_WINS, newItem.wins)

        if (newItem.losses != oldItem.losses)
            bundle.putInt(UPDATE_LOSSES, newItem.losses)

        return if (bundle.isEmpty) null else bundle
    }

    companion object {
        const val UPDATE_NAME = "update_name"
        const val UPDATE_WINS = "update_wins"
        const val UPDATE_LOSSES = "update_losses"
    }
}