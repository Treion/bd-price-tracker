package com.bd.pricemonitor.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.lifecycle.viewmodel.compose.viewModel
import com.bd.pricemonitor.ui.viewmodel.AppViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import android.content.Intent
import android.net.Uri
import androidx.compose.ui.platform.LocalContext

@Composable
fun SplashRoute(onFinished: () -> Unit) {
    LaunchedEffect(Unit) {
        kotlinx.coroutines.delay(800)
        onFinished()
    }
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text("BD Price Monitor", style = MaterialTheme.typography.headlineMedium)
    }
}

@Composable
fun HomeRoute(navController: NavController) {
    val vm: AppViewModel = viewModel()
    val categories by vm.categories.collectAsState()
    Scaffold(topBar = {
        TopAppBar(title = { Text("Categories") }, actions = {
            IconButton(onClick = { navController.navigate("search") }) { Icon(Icons.Filled.Search, contentDescription = null) }
            IconButton(onClick = { navController.navigate("settings") }) { Icon(Icons.Filled.Settings, contentDescription = null) }
        })
    }) { padding ->
        LazyColumn(Modifier.padding(padding)) {
            items(categories) { item ->
                ListItem(
                    headlineContent = { Text(item.name) },
                    modifier = Modifier.clickable { navController.navigate("subcategory/${item.id}") }
                )
                Divider()
            }
        }
    }
}

@Composable
fun SubcategoryRoute(navController: NavController, categoryId: Long) {
    val vm: AppViewModel = viewModel()
    val subs by vm.subcategories(categoryId).collectAsState()
    Scaffold(topBar = { TopAppBar(title = { Text("Subcategories") }) }) { padding ->
        LazyColumn(Modifier.padding(padding)) {
            items(subs) { item ->
                ListItem(
                    headlineContent = { Text(item.name) },
                    modifier = Modifier.clickable { navController.navigate("products/${item.id}") }
                )
                Divider()
            }
        }
    }
}

@Composable
fun ProductsRoute(navController: NavController, subcategoryId: Long) {
    val vm: AppViewModel = viewModel()
    val products by vm.products(subcategoryId).collectAsState()
    Scaffold(topBar = { TopAppBar(title = { Text("Products") }) }) { padding ->
        LazyColumn(Modifier.padding(padding)) {
            items(products) { item ->
                ListItem(
                    headlineContent = { Text(item.name) },
                    supportingContent = { Text("BDT ${item.price} · ${item.unit}", maxLines = 1, overflow = TextOverflow.Ellipsis) },
                    modifier = Modifier.clickable { navController.navigate("product/${item.id}") }
                )
                Divider()
            }
        }
    }
}

@Composable
fun ProductDetailRoute(navController: NavController, productId: Long) {
    val vm: AppViewModel = viewModel()
    val product by vm.product(productId).collectAsState()
    val context = LocalContext.current
    Scaffold(topBar = { TopAppBar(title = { Text("Details") }) }) { padding ->
        Column(Modifier.padding(padding).padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            if (product == null) {
                Text("Loading…")
            } else {
                Text(product!!.name, style = MaterialTheme.typography.titleLarge)
                Text("Current Price: BDT ${product!!.price}", style = MaterialTheme.typography.bodyLarge)
                Text("Unit: ${product!!.unit}")
                Text("Gov Body: ${product!!.govBody}")
                Text("Last Updated: ${product!!.lastUpdated}")
                TextButton(onClick = {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(product!!.sourceUrl))
                    context.startActivity(intent)
                }) { Text("View Source") }
            }
        }
    }
}

@Composable
fun SearchRoute(navController: NavController) {
    var query by remember { mutableStateOf("") }
    val vm: AppViewModel = viewModel()
    val scope = rememberCoroutineScope()
    var results by remember { mutableStateOf(listOf<com.bd.pricemonitor.data.local.entity.ProductEntity>()) }
    LaunchedEffect(query) {
        if (query.isBlank()) {
            results = emptyList()
        } else {
            vm.search(query).collect { results = it }
        }
    }
    Scaffold(topBar = { TopAppBar(title = { Text("Search") }) }) { padding ->
        Column(Modifier.padding(padding)) {
            OutlinedTextField(value = query, onValueChange = { query = it }, modifier = Modifier.fillMaxWidth().padding(16.dp), placeholder = { Text("Search products") })
            LazyColumn {
                items(results) { item ->
                    ListItem(
                        headlineContent = { Text(item.name) },
                        supportingContent = { Text("BDT ${item.price} · ${item.unit}") },
                        modifier = Modifier.clickable { navController.navigate("product/${item.id}") }
                    )
                    Divider()
                }
            }
        }
    }
}

@Composable
fun SettingsRoute(navController: NavController) {
    val vm: AppViewModel = viewModel()
    val dark by vm.darkMode.collectAsState()
    val hours by vm.syncHours.collectAsState()
    Scaffold(topBar = { TopAppBar(title = { Text("Settings") }) }) { padding ->
        Column(Modifier.padding(padding)) {
            ListItem(
                headlineContent = { Text("Dark Mode") },
                trailingContent = { Switch(checked = dark, onCheckedChange = { vm.setDarkMode(it) }) }
            )
            Divider()
            Text("Update Frequency", style = MaterialTheme.typography.titleSmall, modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp))
            val options = listOf(12, 24, 168)
            options.forEach { h ->
                ListItem(
                    headlineContent = { Text(if (h == 12) "Every 12 hours" else if (h == 24) "Daily (24h)" else "Weekly (168h)") },
                    trailingContent = { RadioButton(selected = hours == h, onClick = { vm.setSyncHours(h) }) },
                    modifier = Modifier.clickable { vm.setSyncHours(h) }
                )
            }
        }
    }
}
