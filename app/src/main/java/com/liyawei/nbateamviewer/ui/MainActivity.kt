package com.liyawei.nbateamviewer.ui

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.liyawei.nbateamviewer.R
import com.liyawei.nbateamviewer.model.Team
import com.liyawei.nbateamviewer.network.NetworkClient
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlin.coroutines.CoroutineContext

class MainActivity : AppCompatActivity(), TeamListContract.View, CoroutineScope {

    lateinit var job: Job

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    private lateinit var presenter: TeamListContract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        job = Job()
        setContentView(R.layout.activity_main)
        presenter = TeamListPresenter(this, NetworkClient, coroutineContext, Dispatchers.IO)

        teams_list.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        teams_list.adapter = TeamAdapter()
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }

    override fun onStart() {
        super.onStart()
        presenter.loadTeams()
    }

    override fun showTeamList(teams: List<Team>) {
        (teams_list.adapter as TeamAdapter).setTeamList(teams)
        loading_spinner.visibility = View.GONE
    }

    override fun showloading() {
        loading_spinner.visibility = View.VISIBLE
    }

    override fun showError() {
        tv_error.visibility = View.VISIBLE
        loading_spinner.visibility = View.GONE
    }
}
