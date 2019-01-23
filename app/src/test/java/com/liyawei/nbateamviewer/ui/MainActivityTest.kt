package com.liyawei.nbateamviewer.ui

import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.Observer
import android.view.View
import com.liyawei.nbateamviewer.model.Team
import com.liyawei.nbateamviewer.viewmodel.TeamViewModel
import kotlinx.android.synthetic.main.activity_main.*
import org.junit.Before

import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentCaptor
import org.mockito.ArgumentMatchers
import org.mockito.Captor
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnit
import org.mockito.junit.MockitoRule
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.android.controller.ActivityController
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
class MainActivityTest {

    private lateinit var activity: MainActivity

    private lateinit var activityController: ActivityController<MainActivity>

    @Mock
    private lateinit var viewModel: TeamViewModel

    @Mock
    private lateinit var teamsLiveData: LiveData<List<Team>>

    @Mock
    private lateinit var isLoadingLiveData: LiveData<Boolean>

    @Mock
    private lateinit var isErrorLiveData: LiveData<Boolean>

    @Captor
    private lateinit var teamsObserverCaptor: ArgumentCaptor<Observer<List<Team>>>

    @Captor
    private lateinit var isLoadingObserverCaptor: ArgumentCaptor<Observer<Boolean>>

    @Captor
    private lateinit var isErrorObserverCaptor: ArgumentCaptor<Observer<Boolean>>

    @Rule
    @JvmField
    val mockitoRule: MockitoRule = MockitoJUnit.rule()

    @Before
    fun setUp() {
        `when`(viewModel.teams).thenReturn(teamsLiveData)
        `when`(viewModel.getIsLoading()).thenReturn(isLoadingLiveData)
        `when`(viewModel.shouldShowError()).thenReturn(isErrorLiveData)

        activityController = Robolectric.buildActivity(MainActivity::class.java)
        activity = activityController.get()

        activityController.create()
        activity.setTestViewModel(viewModel)

        activityController.start()
        verify(teamsLiveData).observe(ArgumentMatchers.any(LifecycleOwner::class.java), teamsObserverCaptor.capture())
        verify(isLoadingLiveData).observe(ArgumentMatchers.any(LifecycleOwner::class.java), isLoadingObserverCaptor.capture())
        verify(isErrorLiveData).observe(ArgumentMatchers.any(LifecycleOwner::class.java), isErrorObserverCaptor.capture())
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
        teamsObserverCaptor.value.onChanged(teamsList)
        isLoadingObserverCaptor.value.onChanged(false)
        isErrorObserverCaptor.value.onChanged(false)

        assertEquals(View.VISIBLE, activity.teams_list.visibility)
        assertEquals(View.GONE, activity.loading_spinner.visibility)
        assertEquals(View.GONE, activity.tv_error.visibility)

        assertEquals(teamsList, (activity.teams_list.adapter as? TeamAdapter)?.getTeamList())
    }

    @Test
    fun `displays error view when error obtaining teams`() {
        isLoadingObserverCaptor.value.onChanged(false)
        isErrorObserverCaptor.value.onChanged(true)

        assertEquals(View.GONE, activity.teams_list.visibility)
        assertEquals(View.GONE, activity.loading_spinner.visibility)
        assertEquals(View.VISIBLE, activity.tv_error.visibility)
    }
}