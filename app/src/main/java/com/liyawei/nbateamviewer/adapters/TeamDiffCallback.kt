package com.liyawei.nbateamviewer.adapters

import android.support.v7.util.DiffUtil
import com.liyawei.nbateamviewer.model.Team

class TeamDiffCallback(
    private val oldList: List<Team>,
    private val newList: List<Team>
) : DiffUtil.Callback() {
    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int) =
        oldList[oldItemPosition].id == newList[newItemPosition].id

    override fun getOldListSize() = oldList.size

    override fun getNewListSize() = newList.size

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int) =
        oldList[oldItemPosition] == newList[newItemPosition]
}