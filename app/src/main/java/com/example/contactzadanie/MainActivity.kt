package com.example.contactzadanie

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.contactzadanie.ContactViewModel.ContactViewModelFactory
import com.example.contactzadanie.ui.theme.ContactZadanieTheme

class MainActivity : ComponentActivity() {
    private val contactViewModel: ContactViewModel by viewModels {
        ContactViewModelFactory(
            (application as ContactApplication).database.contactDao()
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ContactZadanieTheme {
                ContactsApp(contactViewModel)
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ContactZadanieTheme {
        Greeting("Android")
    }
}