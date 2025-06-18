package com.uns.desfilesanpedritomanager.presentation.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.uns.desfilesanpedritomanager.domain.ParticipantCategory
import com.uns.desfilesanpedritomanager.domain.Person
import com.uns.desfilesanpedritomanager.domain.TypePerson
import com.uns.desfilesanpedritomanager.presentation.viewmodel.PersonViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    navigateToList: () -> Unit,
    viewModel: PersonViewModel
) {
    var name by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var age by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    var typeSelected by remember { mutableStateOf(TypePerson.STUDENT) }
    var categorySelected by remember { mutableStateOf(ParticipantCategory.SPECTATOR) }
    var showTypeDropdown by remember { mutableStateOf(false) }
    var showCategoryDropdown by remember { mutableStateOf(false) }

    val uiState by viewModel.uiState.collectAsState()

    // Observar mensajes del ViewModel
    LaunchedEffect(uiState.message) {
        if (uiState.message != null) {
            // Limpiar formulario después del registro exitoso
            name = ""
            lastName = ""
            email = ""
            phone = ""
            age = ""
            address = ""
            typeSelected = TypePerson.STUDENT
            categorySelected = ParticipantCategory.SPECTATOR
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            "🎭 Registro Desfile San Pedrito",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(vertical = 24.dp)
        )

        // Información personal
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
            )
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    "👤 Información Personal",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.primary
                )

                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Nombre *") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    isError = name.isBlank() && uiState.error != null
                )

                OutlinedTextField(
                    value = lastName,
                    onValueChange = { lastName = it },
                    label = { Text("Apellidos *") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    isError = lastName.isBlank() && uiState.error != null
                )

                OutlinedTextField(
                    value = age,
                    onValueChange = { if (it.all { char -> char.isDigit() }) age = it },
                    label = { Text("Edad *") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    isError = age.isBlank() && uiState.error != null
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Información de contacto
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
            )
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    "📞 Información de Contacto",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.primary
                )

                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Correo electrónico *") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                    isError = email.isBlank() && uiState.error != null
                )

                OutlinedTextField(
                    value = phone,
                    onValueChange = { phone = it },
                    label = { Text("Teléfono *") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                    isError = phone.isBlank() && uiState.error != null
                )

                OutlinedTextField(
                    value = address,
                    onValueChange = { address = it },
                    label = { Text("Dirección *") },
                    modifier = Modifier.fillMaxWidth(),
                    maxLines = 2,
                    isError = address.isBlank() && uiState.error != null
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Tipo y categoría
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
            )
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    "🎯 Tipo de Participación",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.primary
                )

                // Dropdown para tipo
                ExposedDropdownMenuBox(
                    expanded = showTypeDropdown,
                    onExpandedChange = { showTypeDropdown = !showTypeDropdown }
                ) {
                    OutlinedTextField(
                        value = typeSelected.displayName,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Tipo de Participante") },
                        trailingIcon = {
                            Icon(
                                imageVector = Icons.Default.KeyboardArrowDown,
                                contentDescription = null
                            )
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor()
                    )

                    ExposedDropdownMenu(
                        expanded = showTypeDropdown,
                        onDismissRequest = { showTypeDropdown = false }
                    ) {
                        TypePerson.values().forEach { type ->
                            DropdownMenuItem(
                                text = { Text(type.displayName) },
                                onClick = {
                                    typeSelected = type
                                    showTypeDropdown = false
                                }
                            )
                        }
                    }
                }

                // Dropdown para categoría
                ExposedDropdownMenuBox(
                    expanded = showCategoryDropdown,
                    onExpandedChange = { showCategoryDropdown = !showCategoryDropdown }
                ) {
                    OutlinedTextField(
                        value = categorySelected.displayName,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Categoría de Participación") },
                        trailingIcon = {
                            Icon(
                                imageVector = Icons.Default.KeyboardArrowDown,
                                contentDescription = null
                            )
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor()
                    )

                    ExposedDropdownMenu(
                        expanded = showCategoryDropdown,
                        onDismissRequest = { showCategoryDropdown = false }
                    ) {
                        ParticipantCategory.values().forEach { category ->
                            DropdownMenuItem(
                                text = { Text(category.displayName) },
                                onClick = {
                                    categorySelected = category
                                    showCategoryDropdown = false
                                }
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Botón de registro
        Button(
            onClick = {
                val ageInt = age.toIntOrNull() ?: 0
                val person = Person(
                    name = name.trim(),
                    lastName = lastName.trim(),
                    email = email.trim(),
                    phone = phone.trim(),
                    age = ageInt,
                    address = address.trim(),
                    type = typeSelected,
                    category = categorySelected
                )
                viewModel.addPerson(person)
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            enabled = !uiState.isLoading &&
                    name.isNotBlank() &&
                    lastName.isNotBlank() &&
                    email.isNotBlank() &&
                    phone.isNotBlank() &&
                    age.isNotBlank() &&
                    address.isNotBlank()
        ) {
            if (uiState.isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(20.dp),
                    color = MaterialTheme.colorScheme.onPrimary
                )
            } else {
                Text("✅ Registrar Participante", fontSize = 16.sp)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Botón para ver lista
        OutlinedButton(
            onClick = navigateToList,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("👥 Ver Lista de Participantes")
        }
    }

    // Mostrar mensajes
    uiState.message?.let { message ->
        LaunchedEffect(message) {
            viewModel.clearMessage()
        }

        // Snackbar o diálogo de éxito
        AlertDialog(
            onDismissRequest = { viewModel.clearMessage() },
            title = { Text("✅ Registro Exitoso") },
            text = { Text(message) },
            confirmButton = {
                Button(onClick = { viewModel.clearMessage() }) {
                    Text("Aceptar")
                }
            }
        )
    }

    uiState.error?.let { error ->
        AlertDialog(
            onDismissRequest = { viewModel.clearMessage() },
            title = { Text("❌ Error") },
            text = { Text(error) },
            confirmButton = {
                Button(onClick = { viewModel.clearMessage() }) {
                    Text("Aceptar")
                }
            }
        )
    }
}