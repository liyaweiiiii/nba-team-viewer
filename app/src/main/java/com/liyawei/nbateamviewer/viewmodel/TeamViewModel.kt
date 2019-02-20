package com.liyawei.nbateamviewer.viewmodel

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Transformations
import android.arch.lifecycle.ViewModel
import com.liyawei.nbateamviewer.data.DataRepository
import com.liyawei.nbateamviewer.model.Team
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

open class TeamViewModel(private val repository: DataRepository) : ViewModel(), CoroutineScope {

    /**
     * This is the job for all coroutines started by this ViewModel.
     *
     * Cancelling this job will cancel all coroutines started by this ViewModel.
     */
    private val viewModelJob = Job()

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + viewModelJob

    private val selectedTeams = mutableSetOf<Team>()

    /**
     * Cancel all coroutines when the ViewModel is cleared
     */
    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    open val teams: LiveData<List<Team>> = Transformations.map(repository.teams) { teams ->
        if (teams.isNullOrEmpty()) {
            loadTeams()
        } else {
            isLoading.value = false
        }
        return@map teams
    }

    private lateinit var isLoading: MutableLiveData<Boolean>

    open fun getIsLoading(): LiveData<Boolean> {
        if (!::isLoading.isInitialized) {
            isLoading = MutableLiveData()
            isLoading.value = true
        }
        return isLoading
    }

    private lateinit var showError: MutableLiveData<Boolean>

    open fun shouldShowError(): LiveData<Boolean> {
        if (!::showError.isInitialized) {
            showError = MutableLiveData()
            showError.value = false
        }
        return showError
    }

    private lateinit var showDeleteButton: MutableLiveData<Boolean>

    open fun shouldShowDeleteButton(): LiveData<Boolean> {
        if (!::showDeleteButton.isInitialized) {
            showDeleteButton = MutableLiveData()
            showDeleteButton.value = false
        }
        return showDeleteButton
    }

    open fun loadTeams() {
        launch {
            try {
                isLoading.value = true
                repository.refreshTeams()
            } catch (e: Exception) {
                showError.value = true
                e.printStackTrace()
            } finally {
                isLoading.value = false
            }
        }
    }

    fun onTeamLongPress(team: Team) {
        if (!selectedTeams.remove(team)) {
            selectedTeams.add(team)
        }

        updateDeleteButtonState()
    }

    private fun updateDeleteButtonState() {
        if (showDeleteButton.value != selectedTeams.isNotEmpty()) {
            showDeleteButton.value = selectedTeams.isNotEmpty()
        }
    }

    fun onDeleteClicked() {
        launch {
            repository.deleteTeams(selectedTeams)
            selectedTeams.clear()
            updateDeleteButtonState()
        }
    }
}