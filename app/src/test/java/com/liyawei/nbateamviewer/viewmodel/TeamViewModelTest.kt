package com.liyawei.nbateamviewer.viewmodel

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import com.liyawei.nbateamviewer.data.DataRepository
import com.liyawei.nbateamviewer.model.Team
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.Spy
import org.mockito.junit.MockitoJUnit
import org.mockito.junit.MockitoRule
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(manifest=Config.NONE)
class TeamViewModelTest {
    private lateinit var viewModel: TeamViewModel

    private lateinit var isLoadingLiveData: LiveData<Boolean>

    private lateinit var isErrorLiveData: LiveData<Boolean>

    private val teams = listOf(
        Team(0, "Lakers", 82, 0),
        Team(1, "Celtics", 0, 82)
    )

    @Spy
    private val teamListLiveData: MutableLiveData<List<Team>> = MutableLiveData()

    @Mock
    private lateinit var repo: DataRepository

    @Rule
    @JvmField
    val mockitoRule: MockitoRule = MockitoJUnit.rule()

    @Before
    fun setUp() {
        `when`(repo.teams).thenReturn(teamListLiveData)
        viewModel = TeamViewModel(repo)

        isLoadingLiveData = viewModel.getIsLoading()
        isErrorLiveData = viewModel.shouldShowError()
    }

    @Test
    fun loadTeamsShouldShowAndHideLoading() = runBlocking {
        var isLoading = isLoadingLiveData.value
        assertNotNull(isLoading)
        isLoading?.let { assertTrue(it) }
        viewModel.loadTeams()
        verify(repo).refreshTeams()
        isLoading = isLoadingLiveData.value
        assertNotNull(isLoading)
        isLoading?.let { assertFalse(it) }
        return@runBlocking
    }

    @Test
    fun `isError is true when repo throws exception`() = runBlocking {
        var isLoading = isLoadingLiveData.value
        assertNotNull(isLoading)
        isLoading?.let { assertTrue(it) }

        var isError = isErrorLiveData.value
        assertNotNull(isError)
        isError?.let { assertFalse(it) }

        `when`(repo.refreshTeams()).thenAnswer { throw Exception() }
        viewModel.loadTeams()
        verify(repo).refreshTeams()

        isError = isErrorLiveData.value
        assertNotNull(isError)
        isError?.let { assertTrue(it) }

        isLoading = isLoadingLiveData.value
        assertNotNull(isLoading)
        isLoading?.let { assertFalse(it) }
        return@runBlocking
    }

    @Test
    fun `call refreshTeams to get teams if not locally present`() = runBlocking {
        teamListLiveData.value = listOf()

        viewModel.teams.observeForever { }

        verify(repo).refreshTeams()

        val isLoading = isLoadingLiveData.value
        assertNotNull(isLoading)
        isLoading?.let { assertFalse(it) }

        val isError = isErrorLiveData.value
        assertNotNull(isError)
        isError?.let { assertFalse(it) }
        return@runBlocking
    }

    @Test
    fun `retrieves local teams without calling refreshTeams`() = runBlocking {
        teamListLiveData.value = teams

        viewModel.teams.observeForever { }

        verify(repo, never()).refreshTeams()

        val isLoading = isLoadingLiveData.value
        assertNotNull(isLoading)
        isLoading?.let { assertFalse(it) }

        val isError = isErrorLiveData.value
        assertNotNull(isError)
        isError?.let { assertFalse(it) }
        return@runBlocking
    }
}