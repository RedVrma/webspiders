package com.example.webspiders.ui.screens.userList.view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.webspiders.domain.models.User
import com.example.webspiders.ui.screens.userList.state.UserListContentState
import com.example.webspiders.ui.screens.userList.viewModel.UserListViewModel

@Composable
fun UsersAppNavHost(
    listViewModel: UserListViewModel,
) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "user_list") {
        composable("user_list") {
            UserListScreen(
                viewModel = listViewModel,
                onUserClick = { userId ->
                    // Passes ONLY the userId, not the whole User object
                    //we can pass the userId like this and utilize this, in terms for time constraint i ak skipping thsi feature for now.
//                    navController.navigate("user_detail/$userId")
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserListScreen(
    viewModel: UserListViewModel,
    onUserClick: (Int) -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = { TopAppBar(title = { Text("Users") }) }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            // Search Box filtering by Name
            OutlinedTextField(
                value = uiState.searchQuery,
                onValueChange = viewModel::onSearchQueryChanged,
                label = { Text("Search by name") },
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            )

            // State rendering: Loading, Success, Empty, Error with Retry
            Box(modifier = Modifier.fillMaxSize()) {
                when (val state = uiState.contentState) {
                    is UserListContentState.Loading -> {
                        CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                    }

                    is UserListContentState.Empty -> {
                        Text(
                            text = "No users found.",
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }

                    is UserListContentState.Error -> {
                        Column(
                            modifier = Modifier.align(Alignment.Center),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(text = state.message, color = MaterialTheme.colorScheme.error)
                            Spacer(modifier = Modifier.height(12.dp))
                            Button(onClick = viewModel::refresh) {
                                Text("Retry")
                            }
                        }
                    }

                    is UserListContentState.Success -> {
                        LazyColumn(modifier = Modifier.fillMaxSize()) {
                            items(state.users, key = { it.id }) { user ->
                                UserRow(user = user, onClick = { onUserClick(user.id) })
                                HorizontalDivider()
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun UserRow(user: User, onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(16.dp)
    ) {
        Text(text = user.name, style = MaterialTheme.typography.titleMedium)
        Text(text = user.email, style = MaterialTheme.typography.bodyMedium)
        Text(
            text = user.companyName,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.secondary
        )
    }
}