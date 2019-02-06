package com.liyawei.nbateamviewer.viewholders

import android.support.v7.widget.RecyclerView
import android.view.View
import com.liyawei.nbateamviewer.model.Team

class TeamViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    fun onBind(team: Team) {
        itemView.team_name.text = team.fullName
        itemView.wins.text = team.wins.toString()
        itemView.loses.text = team.losses.toString()
    }

}