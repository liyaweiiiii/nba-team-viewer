package com.liyawei.nbateamviewer.adapters

import android.databinding.DataBindingUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.liyawei.nbateamviewer.R
import com.liyawei.nbateamviewer.model.Team
import com.liyawei.nbateamviewer.viewholders.TeamViewHolder
import org.jetbrains.annotations.TestOnly

class TeamAdapter : RecyclerView.Adapter<TeamViewHolder>() {

    private val mTeamList = mutableListOf<Team>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = TeamViewHolder(
        DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.viewholder_team_item,
            parent,
            false
        )
    )

    override fun getItemCount() = mTeamList.size

    override fun onBindViewHolder(holder: TeamViewHolder, position: Int) {
        holder.onBind(mTeamList[position])
    }

    fun setTeamList(teamList: List<Team>) {
        mTeamList.clear()
        mTeamList.addAll(teamList)
        notifyDataSetChanged()
    }

    @TestOnly
    fun getTeamList(): List<Team> = mTeamList
}