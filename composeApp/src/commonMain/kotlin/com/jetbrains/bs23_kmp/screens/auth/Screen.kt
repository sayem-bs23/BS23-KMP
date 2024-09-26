package com.jetbrains.bs23_kmp.screens.auth

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.jetbrains.bs23_kmp.core.base.widget.BaseViewState
import com.jetbrains.bs23_kmp.core.base.widget.EmptyView
import com.jetbrains.bs23_kmp.core.base.widget.LoadingView
import org.jetbrains.compose.resources.ExperimentalResourceApi

@Composable
fun LoginScreen1(
    onLoginSuccess: () -> Unit,
    viewModel: LoginViewModel,
    navController: NavController,
) {

    val uiState by viewModel.uiState.collectAsState()

    LoginScreenBody(
        uiState = uiState,
        onEvent = { viewModel.onTriggerEvent(it) },
        navController = navController
    )

}

@Composable
fun LoginScreenBody(
    modifier: Modifier = Modifier,
    uiState: BaseViewState<*>,
    onEvent: (LoginEvent) -> Unit,
    navController: NavController
) {
    when (uiState) {
        is BaseViewState.Data -> {
            val state = uiState.value as? LoginUiState

            if (state != null) {
                LoginScreenContent(
                    modifier = Modifier.fillMaxSize().padding(16.dp),
                    uiState = state, onEvent = onEvent, navController = navController
                )
            }
        }

        BaseViewState.Empty -> EmptyView()
        is BaseViewState.Error -> Text("Error while Loading the state")
        BaseViewState.Loading -> LoadingView()
    }

}


@OptIn(ExperimentalResourceApi::class, ExperimentalMaterial3Api::class)
@Composable
fun LoginScreenContent(
    modifier: Modifier = Modifier,
    uiState: LoginUiState,
    onEvent: (LoginEvent) -> Unit,
    navController: NavController
) {

    Box(
        modifier = modifier.fillMaxWidth(),
    ) {
        LoginTitle(
            modifier = Modifier.fillMaxWidth().align(Alignment.TopStart).padding(top = 46.dp),
            title = "Welcome,",
            subtitle = "Sign in to continue!"
        )

        Column(
            modifier = Modifier.fillMaxWidth().align(Alignment.Center)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(24.dp),
            horizontalAlignment = Alignment.Start
        ) {
            LoginTextField(modifier = Modifier.fillMaxWidth().border(
                BorderStroke(
                    1.dp, Brush.linearGradient(
                        listOf(Color(0XFFfa568f), Color(0XFFfda78f))
                    )
                ), shape = RoundedCornerShape(8.dp)
            ), value = uiState.email,
                label = "Email",
                onValueChange = {
                    onEvent(LoginEvent.onEmailChange(it))
                },
                isError = uiState.emailError
            )

            if (uiState.emailError) {
                Text(
                    text = "Please enter a valid email address",
                    color = MaterialTheme.colorScheme.error
                )
            }
            LoginTextField(modifier = Modifier.fillMaxWidth().border(
                BorderStroke(
                    1.dp, Brush.linearGradient(
                        listOf(Color(0XFFfa568f), Color(0XFFfda78f))
                    )
                ), shape = RoundedCornerShape(8.dp)
            ), value = uiState.password,
                label = "Password",
                onValueChange = {
                    onEvent(LoginEvent.onPasswordChange(it))
                },
                isError = uiState.passwordError,
                visualTransformation = PasswordVisualTransformation()

            )

            if (uiState.passwordError) {
                Text(
                    text = "Please enter a valid password",
                    color = MaterialTheme.colorScheme.error
                )
            }


            if (uiState.isProcessing) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            } else {
                LoginButton(modifier =Modifier.fillMaxWidth(), onButtonClick = {
                    onEvent(LoginEvent.Login(uiState.email, uiState.password))
                }, buttonText = "Sign in")
            }

            if (uiState.loginErrorMessage.isNotEmpty()) {
                Text(text = uiState.loginErrorMessage, color = MaterialTheme.colorScheme.error)
            }

            Spacer(modifier = Modifier.height(16.dp))
            AnimatedVisibility(uiState.currentUser != null && !uiState.currentUser.isAnonymous) {
                navController.navigate("home")

            }
        }

        Row(
            modifier = Modifier.align(Alignment.BottomCenter).padding(bottom = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Don't have an account?", style = MaterialTheme.typography.titleMedium)
            TextButton(onClick = { navController.navigate("signup") }) {
                Text("Register", style = MaterialTheme.typography.titleMedium)
            }
        }


    }
}

@Composable
fun LoginTitle(modifier: Modifier = Modifier, title: String, subtitle: String) {
    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            title,
            style = MaterialTheme.typography.displayMedium
                .copy(fontWeight = FontWeight.Bold)
        )
        Text(
            subtitle,
            style = MaterialTheme.typography.displaySmall,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
        )
    }
}

@Composable
fun LoginTextField(
    modifier: Modifier = Modifier,
    value: String = "",
    visualTransformation: VisualTransformation = VisualTransformation.None,
    isError: Boolean = false,
    label: String = "",
    onValueChange: (String) -> Unit = {}

) {
    TextField(
        modifier = modifier.fillMaxWidth().border(
            BorderStroke(
                1.dp, Brush.linearGradient(
                    listOf(Color(0XFFfa568f), Color(0XFFfda78f))
                )
            ),
            shape = RoundedCornerShape(8.dp)
        ),
        value = value,
        visualTransformation = visualTransformation,
        isError = isError,
        label = {
            Text(label)
        },
        onValueChange = { onValueChange(it) },
        shape = RoundedCornerShape(8.dp),
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color.Transparent,
            unfocusedContainerColor = Color.Transparent,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledContainerColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent,
            errorIndicatorColor = Color.Transparent,
        )
    )
}
@Composable
fun LoginButton(modifier: Modifier =Modifier, onButtonClick:()->Unit,buttonText:String ="") {
    Button(
        modifier = modifier.fillMaxWidth().height(48.dp).background(
            Brush.linearGradient(
                listOf(Color(0XFFfa568f), Color(0XFFfda78f))
            ),
            RoundedCornerShape(8.dp)
        ),
        onClick = {
            onButtonClick()
        },
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Transparent,
        )
    )
    {
        Text(buttonText)
    }
}


