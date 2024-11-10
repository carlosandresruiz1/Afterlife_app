package com.example.afterlife

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.afterlife.ui.theme.AfterlifeTheme
import androidx.compose.ui.Alignment

class ShoppingCartActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AfterlifeTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    bottomBar = {
                        BottomAppBar {
                            Button(
                                onClick = { navigateToProductList() },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text("Go to Product List")
                            }
                        }
                    }
                ) { innerPadding ->
                    ShoppingCartScreen(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }

    private fun navigateToProductList() {
        val intent = Intent(this, ProductListActivity::class.java)
        startActivity(intent)
    }
}

@Composable
fun ShoppingCartScreen(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Shopping Cart")
        // Add shopping cart items here
    }
}

@Preview(showBackground = true)
@Composable
fun ShoppingCartScreenPreview() {
    AfterlifeTheme {
        ShoppingCartScreen()
    }
}
