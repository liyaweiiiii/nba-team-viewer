package com.liyawei.nbateamviewer.ui

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.liyawei.nbateamviewer.R
import com.liyawei.nbateamviewer.viewmodel.TeamViewModel
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        teams_list.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        teams_list.adapter = TeamAdapter()

        val model = ViewModelProviders.of(this).get(TeamViewModel::class.java)

        subscribeUi(model)
    }

    private fun subscribeUi(viewModel: TeamViewModel) {
        viewModel.getTeams().observe(this, Observer { teams ->
            // update UI
            teams?.let {
                (teams_list.adapter as TeamAdapter).setTeamList(it)
            }
        })

        viewModel.getIsLoading().observe(this, Observer { value ->
            value?.let { show ->
                loading_spinner.visibility = if (show) View.VISIBLE else View.GONE
            }
        })

        viewModel.shouldShowError().observe(this, Observer { value ->
            value?.let { show ->
                tv_error.visibility = if (show) View.VISIBLE else View.GONE
            }
        })
    }
}
