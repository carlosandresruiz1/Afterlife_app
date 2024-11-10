package com.example.afterlife

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.afterlife.ui.theme.AfterlifeTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AfterlifeTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    LoginPage(
                        modifier = Modifier.padding(innerPadding),
                        onRegisterClick = { navigateToRegister() }
                    )
                }
            }
        }
    }

    private fun navigateToRegister() {
        val intent = Intent(this, RegisterActivity::class.java)
        startActivity(intent)
    }

    private fun navigateToProductList() {
        val intent = Intent(this, ProductListActivity::class.java)
        startActivity(intent)
    }

    private fun navigateToSuperUser() {
        val intent = Intent(this, SuperUserActivity::class.java)
        startActivity(intent)
    }
}

@Composable
fun LoginPage(modifier: Modifier = Modifier, onRegisterClick: () -> Unit) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var loginResult by remember { mutableStateOf("") }
    val context = LocalContext.current

    fun onLoginSuccess() {
        context.startActivity(Intent(context, ProductListActivity::class.java))
    }

    fun onSuperUserLoginSuccess() {
        context.startActivity(Intent(context, SuperUserActivity::class.java))
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TextField(
            value = username,
            onValueChange = { username = it },
            label = { Text("Username") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        TextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = PasswordVisualTransformation()
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
                if (username == "user" && password == "password") {
                    loginResult = "Login Successful"
                    onLoginSuccess()
                } else if (username == "super" && password == "password") {
                    loginResult = "Super User Login Successful"
                    onSuperUserLoginSuccess()
                } else {
                    loginResult = "Login Failed"
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Login")
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text(loginResult)
        Spacer(modifier = Modifier.height(16.dp))
        TextButton(
            onClick = onRegisterClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Register")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoginPagePreview() {
    AfterlifeTheme {
        LoginPage(onRegisterClick = {})
    }
}