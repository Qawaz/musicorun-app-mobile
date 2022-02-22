package com.musicorumapp.mobile.state

import android.content.Context
import com.musicorumapp.mobile.api.models.User
import javax.inject.Singleton

@Singleton
class SessionState (context: Context) {
    var currentUser: User? = null
}