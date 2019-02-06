package com.liyawei.nbateamviewer.extensions

import android.databinding.BindingAdapter
import android.support.v7.widget.RecyclerView
import com.liyawei.nbateamviewer.adapters.TeamAdapter
import com.liyawei.nbateamviewer.model.Team

@BindingAdapter("teamList")
fun setTeamList(view: RecyclerView, teamList: List<Team>?) {
    teamList?.let {
        (view.adapter as? TeamAdapter)?.setTeamList(it)
    }
}