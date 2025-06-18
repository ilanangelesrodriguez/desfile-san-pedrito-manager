package com.uns.desfilesanpedritomanager.presentation.screens

import androidx.compose.foundation.background
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.uns.desfilesanpedritomanager.domain.ParticipantCategory
import com.uns.desfilesanpedritomanager.domain.Person
import com.uns.desfilesanpedritomanager.domain.TypePerson
import com.uns.desfilesanpedritomanager.presentation.viewmodel.PersonViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatisticsScreen(
    viewModel: PersonViewModel
) {
    val statistics by viewModel.statistics.collectAsState()
    val allPersons by viewModel.allPersons.collectAsState()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text(
                text = "📊 Estadísticas del Desfile",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }

        // Tarjetas de resumen
        item {
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(horizontal = 4.dp)
            ) {
                item {
                    StatCard(
                        title = "Total",
                        value = statistics.totalParticipants.toString(),
                        icon = Icons.Default.Person,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
                item {
                    StatCard(
                        title = "Activos",
                        value = statistics.activeParticipants.toString(),
                        icon = Icons.Default.CheckCircle,
                        color = Color(0xFF4CAF50)
                    )
                }
                item {
                    StatCard(
                        title = "Edad Promedio",
                        value = "${statistics.averageAge.toInt()}",
                        icon = Icons.Default.DateRange,
                        color = Color(0xFFFF9800)
                    )
                }
                item {
                    StatCard(
                        title = "Registros Hoy",
                        value = statistics.recentRegistrations.toString(),
                        icon = Icons.Default.Notifications,
                        color = Color(0xFF9C27B0)
                    )
                }
            }
        }

        // Gráfico por tipo de participante
        item {
            StatisticsCard(
                title = "👥 Por Tipo de Participante",
                content = {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        statistics.participantsByType.forEach { (type, count) ->
                            TypeStatRow(
                                type = type,
                                count = count,
                                total = statistics.totalParticipants
                            )
                        }
                    }
                }
            )
        }

        // Gráfico por categoría
        item {
            StatisticsCard(
                title = "🎭 Por Categoría",
                content = {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        statistics.participantsByCategory.forEach { (category, count) ->
                            CategoryStatRow(
                                category = category,
                                count = count,
                                total = statistics.totalParticipants
                            )
                        }
                    }
                }
            )
        }

        // Distribución por edad
        item {
            StatisticsCard(
                title = "📈 Distribución por Edad",
                content = {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        statistics.ageRanges.forEach { (range, count) ->
                            AgeRangeStatRow(
                                range = range,
                                count = count,
                                total = statistics.totalParticipants
                            )
                        }
                    }
                }
            )
        }

        // Registros recientes
        if (allPersons.isNotEmpty()) {
            item {
                StatisticsCard(
                    title = "🕒 Registros Recientes",
                    content = {
                        val recentPersons = allPersons
                            .sortedByDescending { it.registrationDate }
                            .take(5)

                        Column(
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            recentPersons.forEach { person ->
                                RecentRegistrationRow(person = person)
                            }
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun StatCard(
    title: String,
    value: String,
    icon: ImageVector,
    color: Color
) {
    Card(
        modifier = Modifier
            .width(120.dp)
            .height(100.dp),
        colors = CardDefaults.cardColors(
            containerColor = color.copy(alpha = 0.1f)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = color,
                modifier = Modifier.size(24.dp)
            )
            Text(
                text = value,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = color
            )
            Text(
                text = title,
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun StatisticsCard(
    title: String,
    content: @Composable () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = title,
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(bottom = 12.dp)
            )
            content()
        }
    }
}

@Composable
fun TypeStatRow(
    type: TypePerson,
    count: Int,
    total: Int
) {
    val percentage = if (total > 0) (count.toFloat() / total * 100).toInt() else 0

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = type.displayName,
            modifier = Modifier.weight(1f)
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Box(
                modifier = Modifier
                    .width(60.dp)
                    .height(8.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .fillMaxWidth(percentage / 100f)
                        .background(
                            MaterialTheme.colorScheme.primary,
                            RoundedCornerShape(4.dp)
                        )
                )
            }

            Text(
                text = "$count ($percentage%)",
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.width(80.dp),
                textAlign = TextAlign.End
            )
        }
    }
}

@Composable
fun CategoryStatRow(
    category: ParticipantCategory,
    count: Int,
    total: Int
) {
    val percentage = if (total > 0) (count.toFloat() / total * 100).toInt() else 0
    val categoryColor = Color(android.graphics.Color.parseColor(category.color))

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.weight(1f)
        ) {
            Box(
                modifier = Modifier
                    .size(12.dp)
                    .background(categoryColor, RoundedCornerShape(6.dp))
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = category.displayName)
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Box(
                modifier = Modifier
                    .width(60.dp)
                    .height(8.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .fillMaxWidth(percentage / 100f)
                        .background(categoryColor, RoundedCornerShape(4.dp))
                )
            }

            Text(
                text = "$count ($percentage%)",
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.width(80.dp),
                textAlign = TextAlign.End
            )
        }
    }
}

@Composable
fun AgeRangeStatRow(
    range: String,
    count: Int,
    total: Int
) {
    val percentage = if (total > 0) (count.toFloat() / total * 100).toInt() else 0

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "$range años",
            modifier = Modifier.weight(1f)
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Box(
                modifier = Modifier
                    .width(60.dp)
                    .height(8.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .fillMaxWidth(percentage / 100f)
                        .background(
                            Color(0xFFFF9800),
                            RoundedCornerShape(4.dp)
                        )
                )
            }

            Text(
                text = "$count ($percentage%)",
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.width(80.dp),
                textAlign = TextAlign.End
            )
        }
    }
}

@Composable
fun RecentRegistrationRow(
    person: Person
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = person.fullName,
                fontWeight = FontWeight.Medium,
                fontSize = 14.sp
            )
            Text(
                text = person.type.displayName,
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        Text(
            text = person.formattedRegistrationDate,
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}
