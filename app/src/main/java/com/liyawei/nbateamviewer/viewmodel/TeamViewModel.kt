package com.liyawei.nbateamviewer.viewmodel

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.liyawei.nbateamviewer.model.Team
import com.liyawei.nbateamviewer.network.NetworkClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class TeamViewModel : ViewModel() {

    /**
     * This is the job for all coroutines started by this ViewModel.
     *
     * Cancelling this job will cancel all coroutines started by this ViewModel.
     */
    private val viewModelJob = Job()

    /**
     * This is the main scope for all coroutines launched by MainViewModel.
     *
     * Since we pass viewModelJob, you can cancel all coroutines launched by uiScope by calling
     * viewModelJob.cancel()
     */
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    /**
     * Cancel all coroutines when the ViewModel is cleared
     */
    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    private lateinit var teams: MutableLiveData<List<Team>>

    fun getTeams(): LiveData<List<Team>> {
        if (!::teams.isInitialized) {
            teams = MutableLiveData()
            loadTeams()
        }
        return teams
    }

    private val _isLoading = MutableLiveData<Boolean>()

    val isLoading: LiveData<Boolean>
        get() = _isLoading

    private val showError = MutableLiveData<Boolean>()

    fun shouldShowError(): LiveData<Boolean> {
        return showError
    }

    private fun loadTeams() {
        uiScope.launch {
            try {
                _isLoading.value = true
                val userCall = NetworkClient.getTeams(Dispatchers.IO)
                userCall.await()?.let {
                    showError.postValue(false)
                    teams.postValue(it)
                }
            } catch (e: Exception) {
                showError.postValue(true)
            } finally {
                _isLoading.value = false
            }
        }

    }
}