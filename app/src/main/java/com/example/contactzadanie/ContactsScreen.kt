package com.example.contactzadanie

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContactsApp(viewModel: ContactViewModel) {
    var name by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var editingContact by remember { mutableStateOf<Contact?>(null) }
    var isFormVisible by remember { mutableStateOf(false) }

    val contacts by viewModel.contacts.collectAsState(initial = listOf())
    val error by viewModel.errorState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    LaunchedEffect(error) {
        error?.let {
            scope.launch {
                snackbarHostState.showSnackbar(
                    message = it,
                    duration = SnackbarDuration.Short
                )
                viewModel.clearError()
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Контакты") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = colorResource(R.color.primary_dog),
                    titleContentColor = Color.White
                )
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = colorResource(R.color.background_dog),
        floatingActionButton = {
            FloatingActionButton(
                onClick = { isFormVisible = !isFormVisible },
                containerColor = colorResource(R.color.primary_dog),
                contentColor = Color.White,
                shape = RoundedCornerShape(16.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add Contact"
                )
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            if (isFormVisible || editingContact != null) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = colorResource(R.color.surface_dog)
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .padding(16.dp)
                    ) {
                        Text(
                            text = if (editingContact != null) "Edit Contact" else "New Contact",
                            style = MaterialTheme.typography.titleLarge,
                            color = colorResource(R.color.primary_dog),
                            modifier = Modifier.padding(bottom = 16.dp)
                        )

                        OutlinedTextField(
                            value = name,
                            onValueChange = { name = it },
                            label = { Text("Имя") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 8.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = colorResource(R.color.primary_dog),
                                unfocusedBorderColor = Color.Gray,
                                focusedLabelColor = colorResource(R.color.primary_dog),
                                cursorColor = colorResource(R.color.primary_dog)
                            ),
                            shape = RoundedCornerShape(12.dp),
                            isError = error?.contains("name", ignoreCase = true) == true
                        )

                        OutlinedTextField(
                            value = phoneNumber,
                            onValueChange = { phoneNumber = it },
                            label = { Text("Номер телефона") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 8.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = colorResource(R.color.primary_dog),
                                unfocusedBorderColor = Color.Gray,
                                focusedLabelColor = colorResource(R.color.primary_dog),
                                cursorColor = colorResource(R.color.primary_dog)
                            ),
                            shape = RoundedCornerShape(12.dp),
                            isError = error?.contains("phone", ignoreCase = true) == true
                        )

                        OutlinedTextField(
                            value = email,
                            onValueChange = { email = it },
                            label = { Text("Почта") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 16.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = colorResource(R.color.primary_dog),
                                unfocusedBorderColor = Color.Gray,
                                focusedLabelColor = colorResource(R.color.primary_dog),
                                cursorColor = colorResource(R.color.primary_dog)
                            ),
                            shape = RoundedCornerShape(12.dp),
                            isError = error?.contains("email", ignoreCase = true) == true
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Button(
                                onClick = {
                                    scope.launch {
                                        try {
                                            if (editingContact != null) {
                                                viewModel.updateContact(
                                                    editingContact!!.copy(
                                                        name = name,
                                                        phoneNumber = phoneNumber,
                                                        email = email
                                                    )
                                                )
                                                editingContact = null
                                            } else {
                                                viewModel.addContact(name, phoneNumber, email)
                                            }
                                            name = ""
                                            phoneNumber = ""
                                            email = ""
                                            isFormVisible = false
                                        } catch (e: Exception) {
                                            snackbarHostState.showSnackbar(
                                                message = "Error: ${e.message}",
                                                duration = SnackbarDuration.Short
                                            )
                                        }
                                    }
                                },
                                modifier = Modifier.weight(1f),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = colorResource(R.color.primary_dog)
                                ),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Text(if (editingContact != null) "Update Contact" else "Add Contact")
                            }

                            Button(
                                onClick = {
                                    name = ""
                                    phoneNumber = ""
                                    email = ""
                                    editingContact = null
                                    isFormVisible = false
                                },
                                modifier = Modifier.weight(1f),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color.Gray
                                ),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Text("Cancel")
                            }
                        }
                    }
                }
            }

            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(
                    items = contacts,
                    key = { contact -> contact.id }
                ) { contact ->
                    ContactItem(
                        contact = contact,
                        onEdit = {
                            name = contact.name
                            phoneNumber = contact.phoneNumber
                            email = contact.email
                            editingContact = contact
                            isFormVisible = true
                        },
                        onDelete = {
                            scope.launch {
                                try {
                                    viewModel.deleteContact(contact)
                                } catch (e: Exception) {
                                    snackbarHostState.showSnackbar(
                                        message = "Error deleting contact: ${e.message}",
                                        duration = SnackbarDuration.Short
                                    )
                                }
                            }
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun ContactItem(
    contact: Contact,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(
            containerColor = colorResource(R.color.surface_dog)
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = contact.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = colorResource(R.color.text_dog)
                )
                Text(
                    text = contact.phoneNumber,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray
                )
                Text(
                    text = contact.email,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray
                )
            }
            Row {
                IconButton(
                    onClick = onEdit,
                    colors = IconButtonDefaults.iconButtonColors(
                        contentColor = colorResource(R.color.primary_dog)
                    )
                ) {
                    Icon(Icons.Default.Edit, contentDescription = "Edit")
                }
                IconButton(
                    onClick = onDelete,
                    colors = IconButtonDefaults.iconButtonColors(
                        contentColor = Color.Red
                    )
                ) {
                    Icon(Icons.Default.Delete, contentDescription = "Delete")
                }
            }
        }
    }
}
