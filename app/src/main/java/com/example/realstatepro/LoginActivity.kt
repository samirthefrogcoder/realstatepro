package com.example.realstatepro

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.realstatepro.repository.UserRepoImpl
import com.example.realstatepro.ui.theme.*
import com.example.realstatepro.viewmodel.UserViewModel

class LoginActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RealstateProTheme {
                LoginBody()
            }
        }
    }
}

@Composable
fun LoginBody() {
    val userViewModel = remember { UserViewModel(UserRepoImpl()) }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
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
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(80.dp))

            Text(
                text = "Discover Your\nDream Home",
                fontSize = 42.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Black,
                lineHeight = 50.sp,
                modifier = Modifier.fillMaxWidth(),
                letterSpacing = (-1).sp
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Login to access the most exclusive listings.",
                fontSize = 16.sp,
                color = MediumGray,
                modifier = Modifier.fillMaxWidth()
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

            Text(
                text = "Forgot Password?",
                modifier = Modifier
                    .align(Alignment.End)
                    .padding(top = 16.dp)
                    .clickable {
                        context.startActivity(Intent(context, ForgetActivity::class.java))
                    },
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = PremiumGold // Fancy gold for clickable text
            )

            Spacer(modifier = Modifier.height(48.dp))

            Button(
                onClick = {
                    userViewModel.login(email, password) { success, message ->
                        if (success) {
                            context.startActivity(Intent(context, DashboardActivity::class.java))
                            activity.finish()
                        } else {
                            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(64.dp),
                shape = RoundedCornerShape(20.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Black)
            ) {
                Text("Login", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = White)
            }

            Spacer(modifier = Modifier.height(20.dp))

            OutlinedButton(
                onClick = {
                    context.startActivity(Intent(context, RegistrationActivity::class.java))
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(64.dp),
                shape = RoundedCornerShape(20.dp),
                border = BorderStroke(1.5.dp, Black)
            ) {
                Text("Create Account", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Black)
            }

            Spacer(modifier = Modifier.height(56.dp))

            Text("Or continue with", fontSize = 14.sp, color = MediumGray, fontWeight = FontWeight.Medium)
            
            Spacer(modifier = Modifier.height(24.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                SocialIcon(R.drawable.baseline_visibility_24) 
                Spacer(modifier = Modifier.width(20.dp))
                SocialIcon(R.drawable.baseline_visibility_24)
                Spacer(modifier = Modifier.width(20.dp))
                SocialIcon(R.drawable.baseline_visibility_24)
            }

            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewLoginRedesign() {
    RealstateProTheme {
        LoginBody()
    }
}
