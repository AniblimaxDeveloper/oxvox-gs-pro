package com.oxvox.app.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.oxvox.app.data.AuthViewModel
import com.oxvox.app.ui.theme.*

@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit,
    onNavigateToRegister: () -> Unit,
    viewModel: AuthViewModel = viewModel()
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.errorMessage.collectAsState()
    val isSuccess by viewModel.isSuccess.collectAsState()

    LaunchedEffect(isSuccess) {
        if (isSuccess) {
            viewModel.resetState()
            onLoginSuccess()
        }
    }

    HorrorGalaxyBackground {
        Column(
            modifier = Modifier.fillMaxSize().padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            GlitchText("OXVOX LOGIN", color = NeonCyan, fontSize = 32)
            Spacer(modifier = Modifier.height(32.dp))

            if (error != null) {
                Text(text = error!!, color = BloodRed, fontSize = 14.sp, fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace)
                Spacer(modifier = Modifier.height(16.dp))
            }

            OutlinedTextField(
                value = email, onValueChange = { email = it }, label = { Text("Email") },
                colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = BloodRed, unfocusedBorderColor = TextGray, focusedTextColor = TextWhite, unfocusedTextColor = TextWhite),
                modifier = Modifier.fillMaxWidth(), keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
            )
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = password, onValueChange = { password = it }, label = { Text("Password") },
                visualTransformation = PasswordVisualTransformation(),
                colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = BloodRed, unfocusedBorderColor = TextGray, focusedTextColor = TextWhite, unfocusedTextColor = TextWhite),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = { if (email.isNotBlank() && password.isNotBlank()) viewModel.login(email, password) },
                modifier = Modifier.fillMaxWidth().height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = ComicPurple),
                enabled = !isLoading
            ) {
                if (isLoading) CircularProgressIndicator(color = TextWhite, modifier = Modifier.size(24.dp))
                else Text("ACCESS COMMAND CENTER", color = TextWhite, fontWeight = FontWeight.Bold, fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace)
            }

            Spacer(modifier = Modifier.height(16.dp))
            TextButton(onClick = onNavigateToRegister) {
                Text("Don't have an account? Register", color = NeonCyan, fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace)
            }
        }
    }
}
