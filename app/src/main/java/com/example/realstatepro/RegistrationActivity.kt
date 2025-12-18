package com.example.realstatepro

import android.app.Activity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.realstatepro.model.UserModel
import com.example.realstatepro.repository.UserRepoImpl
import com.example.realstatepro.ui.theme.Blue
import com.example.realstatepro.viewmodel.UserViewModel

class RegistrationActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RegisterBody()
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
    var checkbox by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val activity = context as Activity

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = Color.White
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 24.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.Start
        ) {
            Spacer(modifier = Modifier.height(56.dp))

            // --- Header ---
            Text(
                text = "New Account",
                style = TextStyle(
                    fontSize = 34.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.Black
                )
            )
            Text(
                text = "Sign up to start searching for properties.",
                style = TextStyle(fontSize = 16.sp, color = Color.Gray),
                modifier = Modifier.padding(top = 4.dp)
            )

            Spacer(modifier = Modifier.height(40.dp))

            // --- Email Field ---
            Text("Email Address", fontWeight = FontWeight.SemiBold, fontSize = 14.sp, color = Color.Black)
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                placeholder = { Text("Enter your email", color = Color.LightGray) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Blue,
                    unfocusedBorderColor = Color.LightGray,
                    cursorColor = Blue
                )
            )

            Spacer(modifier = Modifier.height(24.dp))

            // --- Password Field ---
            Text("Password", fontWeight = FontWeight.SemiBold, fontSize = 14.sp, color = Color.Black)
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                visualTransformation = if (visibility) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    IconButton(onClick = { visibility = !visibility }) {
                        Icon(
                            painter = painterResource(
                                if (visibility) R.drawable.baseline_visibility_24
                                else R.drawable.baseline_visibility_off_24
                            ),
                            contentDescription = null,
                            tint = if (visibility) Blue else Color.Gray
                        )
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Blue,
                    unfocusedBorderColor = Color.LightGray
                )
            )

            Spacer(modifier = Modifier.height(24.dp))

            // --- Confirm Password Field ---
            Text("Confirm Password", fontWeight = FontWeight.SemiBold, fontSize = 14.sp, color = Color.Black)
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = reTypePassword,
                onValueChange = { reTypePassword = it },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Blue,
                    unfocusedBorderColor = Color.LightGray
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            // --- Terms Checkbox ---
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = checkbox,
                    onCheckedChange = { checkbox = it },
                    colors = CheckboxDefaults.colors(checkedColor = Blue)
                )
                Text(
                    "I agree to the Terms & Conditions",
                    fontSize = 14.sp,
                    color = Color.Black
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            // --- Submit Button ---
            Button(
                onClick = {
                    if (!checkbox) {
                        Toast.makeText(context, "Please accept terms & conditions", Toast.LENGTH_SHORT).show()
                    } else if (password != reTypePassword) {
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
                    .height(58.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Black)
            ) {
                Text("Sign Up", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White)
            }

            Spacer(modifier = Modifier.height(32.dp))

            // --- Login Redirect ---
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Text("Already have an account? ", color = Color.Gray)
                Text(
                    text = "Login",
                    modifier = Modifier.clickable { activity.finish() },
                    style = TextStyle(color = Blue, fontWeight = FontWeight.Bold)
                )
            }
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RegisterPreview() {
    RegisterBody()
}