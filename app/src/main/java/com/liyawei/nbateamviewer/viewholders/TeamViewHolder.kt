package com.liyawei.nbateamviewer.viewholders

import android.support.v7.widget.RecyclerView
import com.liyawei.nbateamviewer.databinding.ViewholderTeamItemBinding
import com.liyawei.nbateamviewer.model.Team

class TeamViewHolder(private val binding: ViewholderTeamItemBinding) : RecyclerView.ViewHolder(binding.root) {
    fun onBind(team: Team) {
        binding.team = team
        binding.executePendingBindings()
    }

}