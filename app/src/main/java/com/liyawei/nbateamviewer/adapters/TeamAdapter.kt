package com.liyawei.nbateamviewer.adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.liyawei.nbateamviewer.R
import com.liyawei.nbateamviewer.model.Team
import com.liyawei.nbateamviewer.viewholders.TeamViewHolder
import org.jetbrains.annotations.TestOnly

class TeamAdapter(private val delegate: ClickDelegate) : RecyclerView.Adapter<TeamViewHolder>(),
    TeamViewHolder.ClickDelegate {
    private val mTeamList = mutableListOf<Team>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TeamViewHolder {
        return TeamViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.viewholder_team_item, parent, false),
            this
        )
    }

    override fun getItemCount() = mTeamList.size

    override fun onBindViewHolder(holder: TeamViewHolder, position: Int) {
        holder.onBind(mTeamList[position])
    }

    override fun onTeamLongPress(index: Int) {
        mTeamList[index].selected = !mTeamList[index].selected
        notifyItemChanged(index)
        delegate.onTeamLongPress(mTeamList[index])
    }

    fun setTeamList(teamList: List<Team>) {
        mTeamList.clear()
        mTeamList.addAll(teamList)
        notifyDataSetChanged()
    }

    @TestOnly
    fun getTeamList(): List<Team> = mTeamList

    interface ClickDelegate {
        fun onTeamLongPress(team: Team)
    }
}