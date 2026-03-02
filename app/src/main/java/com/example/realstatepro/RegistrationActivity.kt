package com.example.realstatepro

import android.app.Activity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.realstatepro.model.UserModel
import com.example.realstatepro.repository.UserRepoImpl
import com.example.realstatepro.ui.theme.*
import com.example.realstatepro.viewmodel.UserViewModel

class RegistrationActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RealstateProTheme {
                RegisterBody()
            }
        }
    }
}

@Composable
fun RegisterBody() {
    val userViewModel = remember { UserViewModel(UserRepoImpl()) }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var reTypePassword by remember { mutableStateOf("") }
    var visibility by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val activity = context as Activity

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = White
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 28.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.Start
        ) {
            Spacer(modifier = Modifier.height(80.dp))

            Text(
                text = "Create\nNew Account",
                fontSize = 42.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Black,
                lineHeight = 50.sp,
                letterSpacing = (-1).sp
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Join our community and find the home you've always wanted.",
                fontSize = 16.sp,
                color = MediumGray
            )

            Spacer(modifier = Modifier.height(48.dp))

            ModernTextField(
                value = email,
                onValueChange = { email = it },
                label = "Email Address",
                placeholder = "Enter your email"
            )

            Spacer(modifier = Modifier.height(24.dp))

            ModernTextField(
                value = password,
                onValueChange = { password = it },
                label = "Password",
                placeholder = "••••••••",
                isPassword = true,
                visibility = visibility,
                onVisibilityChange = { visibility = it }
            )

            Spacer(modifier = Modifier.height(24.dp))

            ModernTextField(
                value = reTypePassword,
                onValueChange = { reTypePassword = it },
                label = "Confirm Password",
                placeholder = "••••••••",
                isPassword = true
            )

            Spacer(modifier = Modifier.height(56.dp))

            Button(
                onClick = {
                    if (password != reTypePassword) {
                        Toast.makeText(context, "Passwords do not match", Toast.LENGTH_SHORT).show()
                    } else {
                        userViewModel.register(email, password) { success, message, userId ->
                            if (success) {
                                val model = UserModel(id = userId, email = email)
                                userViewModel.addUserToDatabase(userId, model) { successDb, messageDb ->
                                    if (successDb) {
                                        Toast.makeText(context, messageDb, Toast.LENGTH_SHORT).show()
                                        activity.finish()
                                    } else {
                                        Toast.makeText(context, messageDb, Toast.LENGTH_SHORT).show()
                                    }
                                }
                            } else {
                                Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(64.dp),
                shape = RoundedCornerShape(20.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Black)
            ) {
                Text("Create Account", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = PremiumGold)
            }

            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewRegisterRedesign() {
    RealstateProTheme {
        RegisterBody()
    }
}
