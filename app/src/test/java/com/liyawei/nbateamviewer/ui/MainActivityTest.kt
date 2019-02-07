package com.liyawei.nbateamviewer.ui

import android.arch.lifecycle.MutableLiveData
import android.view.View
import com.liyawei.nbateamviewer.adapters.TeamAdapter
import com.liyawei.nbateamviewer.model.Team
import com.liyawei.nbateamviewer.viewmodel.TeamViewModel
import kotlinx.android.synthetic.main.activity_main.*
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnit
import org.mockito.junit.MockitoRule
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.android.controller.ActivityController

@RunWith(RobolectricTestRunner::class)
class MainActivityTest {

    private lateinit var activity: MainActivity

    private lateinit var activityController: ActivityController<MainActivity>

    @Mock
    private lateinit var viewModel: TeamViewModel

    private lateinit var teamsLiveData: MutableLiveData<List<Team>>

    private lateinit var isLoadingLiveData: MutableLiveData<Boolean>

    private lateinit var isErrorLiveData: MutableLiveData<Boolean>

    @Rule
    @JvmField
    val mockitoRule: MockitoRule = MockitoJUnit.rule()

    @Before
    fun setUp() {
        teamsLiveData = MutableLiveData()
        isLoadingLiveData = MutableLiveData()
        isLoadingLiveData.value = true
        isErrorLiveData = MutableLiveData()
        isErrorLiveData.value = false

        `when`(viewModel.teams).thenReturn(teamsLiveData)
        `when`(viewModel.getIsLoading()).thenReturn(isLoadingLiveData)
        `when`(viewModel.shouldShowError()).thenReturn(isErrorLiveData)

        activityController = Robolectric.buildActivity(MainActivity::class.java)
        activity = activityController.get()

        activityController.create()
        activity.setTestViewModel(viewModel)

        activityController.start()
    }

    @Test
    fun `has visible loading view on create`() {
        assertEquals(View.VISIBLE, activity.loading_spinner.visibility)
    }

    @Test
    fun `has hidden recycler view and error view on create`() {
        assertEquals(View.GONE, activity.teams_list.visibility)
        assertEquals(View.GONE, activity.tv_error.visibility)
    }

    @Test
    fun `displays teams list when available`() {
        val teamsList = listOf(Team(1, "Team1", 4, 2), Team(2, "Team2", 3, 5))
        teamsLiveData.value = teamsList
        isLoadingLiveData.value = false
        isErrorLiveData.value = false

        activity.binding.executePendingBindings()

        assertEquals(View.VISIBLE, activity.teams_list.visibility)
        assertEquals(View.GONE, activity.loading_spinner.visibility)
        assertEquals(View.GONE, activity.tv_error.visibility)

        assertEquals(teamsList, (activity.teams_list.adapter as? TeamAdapter)?.getTeamList())
    }

    @Test
    fun `displays error view when error obtaining teams`() {
        isLoadingLiveData.value = false
        isErrorLiveData.value = true

        activity.binding.executePendingBindings()

        assertEquals(View.GONE, activity.teams_list.visibility)
        assertEquals(View.GONE, activity.loading_spinner.visibility)
        assertEquals(View.VISIBLE, activity.tv_error.visibility)
    }
}