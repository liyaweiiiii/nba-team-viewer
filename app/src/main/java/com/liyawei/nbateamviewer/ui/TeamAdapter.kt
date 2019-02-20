package com.liyawei.nbateamviewer.ui

import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.liyawei.nbateamviewer.R
import com.liyawei.nbateamviewer.model.Team
import com.liyawei.nbateamviewer.ui.TeamDiffCallback.Companion.UPDATE_LOSSES
import com.liyawei.nbateamviewer.ui.TeamDiffCallback.Companion.UPDATE_NAME
import com.liyawei.nbateamviewer.ui.TeamDiffCallback.Companion.UPDATE_WINS
import kotlinx.android.synthetic.main.viewholder_team_item.view.*
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

    override fun onBindViewHolder(holder: TeamViewHolder, position: Int, payloads: MutableList<Any>) {
        if (payloads.isEmpty()) {
            holder.onBind(mTeamList[position])
        } else {
            for (payload in payloads) {
                holder.onBind(payload)
            }
        }
    }

    override fun onTeamLongPress(index: Int) {
        mTeamList[index].selected = !mTeamList[index].selected
        notifyItemChanged(index)
        delegate.onTeamLongPress(mTeamList[index])
    }

    fun setTeamList(teamList: List<Team>) {
        val diff = DiffUtil.calculateDiff(TeamDiffCallback(mTeamList, teamList))
        diff.dispatchUpdatesTo(this)
        mTeamList.clear()
        mTeamList.addAll(teamList)
    }

    @TestOnly
    fun getTeamList(): List<Team> = mTeamList

    interface ClickDelegate {
        fun onTeamLongPress(team: Team)
    }
}

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
            if (bundle.containsKey(UPDATE_NAME))
                itemView.team_name.text = bundle.getString(UPDATE_NAME)

            if (bundle.containsKey(UPDATE_WINS))
                itemView.wins.text = bundle.getInt(UPDATE_WINS).toString()

            if (bundle.containsKey(UPDATE_LOSSES))
                itemView.loses.text = bundle.getInt(UPDATE_LOSSES).toString()
        }
    }

    interface ClickDelegate {
        fun onTeamLongPress(index: Int)
    }
}

class TeamDiffCallback(private val oldList: List<Team>, private val newList: List<Team>) : DiffUtil.Callback() {
    companion object {
        const val UPDATE_NAME = "update_name"
        const val UPDATE_WINS = "update_wins"
        const val UPDATE_LOSSES = "update_losses"
    }
    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int) =
        oldList[oldItemPosition].id == newList[newItemPosition].id

    override fun getOldListSize() = oldList.size

    override fun getNewListSize() = newList.size

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int) =
        oldList[oldItemPosition] == newList[newItemPosition]

    override fun getChangePayload(oldItemPosition: Int, newItemPosition: Int): Any? {
        val oldItem = oldList[oldItemPosition]
        val newItem = newList[newItemPosition]

        val bundle = Bundle()
        if (oldItem.fullName != newItem.fullName)
            bundle.putString(UPDATE_NAME, newItem.fullName)

        if (oldItem.wins != newItem.wins)
            bundle.putInt(UPDATE_WINS, newItem.wins)

        if (oldItem.losses != newItem.losses)
            bundle.putInt(UPDATE_LOSSES, newItem.losses)

        return if (bundle.isEmpty) null else bundle
    }
}
