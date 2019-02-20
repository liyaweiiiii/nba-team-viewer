package com.liyawei.nbateamviewer.activities

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.liyawei.nbateamviewer.R
import com.liyawei.nbateamviewer.adapters.TeamAdapter
import com.liyawei.nbateamviewer.data.DataRepository
import com.liyawei.nbateamviewer.data.getDatabase
import com.liyawei.nbateamviewer.model.Team
import com.liyawei.nbateamviewer.network.NetworkClient
import com.liyawei.nbateamviewer.viewmodel.TeamViewModel
import com.liyawei.nbateamviewer.viewmodel.TeamViewModelFactory
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.annotations.TestOnly

class MainActivity : AppCompatActivity(), TeamAdapter.ClickDelegate {
    private lateinit var viewModel: TeamViewModel

    private lateinit var teamsObserver: Observer<List<Team>>
    private lateinit var isLoadingObserver: Observer<Boolean>
    private lateinit var isErrorObserver: Observer<Boolean>
    private lateinit var shouldShowDeleteObserver: Observer<Boolean>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        teams_list.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        teams_list.adapter = TeamAdapter(this)

        val repository = DataRepository(NetworkClient, getDatabase(this).teamDao)
        viewModel = ViewModelProviders.of(this, TeamViewModelFactory(repository)).get(TeamViewModel::class.java)
        initializeObservers()
    }

    override fun onStart() {
        super.onStart()
        registerObservers()
    }

    override fun onTeamLongPress(team: Team) {
        viewModel.onTeamLongPress(team)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menu?.let {
            menuInflater.inflate(R.menu.menu, it)
            it.findItem(R.id.delete).isVisible = viewModel.shouldShowDeleteButton().value == true
            return true
        }
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        item?.let {
            when(it.itemId) {
                R.id.delete -> {
                    viewModel.onDeleteClicked()
                    return true
                }
                else -> {}
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun initializeObservers() {
        teamsObserver = Observer { teams ->
            teams?.let {
                (teams_list.adapter as TeamAdapter).setTeamList(it)
                teams_list.visibility = View.VISIBLE
            }
        }

        isLoadingObserver = Observer { value ->
            value?.let { show ->
                if (show) {
                    loading_spinner.visibility = View.VISIBLE
                    teams_list.visibility = View.GONE
                    tv_error.visibility = View.GONE
                } else {
                    loading_spinner.visibility = View.GONE
                }
            }
        }

        isErrorObserver = Observer { value ->
            value?.let { show ->
                tv_error.visibility = if (show) View.VISIBLE else View.GONE
            }
        }

        shouldShowDeleteObserver = Observer { value ->
            value?.let { invalidateOptionsMenu() }
        }
    }

    private fun registerObservers() {
        viewModel.apply {
            teams.observe(this@MainActivity, teamsObserver)
            getIsLoading().observe(this@MainActivity, isLoadingObserver)
            shouldShowError().observe(this@MainActivity, isErrorObserver)
            shouldShowDeleteButton().observe(this@MainActivity, shouldShowDeleteObserver)
        }
    }

    @TestOnly
    fun setTestViewModel(testViewModel: TeamViewModel) {
        viewModel = testViewModel
    }
}
