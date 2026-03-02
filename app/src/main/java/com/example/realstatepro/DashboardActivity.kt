package com.example.realstatepro

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.realstatepro.model.PropertyModel
import com.example.realstatepro.repository.PropertyRepoImpl
import com.example.realstatepro.ui.theme.*
import com.example.realstatepro.viewmodel.PropertyViewModel
import com.google.firebase.auth.FirebaseAuth

class DashboardActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RealstateProTheme {
                DashboardScreen()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen() {
    val context = LocalContext.current
    val viewModel = remember { PropertyViewModel(PropertyRepoImpl()) }
    val properties by viewModel.properties.observeAsState(initial = null)
    val loading by viewModel.loading.observeAsState(initial = false)

    var searchQuery by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("Real Estate") }
    var showProfileMenu by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.getAllProperties()
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = OffWhite,
        floatingActionButton = {
            FloatingActionButton(
                onClick = { context.startActivity(Intent(context, AddPropertyActivity::class.java)) },
                containerColor = Black,
                contentColor = PremiumGold,
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier
                    .padding(16.dp)
                    .size(64.dp)
                    .shadow(12.dp, RoundedCornerShape(20.dp), ambientColor = PremiumGold, spotColor = PremiumGold)
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add", modifier = Modifier.size(32.dp))
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            // --- Fancy Header Section ---
            Column(
                modifier = Modifier
                    .background(White)
                    .padding(horizontal = 28.dp, vertical = 20.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text("Current Location", fontSize = 12.sp, color = MediumGray, fontWeight = FontWeight.Medium)
                        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(top = 2.dp)) {
                            Icon(Icons.Default.LocationOn, contentDescription = null, modifier = Modifier.size(18.dp), tint = PremiumGold)
                            Text("New York, USA", fontSize = 16.sp, fontWeight = FontWeight.ExtraBold, color = Black)
                            Icon(Icons.Default.KeyboardArrowDown, contentDescription = null, tint = Black)
                        }
                    }
                    Box {
                        Surface(
                            modifier = Modifier
                                .size(52.dp)
                                .clickable { showProfileMenu = true },
                            shape = RoundedCornerShape(16.dp),
                            color = OffWhite,
                            border = BorderStroke(1.dp, LightGray)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(Icons.Default.Person, contentDescription = null, tint = Black)
                            }
                        }
                        
                        DropdownMenu(
                            expanded = showProfileMenu,
                            onDismissRequest = { showProfileMenu = false },
                            modifier = Modifier.background(White).padding(8.dp)
                        ) {
                            DropdownMenuItem(
                                text = { Text("Profile", fontWeight = FontWeight.Bold, color = Black) },
                                onClick = { showProfileMenu = false },
                                leadingIcon = { Icon(Icons.Default.Person, contentDescription = null, tint = Black) }
                            )
                            Divider(color = OffWhite, modifier = Modifier.padding(vertical = 4.dp))
                            DropdownMenuItem(
                                text = { Text("Logout", color = SoftRed, fontWeight = FontWeight.Bold) },
                                onClick = {
                                    showProfileMenu = false
                                    FirebaseAuth.getInstance().signOut()
                                    context.startActivity(Intent(context, LoginActivity::class.java).apply {
                                        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                    })
                                },
                                leadingIcon = { Icon(Icons.Default.ExitToApp, contentDescription = null, tint = SoftRed) }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(28.dp))

                // --- Search Bar with Fancy Filter ---
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        placeholder = { Text("Search properties...", color = LightGray) },
                        modifier = Modifier
                            .weight(1f)
                            .height(60.dp),
                        shape = RoundedCornerShape(20.dp),
                        leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = MediumGray) },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Black,
                            unfocusedBorderColor = Color.Transparent,
                            focusedContainerColor = OffWhite,
                            unfocusedContainerColor = OffWhite
                        )
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Surface(
                        modifier = Modifier
                            .size(60.dp)
                            .clickable { context.startActivity(Intent(context, FilterActivity::class.java)) },
                        shape = RoundedCornerShape(20.dp),
                        color = Black,
                        shadowElevation = 8.dp
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(Icons.Default.Settings, contentDescription = "Filter", tint = PremiumGold, modifier = Modifier.size(24.dp))
                        }
                    }
                }
            }

            // --- Category Section ---
            LazyRow(
                contentPadding = PaddingValues(horizontal = 28.dp, vertical = 20.dp),
                horizontalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                val categories = listOf("Real Estate", "Apartment", "House", "Motels", "Villa")
                items(categories) { category ->
                    CategoryChip(
                        title = category,
                        isSelected = selectedCategory == category,
                        onClick = { selectedCategory = category }
                    )
                }
            }

            // --- Listings Section ---
            if (loading && properties == null) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = PremiumGold, strokeWidth = 3.dp)
                }
            } else {
                val list = properties ?: emptyList()
                if (list.isEmpty()) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("No properties found", color = MediumGray, fontSize = 16.sp)
                            Spacer(modifier = Modifier.height(16.dp))
                            Button(
                                onClick = { viewModel.seedExampleData() },
                                colors = ButtonDefaults.buttonColors(containerColor = Black),
                                shape = RoundedCornerShape(16.dp)
                            ) {
                                Text("Load Premium Listings", color = PremiumGold)
                            }
                        }
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(bottom = 100.dp)
                    ) {
                        item {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 28.dp, vertical = 12.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.Bottom
                            ) {
                                Text(
                                    "Featured Nearby",
                                    fontSize = 22.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = Black,
                                    letterSpacing = (-0.5).sp
                                )
                                Text(
                                    "View all",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = PremiumGold
                                )
                            }
                        }
                        items(list) { property ->
                            ModernPropertyCard(
                                property = property,
                                onClick = { /* Details */ },
                                onEdit = {
                                    val intent = Intent(context, EditPropertyActivity::class.java).apply {
                                        putExtra("PROPERTY_ID", property.id)
                                        putExtra("TITLE", property.title)
                                        putExtra("DESC", property.description)
                                        putExtra("PRICE", property.price)
                                        putExtra("LOCATION", property.location)
                                        putExtra("IMAGE_URL", property.imageUrl)
                                    }
                                    context.startActivity(intent)
                                },
                                onDelete = { viewModel.deleteProperty(property.id) }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CategoryChip(title: String, isSelected: Boolean, onClick: () -> Unit) {
    Surface(
        modifier = Modifier.clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        color = if (isSelected) Black else White,
        border = if (isSelected) null else BorderStroke(1.dp, LightGray)
    ) {
        Text(
            text = title,
            modifier = Modifier.padding(horizontal = 22.dp, vertical = 12.dp),
            color = if (isSelected) PremiumGold else MediumGray,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
            fontSize = 14.sp
        )
    }
}

@Composable
fun ModernPropertyCard(
    property: PropertyModel,
    onClick: () -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 28.dp, vertical = 14.dp)
            .clickable { onClick() }
            .shadow(10.dp, RoundedCornerShape(28.dp), ambientColor = Color.LightGray),
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(containerColor = White),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column {
            // Image Area
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(230.dp)
                    .background(LightGray)
            ) {
                if (property.imageUrl.isNotEmpty()) {
                    AsyncImage(
                        model = property.imageUrl,
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }
                
                Surface(
                    modifier = Modifier
                        .padding(16.dp)
                        .align(Alignment.TopEnd),
                    shape = CircleShape,
                    color = White.copy(alpha = 0.9f)
                ) {
                    Icon(
                        Icons.Default.FavoriteBorder,
                        contentDescription = null,
                        modifier = Modifier
                            .padding(10.dp)
                            .size(20.dp),
                        tint = SoftRed
                    )
                }
                Surface(
                    modifier = Modifier
                        .padding(16.dp)
                        .align(Alignment.BottomStart),
                    shape = RoundedCornerShape(12.dp),
                    color = PremiumGold
                ) {
                    Text(
                        text = "PREMIUM",
                        color = Black,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Black,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                    )
                }
            }

            Column(modifier = Modifier.padding(24.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = property.title,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Black,
                            letterSpacing = (-0.5).sp
                        )
                        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(top = 4.dp)) {
                            Icon(Icons.Default.LocationOn, contentDescription = null, tint = PremiumGold, modifier = Modifier.size(14.dp))
                            Text(
                                text = property.location,
                                fontSize = 14.sp,
                                color = MediumGray,
                                modifier = Modifier.padding(start = 4.dp),
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                    Text(
                        text = "$${property.price}",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = PremiumGold
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))
                HorizontalDivider(color = OffWhite, thickness = 1.dp)
                Spacer(modifier = Modifier.height(20.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                        PropertyFeature(Icons.Default.Home, "3 Bed")
                        PropertyFeature(Icons.Default.Face, "2 Bath")
                        PropertyFeature(Icons.Default.Info, "1.2k ft²")
                    }
                    Row {
                        IconButton(onClick = onEdit, modifier = Modifier.size(40.dp)) {
                            Icon(Icons.Default.Edit, contentDescription = "Edit", tint = MediumGray, modifier = Modifier.size(20.dp))
                        }
                        IconButton(onClick = onDelete, modifier = Modifier.size(40.dp)) {
                            Icon(Icons.Default.Delete, contentDescription = "Delete", tint = SoftRed.copy(alpha = 0.7f), modifier = Modifier.size(20.dp))
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun PropertyFeature(icon: ImageVector, text: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(icon, contentDescription = null, modifier = Modifier.size(16.dp), tint = PremiumGold)
        Text(
            text = text,
            fontSize = 13.sp,
            color = MediumGray,
            modifier = Modifier.padding(start = 6.dp),
            fontWeight = FontWeight.Bold
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewDashboardRedesign() {
    RealstateProTheme {
        DashboardScreen()
    }
}
