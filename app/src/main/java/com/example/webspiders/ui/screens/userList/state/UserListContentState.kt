package com.example.webspiders.ui.screens.userList.state

import com.example.webspiders.domain.models.User

sealed interface UserListContentState {

    data object Loading : UserListContentState
    data class Success(val users : List<User>) : UserListContentState
    data object Empty : UserListContentState
    data class Error(val message : String) : UserListContentState
}

data class UserListUiState(
    val searchQuery : String = "",
    val contentState: UserListContentState = UserListContentState.Loading
)

