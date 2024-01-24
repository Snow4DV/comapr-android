package ru.snowadv.comapr.presentation.screen.login

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import ru.snowadv.comapr.R
import ru.snowadv.comapr.presentation.view_model.MainViewModel


@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    mainViewModel: MainViewModel = hiltViewModel()
) {
    LaunchedEffect(true) {
        mainViewModel.authenticate()
    }

    AuthScreenContent(
        modifier = modifier,
        onLogin = { login, password -> mainViewModel.signIn(login, password)},
        onRegister = { email, login, password -> mainViewModel.signUp(email, login, password) }
    )
}

@Composable
fun AuthScreenContent(
    modifier: Modifier = Modifier,
    onLogin: (login: String, password: String) -> Unit,
    onRegister: (email: String, login: String, password: String) -> Unit
) {

    val emailValue = rememberSaveable { mutableStateOf("") }
    val loginValue = rememberSaveable { mutableStateOf("") }
    val passwordValue = rememberSaveable { mutableStateOf("") }

    val register = rememberSaveable { mutableStateOf(false) }

    val elementsPadding = 10.dp

    Scaffold(modifier = modifier) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier
                    .fillMaxHeight(0.2f)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    "C",
                    fontSize = 40.sp,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Medium
                )
                Text("omapr", fontSize = 40.sp)
            }
            AnimatedVisibility(visible = register.value) {
                OutlinedTextField(
                    singleLine = true,
                    label = { Text(stringResource(R.string.email)) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = elementsPadding),
                    value = emailValue.value,
                    onValueChange = { emailValue.value = it }
                )
            }
            OutlinedTextField(
                singleLine = true,
                label = { Text(stringResource(R.string.username)) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = elementsPadding),
                value = loginValue.value,
                onValueChange = { loginValue.value = it }
            )
            OutlinedTextField(
                singleLine = true,
                label = { Text(stringResource(R.string.password)) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = elementsPadding),
                value = passwordValue.value,
                onValueChange = { passwordValue.value = it }
            )
            AnimatedContent(targetState = register.value, label = "buttonTextAnimation") {
                ClickableText(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = elementsPadding),
                    text = AnnotatedString(
                        stringResource(
                            if (it) R.string.already_registered else R.string.don_t_have_an_account
                        )
                    ),
                    onClick = {
                        register.value = !register.value
                    },
                    style = TextStyle(
                        textAlign = TextAlign.End,
                        fontSize = 17.sp,
                        color = MaterialTheme.colorScheme.primary
                    )
                )
            }
            Button(
                onClick = {
                    if (register.value) {
                        onRegister(emailValue.value, loginValue.value, passwordValue.value)
                    } else {
                        onLogin(loginValue.value, passwordValue.value)
                    }
                }
            ) {
                AnimatedContent(targetState = register.value, label = "buttonTextAnimation") {
                    Text(text = if (it) "Register" else "Login")
                }
            }
        }
    }
}


@Preview
@Composable
fun LoginScreenPreview() {
    AuthScreenContent(
        modifier = Modifier
            .width(420.dp)
            .height(905.dp),
        onLogin = { login, password -> },
        onRegister = { email, login, password -> }
    )
}