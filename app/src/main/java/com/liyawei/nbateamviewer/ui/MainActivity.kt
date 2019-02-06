package com.liyawei.nbateamviewer.ui

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import com.liyawei.nbateamviewer.R
import com.liyawei.nbateamviewer.data.DataRepository
import com.liyawei.nbateamviewer.data.getDatabase
import com.liyawei.nbateamviewer.databinding.ActivityMainBinding
import com.liyawei.nbateamviewer.model.Team
import com.liyawei.nbateamviewer.network.NetworkClient
import com.liyawei.nbateamviewer.viewmodel.TeamViewModel
import com.liyawei.nbateamviewer.viewmodel.TeamViewModelFactory
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.annotations.TestOnly

class MainActivity : AppCompatActivity() {
    private lateinit var viewModel: TeamViewModel
    private lateinit var binding: ActivityMainBinding

    private lateinit var teamsObserver: Observer<List<Team>>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.setLifecycleOwner(this)

        teams_list.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        teams_list.adapter = TeamAdapter()

        val repository = DataRepository(NetworkClient, getDatabase(this).teamDao)
        viewModel = ViewModelProviders.of(this, TeamViewModelFactory(repository)).get(TeamViewModel::class.java)
        initializeObservers()
    }

    override fun onStart() {
        super.onStart()
        binding.viewModel = viewModel
        registerObservers()
    }

    private fun initializeObservers() {
        teamsObserver = Observer { teams ->
            teams?.let {
                (teams_list.adapter as TeamAdapter).setTeamList(it)
            }
        }
    }

    private fun registerObservers() {
        viewModel.apply {
            teams.observe(this@MainActivity, teamsObserver)
        }
    }

    @TestOnly
    fun setTestViewModel(testViewModel: TeamViewModel) {
        viewModel = testViewModel
    }
}
