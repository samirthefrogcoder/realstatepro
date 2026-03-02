package com.example.realstatepro

import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.realstatepro.model.PropertyModel
import com.example.realstatepro.repository.PropertyRepoImpl
import com.example.realstatepro.ui.theme.*
import com.example.realstatepro.viewmodel.PropertyViewModel

class AddPropertyActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RealstateProTheme {
                AddPropertyScreen()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddPropertyScreen() {
    val context = LocalContext.current
    val viewModel = remember { PropertyViewModel(PropertyRepoImpl()) }
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }
    var location by remember { mutableStateOf("") }
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    var isUploading by remember { mutableStateOf(false) }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        imageUri = uri
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("List Your Property", fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = White)
            )
        },
        containerColor = White
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 28.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            Spacer(modifier = Modifier.height(8.dp))

            // Image Picker Section
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(OffWhite)
                    .clickable { launcher.launch("image/*") },
                contentAlignment = Alignment.Center
            ) {
                if (imageUri != null) {
                    AsyncImage(
                        model = imageUri,
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(Icons.Default.Add, contentDescription = null, tint = MediumGray, modifier = Modifier.size(40.dp))
                        Text("Add Property Photo", color = MediumGray, fontSize = 14.sp)
                    }
                }
            }

            ModernTextField(
                value = title,
                onValueChange = { title = it },
                label = "Property Title",
                placeholder = "e.g. Modern Villa"
            )

            ModernTextField(
                value = location,
                onValueChange = { location = it },
                label = "Location",
                placeholder = "e.g. Los Angeles, CA"
            )

            ModernTextField(
                value = price,
                onValueChange = { price = it },
                label = "Price ($)",
                placeholder = "e.g. 500,000"
            )

            ModernTextField(
                value = description,
                onValueChange = { description = it },
                label = "Description",
                placeholder = "Describe your property..."
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    if (title.isNotEmpty() && description.isNotEmpty() && price.isNotEmpty() && location.isNotEmpty()) {
                        isUploading = true
                        if (imageUri != null) {
                            viewModel.uploadImage(imageUri!!) { success, url ->
                                if (success) {
                                    val model = PropertyModel(
                                        title = title,
                                        description = description,
                                        price = price,
                                        location = location,
                                        imageUrl = url
                                    )
                                    viewModel.addProperty(model) { addSuccess, msg ->
                                        isUploading = false
                                        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
                                        if (addSuccess) (context as ComponentActivity).finish()
                                    }
                                } else {
                                    isUploading = false
                                    Toast.makeText(context, "Image upload failed: $url", Toast.LENGTH_SHORT).show()
                                }
                            }
                        } else {
                            val model = PropertyModel(
                                title = title,
                                description = description,
                                price = price,
                                location = location
                            )
                            viewModel.addProperty(model) { addSuccess, msg ->
                                isUploading = false
                                Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
                                if (addSuccess) (context as ComponentActivity).finish()
                            }
                        }
                    } else {
                        Toast.makeText(context, "Please fill all fields", Toast.LENGTH_SHORT).show()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(64.dp),
                shape = RoundedCornerShape(20.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Black),
                enabled = !isUploading
            ) {
                if (isUploading) {
                    CircularProgressIndicator(color = White, modifier = Modifier.size(24.dp))
                } else {
                    Text("Publish Property", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = White)
                }
            }
            
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}
