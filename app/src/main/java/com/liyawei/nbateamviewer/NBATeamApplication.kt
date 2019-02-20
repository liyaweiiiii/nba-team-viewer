package com.liyawei.nbateamviewer

import android.app.Application
import com.facebook.stetho.Stetho

class NBATeamApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        Stetho.initializeWithDefaults(this)
    }
}