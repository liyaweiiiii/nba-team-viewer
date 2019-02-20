package com.liyawei.nbateamviewer.viewholders

import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.View
import com.liyawei.nbateamviewer.model.Team
import com.liyawei.nbateamviewer.adapters.TeamDiffCallback
import kotlinx.android.synthetic.main.viewholder_team_item.view.*

class TeamViewHolder(itemView: View, delegate: ClickDelegate) : RecyclerView.ViewHolder(itemView) {
    init {
        itemView.setOnLongClickListener {
            delegate.onTeamLongPress(adapterPosition)
            return@setOnLongClickListener true
        }
    }

    fun onBind(team: Team) {
        itemView.setBackgroundColor(
            ContextCompat.getColor(
                itemView.context,
                if (team.selected) android.R.color.darker_gray else android.R.color.white
            )
        )
        itemView.team_name.text = team.fullName
        itemView.wins.text = team.wins.toString()
        itemView.loses.text = team.losses.toString()
    }

    fun onBind(payload: Any) {
        (payload as? Bundle)?.let { bundle ->
            if (bundle.containsKey(TeamDiffCallback.UPDATE_NAME))
                itemView.team_name.text = bundle.getString(TeamDiffCallback.UPDATE_NAME)

            if (bundle.containsKey(TeamDiffCallback.UPDATE_WINS))
                itemView.wins.text = bundle.getInt(TeamDiffCallback.UPDATE_WINS).toString()

            if (bundle.containsKey(TeamDiffCallback.UPDATE_LOSSES))
                itemView.loses.text = bundle.getInt(TeamDiffCallback.UPDATE_LOSSES).toString()
        }
    }

    interface ClickDelegate {
        fun onTeamLongPress(index: Int)
    }
}