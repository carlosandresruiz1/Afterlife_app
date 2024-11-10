package com.example.afterlife

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.afterlife.ui.theme.AfterlifeTheme

class SuperUserActivity : ComponentActivity() {
    private lateinit var dbHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dbHelper = DatabaseHelper(this)
        setContent {
            AfterlifeTheme {
                SuperUserScreen(
                    onAddProduct = { product -> dbHelper.insertProduct(product) },
                    onAddUser = { user -> dbHelper.insertUser(user) }
                )
            }
        }
    }
}

@Composable
fun SuperUserScreen(onAddProduct: (Product) -> Unit, onAddUser: (User) -> Unit) {
    var selectedTab by remember { mutableIntStateOf(0) }
    val tabs = listOf("Products", "Users")
    val context = LocalContext.current

    Column(modifier = Modifier.fillMaxSize()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(
                onClick = {
                    context.startActivity(Intent(context, MainActivity::class.java))
                },
                modifier = Modifier.padding(8.dp)
            ) {
                Icon(Icons.Filled.ArrowBack, contentDescription = "Back to Main Activity")
                Spacer(modifier = Modifier.width(4.dp))
            }
            TabRow(selectedTabIndex = selectedTab, modifier = Modifier.weight(1f)) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTab == index,
                        onClick = { selectedTab = index },
                        text = { Text(title) }
                    )
                }
            }
        }
        when (selectedTab) {
            0 -> ProductTab(onAddProduct)
            1 -> UserTab(onAddUser)
        }
    }
}

@Composable
fun ProductTab(onAddProduct: (Product) -> Unit) {
    var name by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var message by remember { mutableStateOf("") }
    val dbHelper = DatabaseHelper(LocalContext.current)
    val products = dbHelper.getProducts()
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
        ) {
            products.forEach { product ->
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                        .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f))
                        .padding(16.dp)
                ) {
                    Text(
                        text = product.name,
                        style = MaterialTheme.typography.headlineMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = "Price: ${product.price}",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.secondary
                    )
                    Text(
                        text = "Description: ${product.description}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        Button(onClick = {
                            val intent = Intent(context, UpdateProductActivity::class.java)
                            intent.putExtra("PRODUCT_ID", product.id)
                            context.startActivity(intent)
                        }) {
                            Text("Edit")
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Button(onClick = {
                            dbHelper.deleteProduct(product.id)
                            context.startActivity(Intent(context, SuperUserActivity::class.java))
                        }) {
                            Text("Delete")
                        }
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text("Add Product")
            Spacer(modifier = Modifier.height(16.dp))
            TextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Product Name") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            TextField(
                value = price,
                onValueChange = { price = it },
                label = { Text("Product Price") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            TextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Product Description") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = {
                    val product = Product(0, name, price.toDouble(), description)
                    onAddProduct(product)
                    name = ""
                    price = ""
                    description = ""
                    message = "Product added successfully!"
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Add Product")
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(message)
        }
    }
}

@Composable
fun UserTab(onAddUser: (User) -> Unit) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var message by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Add User")
        Spacer(modifier = Modifier.height(16.dp))
        TextField(
            value = username,
            onValueChange = { username = it },
            label = { Text("Username") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        TextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
                val user = User(0, username, password)
                onAddUser(user)
                username = ""
                password = ""
                message = "User added successfully!"
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Add User")
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text(message)
    }
}

@Preview(showBackground = true)
@Composable
fun SuperUserScreenPreview() {
    AfterlifeTheme {
        SuperUserScreen(onAddProduct = {}, onAddUser = {})
    }
}