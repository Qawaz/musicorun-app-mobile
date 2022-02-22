package com.musicorumapp.mobile.state.models

import android.util.Log
import androidx.lifecycle.*
import com.musicorumapp.mobile.Constants
import com.musicorumapp.mobile.LogTag
import com.musicorumapp.mobile.api.LastfmApi
import com.musicorumapp.mobile.api.models.User
import com.musicorumapp.mobile.authentication.AuthenticationPreferences
import com.musicorumapp.mobile.state.SessionState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthenticationViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val authPrefs: AuthenticationPreferences,
    private val sessionState: SessionState
): ViewModel() {
    private val _user = MutableLiveData<User?>(null)
    private val _authenticationValidationState = MutableLiveData(State.AUTHENTICATING)

    val user: LiveData<User?> = _user
    val authenticationValidationState: LiveData<State> = _authenticationValidationState

    fun setAuthenticationValidationState (state: State) {
        _authenticationValidationState.value = state
    }

    fun authenticateFromToken (token: String, showSnackBar: (String) -> Unit) {
        Log.i(LogTag, token)
        viewModelScope.launch {
            val res = LastfmApi.getAuthEndpoint().getSession(token)

            authPrefs.setLastfmSessionToken(res.key)
            fetchUser(showSnackBar)
        }
    }

    fun fetchUser (
        showSnackBar: (String) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val usr = LastfmApi.getUserEndpoint().getUserInfoFromToken(authPrefs.getLastfmSessionToken().orEmpty()).toUser()
                _user.value = usr
                sessionState.currentUser = usr
                _authenticationValidationState.value = State.LOGGED_IN
            } catch (e: Exception) {
                Log.e(Constants.LOG_TAG, e.toString())
                showSnackBar("Could not fetch user data!")
            }
        }
    }

    fun setUser(usr: User) {
        _user.value = usr
    }

    enum class State {
        NONE,
        LOGGED_OUT, // LOGIN SCREEN
        LOGGED_IN, // MAIN SCREEN LOADED
        AUTHENTICATING, // MAIN SCREEN LOGGING IN
    }
}