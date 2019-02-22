package com.liyawei.nbateamviewer.adapters

import android.support.v7.recyclerview.extensions.ListAdapter
import android.view.LayoutInflater
import android.view.ViewGroup
import com.liyawei.nbateamviewer.R
import com.liyawei.nbateamviewer.model.Team
import com.liyawei.nbateamviewer.viewholders.TeamViewHolder

class TeamAdapter(private val delegate: ClickDelegate) : ListAdapter<Team, TeamViewHolder>(TeamDiffCallback()),
    TeamViewHolder.ClickDelegate {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TeamViewHolder {
        return TeamViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.viewholder_team_item, parent, false),
            this
        )
    }

    override fun onBindViewHolder(holder: TeamViewHolder, position: Int) {
        holder.onBind(getItem(position))
    }

    override fun onBindViewHolder(holder: TeamViewHolder, position: Int, payloads: MutableList<Any>) {
        if (payloads.isEmpty()) {
            holder.onBind(getItem(position))
        } else {
            for (payload in payloads) {
                holder.onBind(payload)
            }
        }
    }

    override fun onTeamLongPress(index: Int) {
        getItem(index).selected = !getItem(index).selected
        notifyItemChanged(index)
        delegate.onTeamLongPress(getItem(index))
    }

    fun setTeamList(teamList: List<Team>) {
        submitList(teamList)
    }

    interface ClickDelegate {
        fun onTeamLongPress(team: Team)
    }
}