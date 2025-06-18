package com.uns.desfilesanpedritomanager.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.uns.desfilesanpedritomanager.domain.ParticipantCategory
import com.uns.desfilesanpedritomanager.domain.Person
import com.uns.desfilesanpedritomanager.domain.TypePerson
import com.uns.desfilesanpedritomanager.presentation.viewmodel.PersonViewModel
import com.uns.desfilesanpedritomanager.presentation.viewmodel.SortOption
import androidx.core.graphics.toColorInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListScreen(
    navigateToRegister: () -> Unit,
    viewModel: PersonViewModel
) {
    val filteredPersons by viewModel.filteredPersons.collectAsState()
    val allPersons by viewModel.allPersons.collectAsState()
    val currentTypeFilter by viewModel.currentTypeFilter.collectAsState()
    val currentCategoryFilter by viewModel.currentCategoryFilter.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()

    var selectedPerson by remember { mutableStateOf<Person?>(null) }
    var showDeleteDialog by remember { mutableStateOf(false) }
    var showFilterDialog by remember { mutableStateOf(false) }
    var showSortDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Header con título y estadísticas rápidas
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    "👥 Participantes",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    "${filteredPersons.size} de ${allPersons.size} participantes",
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Barra de búsqueda
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { viewModel.setSearchQuery(it) },
            label = { Text("Buscar participante...") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Buscar"
                )
            },
            trailingIcon = {
                if (searchQuery.isNotEmpty()) {
                    IconButton(onClick = { viewModel.setSearchQuery("") }) {
                        Icon(
                            imageVector = Icons.Default.Clear,
                            contentDescription = "Limpiar búsqueda"
                        )
                    }
                }
            }
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Botones de acción
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(horizontal = 4.dp)
        ) {
            item {
                FilterChip(
                    onClick = { showFilterDialog = true },
                    label = {
                        Text(
                            if (currentTypeFilter != null || currentCategoryFilter != null)
                                "Filtros (${if (currentTypeFilter != null) 1 else 0 + if (currentCategoryFilter != null) 1 else 0})"
                            else "Filtros"
                        )
                    },
                    selected = currentTypeFilter != null || currentCategoryFilter != null,
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Menu,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                )
            }

            item {
                FilterChip(
                    onClick = { showSortDialog = true },
                    label = { Text("Ordenar") },
                    selected = false,
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.KeyboardArrowDown,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                )
            }

            if (currentTypeFilter != null || currentCategoryFilter != null || searchQuery.isNotEmpty()) {
                item {
                    FilterChip(
                        onClick = {
                            viewModel.clearFilters()
                        },
                        label = { Text("Limpiar") },
                        selected = false,
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Clear,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Lista de participantes
        if (filteredPersons.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = null,
                        modifier = Modifier.size(64.dp),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                    )
                    Text(
                        if (allPersons.isEmpty())
                            "No hay participantes registrados"
                        else
                            "No se encontraron participantes con los filtros aplicados",
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(filteredPersons, key = { it.id }) { person ->
                    PersonCard(
                        person = person,
                        onDelete = {
                            selectedPerson = person
                            showDeleteDialog = true
                        },
                        onToggleStatus = {
                            viewModel.togglePersonStatus(person.id)
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Botón para agregar nuevo participante
        Button(
            onClick = navigateToRegister,
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = null,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text("Registrar Nuevo Participante")
        }
    }

    // Diálogos
    if (showDeleteDialog && selectedPerson != null) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("🗑️ Confirmar Eliminación") },
            text = {
                Text("¿Está seguro que desea eliminar a ${selectedPerson?.fullName}?")
            },
            confirmButton = {
                Button(
                    onClick = {
                        selectedPerson?.id?.let { viewModel.deletePerson(it) }
                        showDeleteDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.error
                    )
                ) {
                    Text("Eliminar")
                }
            },
            dismissButton = {
                OutlinedButton(onClick = { showDeleteDialog = false }) {
                    Text("Cancelar")
                }
            }
        )
    }

    if (showFilterDialog) {
        FilterDialog(
            currentTypeFilter = currentTypeFilter,
            currentCategoryFilter = currentCategoryFilter,
            onTypeFilterChange = { viewModel.setTypeFilter(it) },
            onCategoryFilterChange = { viewModel.setCategoryFilter(it) },
            onDismiss = { showFilterDialog = false }
        )
    }

    if (showSortDialog) {
        SortDialog(
            onSortSelected = { sortOption ->
                viewModel.sortPersons(sortOption)
                showSortDialog = false
            },
            onDismiss = { showSortDialog = false }
        )
    }
}

@Composable
fun PersonCard(
    person: Person,
    onDelete: () -> Unit,
    onToggleStatus: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            person.fullName,
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )

                        if (!person.isActive) {
                            Surface(
                                color = MaterialTheme.colorScheme.error.copy(alpha = 0.1f),
                                shape = RoundedCornerShape(4.dp)
                            ) {
                                Text(
                                    "Inactivo",
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                    fontSize = 10.sp,
                                    color = MaterialTheme.colorScheme.error,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }
                    }


                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        "📧 ${person.email}",
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                    Text(
                        "📱 ${person.phone}",
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Text(
                        "🎂 ${person.age} años",
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Text(
                        "📍 ${person.address}",
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Column(
                    horizontalAlignment = Alignment.End,
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        IconButton(
                            onClick = onToggleStatus,
                            modifier = Modifier.size(32.dp)
                        ) {
                            Icon(
                                imageVector = if (person.isActive) Icons.Default.ArrowBack else Icons.Default.ArrowForward,
                                contentDescription = if (person.isActive) "Desactivar" else "Activar",
                                tint = if (person.isActive) Color(0xFF4CAF50) else MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.size(20.dp)
                            )
                        }

                        IconButton(
                            onClick = onDelete,
                            modifier = Modifier.size(32.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "Eliminar",
                                tint = MaterialTheme.colorScheme.error,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Tags de tipo y categoría
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Tag de tipo
                Surface(
                    color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        person.type.displayName,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Medium
                    )
                }

                // Tag de categoría
                val categoryColor = Color(person.category.color.toColorInt())
                Surface(
                    color = categoryColor.copy(alpha = 0.1f),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        person.category.displayName,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        fontSize = 12.sp,
                        color = categoryColor,
                        fontWeight = FontWeight.Medium
                    )
                }

            }

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Text(
                    person.formattedRegistrationDate,
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
fun FilterDialog(
    currentTypeFilter: TypePerson?,
    currentCategoryFilter: ParticipantCategory?,
    onTypeFilterChange: (TypePerson?) -> Unit,
    onCategoryFilterChange: (ParticipantCategory?) -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("🔍 Filtros") },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Filtro por tipo
                Text(
                    "Tipo de Participante:",
                    fontWeight = FontWeight.Medium
                )

                Column {
                    FilterOption(
                        text = "Todos",
                        selected = currentTypeFilter == null,
                        onClick = { onTypeFilterChange(null) }
                    )
                    TypePerson.values().forEach { type ->
                        FilterOption(
                            text = type.displayName,
                            selected = currentTypeFilter == type,
                            onClick = { onTypeFilterChange(type) }
                        )
                    }
                }

                Divider()

                // Filtro por categoría
                Text(
                    "Categoría:",
                    fontWeight = FontWeight.Medium
                )

                Column {
                    FilterOption(
                        text = "Todas",
                        selected = currentCategoryFilter == null,
                        onClick = { onCategoryFilterChange(null) }
                    )
                    ParticipantCategory.entries.forEach { category ->
                        FilterOption(
                            text = category.displayName,
                            selected = currentCategoryFilter == category,
                            onClick = { onCategoryFilterChange(category) }
                        )
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Cerrar")
            }
        }
    )
}

@Composable
fun SortDialog(
    onSortSelected: (SortOption) -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("📊 Ordenar por") },
        text = {
            Column {
                SortOption.values().forEach { option ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onSortSelected(option) }
                            .padding(vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(option.displayName)
                    }
                }
            }
        },
        confirmButton = {},
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}

@Composable
fun FilterOption(
    text: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(
            selected = selected,
            onClick = onClick
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = text)
    }
}
