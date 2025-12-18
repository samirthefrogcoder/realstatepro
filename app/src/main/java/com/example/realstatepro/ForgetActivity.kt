package com.example.realstatepro


import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.realstatepro.repository.UserRepoImpl
import com.example.realstatepro.ui.theme.Gray
import com.example.realstatepro.viewmodel.UserViewModel


class ForgetActivity : ComponentActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ForgotPasswordScreen()
        }
    }
}

@Composable
fun ForgotPasswordScreen() {

    val userViewModel = remember { UserViewModel(UserRepoImpl()) }

    val context = LocalContext.current
    var email by remember { mutableStateOf("") }


    Scaffold { padding ->
        LazyColumn(
            modifier = Modifier.padding(padding)
                .fillMaxSize()
                .padding(5.dp)
        ) {
            item {

                Spacer(modifier = Modifier.height(30.dp))
                Text("Please enter your email.",
                    style = TextStyle(
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Normal,
                        color = Gray
                    ),
                    modifier = Modifier.padding(start = 30.dp)
                )
                Spacer(modifier = Modifier.height(5.dp))
                Column(
                    modifier = Modifier.fillMaxWidth().padding(30.dp)
                ) {
                    TextField(
                        value = email,
                        onValueChange = { email = it },
                        label = { Text("ABC@gmail.com") }
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Button (
                        onClick = {
                            userViewModel.forgetPassword(email) { success, message ->
                                Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                            }
                        }
                    ) {
                        Text("Send ")
                    }
                }

            }
        }
    }


}


@Preview
@Composable
fun PreviewForget(){
    ForgotPasswordScreen()
}