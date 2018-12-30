package com.liyawei.nbateamviewer.ui

import com.liyawei.nbateamviewer.model.Team

interface TeamListContract {
    interface View {
        fun showTeamList(teams: List<Team>)
        fun showError()
        fun showloading()
    }

    interface Presenter {
        fun loadTeams()
    }
}