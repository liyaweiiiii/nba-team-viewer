package com.liyawei.nbateamviewer.ui

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.liyawei.nbateamviewer.R
import com.liyawei.nbateamviewer.model.Team
import kotlinx.android.synthetic.main.viewholder_team_item.view.*

class TeamAdapter : RecyclerView.Adapter<TeamViewHolder>() {

    private var mTeamList = listOf<Team>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TeamViewHolder {
        return TeamViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.viewholder_team_item, parent, false))
    }

    override fun getItemCount() = mTeamList.size

    override fun onBindViewHolder(holder: TeamViewHolder, position: Int) {
        holder.onBind(mTeamList[position])
    }

    fun setTeamList(teamList: List<Team>) {
        mTeamList = teamList
        notifyDataSetChanged()
    }

}

class TeamViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    fun onBind(team: Team) {
        itemView.team_name.text = team.fullName
        itemView.wins.text = team.wins.toString()
        itemView.loses.text = team.losses.toString()
    }

}
