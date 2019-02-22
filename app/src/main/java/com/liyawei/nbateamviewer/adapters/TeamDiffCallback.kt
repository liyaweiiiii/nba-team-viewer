package com.liyawei.nbateamviewer.adapters

import android.os.Bundle
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

    override fun getChangePayload(oldItemPosition: Int, newItemPosition: Int): Any? {
        val oldItem = oldList[oldItemPosition]
        val newItem = newList[newItemPosition]

        val bundle = Bundle()
        if (newItem.fullName != oldItem.fullName)
            bundle.putString(UPDATE_NAME, newItem.fullName)

        if (newItem.wins != oldItem.wins)
            bundle.putInt(UPDATE_WINS, newItem.wins)

        if (newItem.losses != oldItem.losses)
            bundle.putInt(UPDATE_LOSSES, newItem.losses)

        return if (bundle.isEmpty) null else bundle
    }

    companion object {
        const val UPDATE_NAME = "update_name"
        const val UPDATE_WINS = "update_wins"
        const val UPDATE_LOSSES = "update_losses"
    }
}