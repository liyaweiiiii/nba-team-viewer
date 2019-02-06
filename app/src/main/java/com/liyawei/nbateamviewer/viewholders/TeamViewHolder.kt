package com.liyawei.nbateamviewer.viewholders

import android.support.v7.widget.RecyclerView
import android.view.View
import com.liyawei.nbateamviewer.model.Team
import kotlinx.android.synthetic.main.viewholder_team_item.view.*

class TeamViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    fun onBind(team: Team) {
        itemView.team_name.text = team.fullName
        itemView.wins.text = team.wins.toString()
        itemView.loses.text = team.losses.toString()
    }

}