package com.example.afterlife

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.* // Add this import
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.* // Add this import
import androidx.compose.ui.Alignment // Add this import
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.afterlife.ui.theme.AfterlifeTheme

class UpdateProductActivity : ComponentActivity() {
    private lateinit var dbHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dbHelper = DatabaseHelper(this)
        val productId = intent.getIntExtra("PRODUCT_ID", -1)
        val product = dbHelper.getProductById(productId)

        setContent {
            AfterlifeTheme {
                product?.let {
                    UpdateProductScreen(product = it, onUpdateProduct = { updatedProduct ->
                        dbHelper.updateProduct(updatedProduct)
                        startActivity(Intent(this, SuperUserActivity::class.java))
                    })
                }
            }
        }
    }
}

@Composable
fun UpdateProductScreen(product: Product, onUpdateProduct: (Product) -> Unit) {
    var name by remember { mutableStateOf(product.name) }
    var price by remember { mutableStateOf(product.price.toString()) }
    var description by remember { mutableStateOf(product.description) }
    var message by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Update Product", style = MaterialTheme.typography.headlineMedium)
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
                val updatedProduct = Product(product.id, name, price.toDouble(), description)
                onUpdateProduct(updatedProduct)
                message = "Product updated successfully!"
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Update Product")
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text(message)
    }
}

@Preview(showBackground = true)
@Composable
fun UpdateProductScreenPreview() {
    AfterlifeTheme {
        UpdateProductScreen(
            product = Product(1, "Product 1", 10.0, "Description 1"),
            onUpdateProduct = {}
        )
    }
}