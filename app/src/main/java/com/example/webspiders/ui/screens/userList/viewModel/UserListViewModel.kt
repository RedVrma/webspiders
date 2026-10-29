package com.example.webspiders.ui.screens.userList.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.webspiders.domain.repositories.UserRepository
import com.example.webspiders.ui.screens.userList.state.UserListContentState
import com.example.webspiders.ui.screens.userList.state.UserListUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class UserListViewModel(
    private val repository: UserRepository
) : ViewModel() {

    private val searchQuery = MutableStateFlow("")
    private val isRefreshing = MutableStateFlow(false)
    private val syncError = MutableStateFlow<String?>(null)

    private val cachedUsersFlow = searchQuery.flatMapLatest { query ->
        repository.observeUsers(query)
    }

    val uiState: StateFlow<UserListUiState> = combine(
        searchQuery,
        cachedUsersFlow,
        isRefreshing,
        syncError
    ) { searchQuery, users, loading, error ->
        val contentState = when {
            loading && users.isEmpty() -> UserListContentState.Loading
            error != null && users.isEmpty() -> UserListContentState.Error(error)
            users.isEmpty() -> UserListContentState.Empty
            else -> UserListContentState.Success(users)
        }
        UserListUiState(
            searchQuery = searchQuery,
            contentState = contentState
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = UserListUiState()
    )

    init {
        refresh()
    }

    fun onSearchQueryChanged(newQuery: String) {
        searchQuery.value = newQuery
    }

    fun refresh() {
        viewModelScope.launch {
            isRefreshing.value = true
            syncError.value = null
            repository.refreshUsers().onFailure { throwable ->
                syncError.value = throwable.localizedMessage ?: "Failure to load"
            }
            isRefreshing.value = false
        }
    }

}