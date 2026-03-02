package com.example.realstatepro

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.realstatepro.ui.theme.*
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.delay

class SplashActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RealstateProTheme {
                SplashBody()
            }
        }
    }
}

@Composable
fun SplashBody() {
    val context = LocalContext.current
    val activity = context as? Activity

    // Luxury Fade-in Animation
    val alphaAnim = remember { Animatable(0f) }
    LaunchedEffect(Unit) {
        alphaAnim.animateTo(1f, animationSpec = tween(durationMillis = 1500))
        delay(1000)
        val currentUser = FirebaseAuth.getInstance().currentUser
        if (currentUser != null) {
            context.startActivity(Intent(context, DashboardActivity::class.java))
        } else {
            context.startActivity(Intent(context, LoginActivity::class.java))
        }
        activity?.finish()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(Black, DarkGray)
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.alpha(alphaAnim.value)
        ) {
            Text(
                text = "REAL ESTATE",
                fontSize = 36.sp,
                fontWeight = FontWeight.ExtraBold,
                color = PremiumGold,
                letterSpacing = 6.sp
            )
            Text(
                text = "PRO",
                fontSize = 18.sp,
                fontWeight = FontWeight.Light,
                color = White,
                letterSpacing = 10.sp
            )
            
            Spacer(modifier = Modifier.height(60.dp))
            
            CircularProgressIndicator(
                color = PremiumGold,
                strokeWidth = 2.dp,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewSplash() {
    RealstateProTheme(darkTheme = true) {
        SplashBody()
    }
}
