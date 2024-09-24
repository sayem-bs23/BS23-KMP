package com.jetbrains.bs23_kmp.screens.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.jetbrains.bs23_kmp.core.base.widget.BaseViewState
import com.jetbrains.bs23_kmp.core.base.widget.EmptyView
import com.jetbrains.bs23_kmp.core.base.widget.LoadingView
import com.jetbrains.bs23_kmp.screens.auth.LoginEvent
import com.jetbrains.bs23_kmp.screens.auth.LoginUiState
import com.jetbrains.bs23_kmp.screens.auth.LoginViewModel

@Composable
fun HomeScreen(navController: NavController, loginViewModel: LoginViewModel) {
    val uiState by loginViewModel.uiState.collectAsState()

    HomeScreenBody(
        modifier = Modifier,
        uiState = uiState,
        onEvent = { loginViewModel.onTriggerEvent(it) },
        navController)

}

@Composable
fun HomeScreenBody(
    modifier: Modifier = Modifier,
    uiState: BaseViewState<*>,
    onEvent: (LoginEvent) -> Unit,
    navController: NavController,
) {
    when (uiState) {
        is BaseViewState.Data -> {
            val state = uiState.value as? LoginUiState

            if (state != null) {
                HomeScreenContent(
                    modifier.fillMaxSize().padding(16.dp),
                    state,
                    onEvent,
                    navController,
                )
            }
        }

        BaseViewState.Empty -> EmptyView()
        is BaseViewState.Error -> Text("Error ${uiState.throwable.message}")
        BaseViewState.Loading -> LoadingView()
    }

}


@Composable
fun HomeScreenContent(
    modifier: Modifier = Modifier,
    homeState: LoginUiState,
    onEvent: (LoginEvent) -> Unit,
    navController: NavController
) {

    Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.Start) {
        Text("Login Successful", color = Color.Green.copy(alpha = 0.5f))
        Text("Logged In auth.User ID: ")
        Text("${homeState.currentUser?.id}")
        Text("Email: ${homeState.email}")

        TextButton(contentPadding = PaddingValues(0.dp), onClick = {
            onEvent(LoginEvent.Logout)
        }) {
            Text("Log Out")
        }
    }

}