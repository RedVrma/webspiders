package com.example.webspiders

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.room3.Room
import com.example.webspiders.data.AppDatabase
import com.example.webspiders.data.repository.UserRepositoryImpl
import com.example.webspiders.domain.repositories.UserRepository
import com.example.webspiders.network.client.NetworkModule
import com.example.webspiders.network.service.UserNetworkService
import com.example.webspiders.ui.screens.userList.view.UsersAppNavHost
import com.example.webspiders.ui.screens.userList.viewModel.UserListViewModel
import com.example.webspiders.ui.theme.WebspidersTheme

class MainActivity : ComponentActivity() {

    private val db by lazy {
        Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "users_db"
        ).build()
    }

    private val repository : UserRepository by lazy {
        val client = NetworkModule.provideNetworkClient(useFakeJson = true)
        val networkService = UserNetworkService(client)
        UserRepositoryImpl(
            networkDataSource = networkService,
            db.userDao()
        )
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            WebspidersTheme {
                val listViewModel: UserListViewModel = viewModel(
                    factory = object : ViewModelProvider.Factory{
                        @Suppress("UNCHECKED_CAST")
                        override fun <T : ViewModel> create(modelClass: Class<T>): T {
                            return UserListViewModel(repository) as T
                        }
                    }
                )

                UsersAppNavHost(
                    listViewModel = listViewModel,
                )
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    WebspidersTheme {
        Greeting("Android")
    }
}