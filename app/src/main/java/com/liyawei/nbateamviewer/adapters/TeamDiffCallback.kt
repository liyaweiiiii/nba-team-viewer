package com.liyawei.nbateamviewer.adapters

import android.os.Bundle
import android.support.v7.util.DiffUtil
import com.liyawei.nbateamviewer.model.Team

class TeamDiffCallback() : DiffUtil.ItemCallback<Team>() {
    override fun areItemsTheSame(oldTeam: Team, newTeam: Team) =
        oldTeam.id == newTeam.id

    override fun areContentsTheSame(oldTeam: Team, newTeam: Team) =
        oldTeam == newTeam

    override fun getChangePayload(oldTeam: Team, newTeam: Team): Any? {
        val bundle = Bundle()
        if (oldTeam.fullName != newTeam.fullName)
            bundle.putString(UPDATE_NAME, newTeam.fullName)

        if (oldTeam.wins != newTeam.wins)
            bundle.putInt(UPDATE_WINS, newTeam.wins)

        if (oldTeam.losses != newTeam.losses)
            bundle.putInt(UPDATE_LOSSES, newTeam.losses)

        return if (bundle.isEmpty) null else bundle
    }

    companion object {
        const val UPDATE_NAME = "update_name"
        const val UPDATE_WINS = "update_wins"
        const val UPDATE_LOSSES = "update_losses"
    }
}