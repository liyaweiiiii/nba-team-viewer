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
import kotlin.coroutines.CoroutineContext

class TeamViewModel : ViewModel(), CoroutineScope {

    /**
     * This is the job for all coroutines started by this ViewModel.
     *
     * Cancelling this job will cancel all coroutines started by this ViewModel.
     */
    private val viewModelJob = Job()

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + viewModelJob

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

    private lateinit var isLoading: MutableLiveData<Boolean>

    fun getIsLoading(): LiveData<Boolean> {
        if (!::isLoading.isInitialized) {
            isLoading = MutableLiveData()
        }
        return isLoading
    }

    private lateinit var showError: MutableLiveData<Boolean>

    fun shouldShowError(): LiveData<Boolean> {
        if (!::showError.isInitialized) {
            showError = MutableLiveData()
        }
        return showError
    }

    private fun loadTeams() {
        launch {
            try {
                isLoading.value = true
                val result = NetworkClient.getTeams(Dispatchers.IO)
                result.await()?.let {
                    showError.value = false
                    teams.value = it
                }
            } catch (e: Exception) {
                showError.value = true
            } finally {
                isLoading.value = false
            }
        }

    }
}