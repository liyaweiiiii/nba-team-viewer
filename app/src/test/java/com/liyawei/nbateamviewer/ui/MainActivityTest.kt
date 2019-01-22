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
    fun hasVisibleLoadingViewOnCreate() {
        assertEquals(View.VISIBLE, activity.loading_spinner.visibility)
    }
}