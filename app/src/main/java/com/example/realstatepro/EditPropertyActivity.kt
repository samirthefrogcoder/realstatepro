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
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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

class EditPropertyActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val propertyId = intent.getStringExtra("PROPERTY_ID") ?: ""
        val initialTitle = intent.getStringExtra("TITLE") ?: ""
        val initialDesc = intent.getStringExtra("DESC") ?: ""
        val initialPrice = intent.getStringExtra("PRICE") ?: ""
        val initialLocation = intent.getStringExtra("LOCATION") ?: ""
        val initialImageUrl = intent.getStringExtra("IMAGE_URL") ?: ""

        setContent {
            RealstateProTheme {
                EditPropertyScreen(propertyId, initialTitle, initialDesc, initialPrice, initialLocation, initialImageUrl)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditPropertyScreen(id: String, initialTitle: String, initialDesc: String, initialPrice: String, initialLocation: String, initialImageUrl: String) {
    val context = LocalContext.current
    val viewModel = remember { PropertyViewModel(PropertyRepoImpl()) }
    var title by remember { mutableStateOf(initialTitle) }
    var description by remember { mutableStateOf(initialDesc) }
    var price by remember { mutableStateOf(initialPrice) }
    var location by remember { mutableStateOf(initialLocation) }
    var imageUrl by remember { mutableStateOf(initialImageUrl) }
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
                title = { Text("Edit Property", fontWeight = FontWeight.Bold) },
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
                } else if (imageUrl.isNotEmpty()) {
                    AsyncImage(
                        model = imageUrl,
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Text("Tap to change photo", color = MediumGray)
                }
            }

            ModernTextField(
                value = title,
                onValueChange = { title = it },
                label = "Property Title",
                placeholder = "Title"
            )

            ModernTextField(
                value = location,
                onValueChange = { location = it },
                label = "Location",
                placeholder = "Location"
            )

            ModernTextField(
                value = price,
                onValueChange = { price = it },
                label = "Price",
                placeholder = "Price"
            )

            ModernTextField(
                value = description,
                onValueChange = { description = it },
                label = "Description",
                placeholder = "Description"
            )

            Button(
                onClick = {
                    if (title.isNotEmpty() && description.isNotEmpty() && price.isNotEmpty() && location.isNotEmpty()) {
                        isUploading = true
                        if (imageUri != null) {
                            viewModel.uploadImage(imageUri!!) { success, newUrl ->
                                if (success) {
                                    val model = PropertyModel(
                                        id = id,
                                        title = title,
                                        description = description,
                                        price = price,
                                        location = location,
                                        imageUrl = newUrl
                                    )
                                    viewModel.updateProperty(id, model)
                                    isUploading = false
                                    Toast.makeText(context, "Property Updated", Toast.LENGTH_SHORT).show()
                                    (context as ComponentActivity).finish()
                                } else {
                                    isUploading = false
                                    Toast.makeText(context, "Upload failed", Toast.LENGTH_SHORT).show()
                                }
                            }
                        } else {
                            val model = PropertyModel(
                                id = id,
                                title = title,
                                description = description,
                                price = price,
                                location = location,
                                imageUrl = imageUrl
                            )
                            viewModel.updateProperty(id, model)
                            isUploading = false
                            Toast.makeText(context, "Property Updated", Toast.LENGTH_SHORT).show()
                            (context as ComponentActivity).finish()
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
                    Text("Save Changes", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = White)
                }
            }
            
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}
