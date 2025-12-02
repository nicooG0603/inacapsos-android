package com.inacapsos.app.ui.screens

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.inacapsos.app.data.remote.dto.IncidenteDto
import com.inacapsos.app.data.remote.dto.FechaDto
import com.inacapsos.app.data.repository.InacapRepositoryImpl
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GuardPanelScreen(
    navController: NavController,
    repository: InacapRepositoryImpl = InacapRepositoryImpl()
) {
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    var loading by remember { mutableStateOf(true) }
    var error by remember { mutableStateOf<String?>(null) }
    var incidentes by remember { mutableStateOf<List<IncidenteDto>>(emptyList()) }

    fun cargarIncidentes() {
        scope.launch {
            loading = true
            error = null
            try {
                // Ordena: primero SOS arriba
                incidentes = repository.getIncidentes()
                    .sortedByDescending { it.tipo == "sos" }
            } catch (e: Exception) {
                error = e.message ?: "Error al cargar incidentes"
            } finally {
                loading = false
            }
        }
    }

    LaunchedEffect(Unit) {
        cargarIncidentes()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Panel de Guardias – InacapSOS") }
            )
        }
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(16.dp)
        ) {

            // Título + botón actualizar
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Alertas y reportes",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )

                Button(onClick = { cargarIncidentes() }) {
                    Text("Actualizar")
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Loading
            if (loading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
                return@Column
            }

            // Error
            error?.let {
                Text("Error: $it", color = Color.Red)
            }

            // Lista vacía
            if (incidentes.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("No hay alertas registradas.")
                }
            } else {
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(incidentes) { incidente ->
                        GuardIncidentCard(
                            incidente = incidente,
                            onMarcarEnAtencion = {
                                Toast.makeText(
                                    context,
                                    "Marcado como 'En atención' (solo demo)",
                                    Toast.LENGTH_SHORT
                                ).show()
                            },
                            onCerrar = {
                                Toast.makeText(
                                    context,
                                    "Alerta cerrada (solo demo)",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            }
        }
    }
}

@Composable
private fun GuardIncidentCard(
    incidente: IncidenteDto,
    onMarcarEnAtencion: () -> Unit,
    onCerrar: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {

            // Tipo + Estado
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = incidente.tipo.uppercase(),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = if (incidente.tipo == "sos") Color.Red else Color.Black
                )

                Text(
                    text = incidente.estado,
                    style = MaterialTheme.typography.labelMedium,
                    color = when (incidente.estado.lowercase()) {
                        "activa" -> Color.Red
                        "en atencion", "en atención" -> Color(0xFFFFA000)
                        "cerrada", "resuelta" -> Color(0xFF388E3C)
                        else -> Color.Gray
                    }
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            // Descripción
            Text(
                text = incidente.descripcion,
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(modifier = Modifier.height(4.dp))

            // Usuario
            Text(
                text = "Usuario ID: ${incidente.usuario_id.id}",
                style = MaterialTheme.typography.bodySmall
            )

            // Fecha formateada
            Text(
                text = "Fecha: ${formatFecha(incidente.fecha)}",
                style = MaterialTheme.typography.bodySmall
            )

            // Ubicación
            Text(
                text = "Ubicación: ${incidente.ubicacion.latitude}, ${incidente.ubicacion.longitude}",
                style = MaterialTheme.typography.bodySmall
            )

            // Evidencia
            if (incidente.evidencia_url.isNotBlank()) {
                Text(
                    text = "Evidencia: ${incidente.evidencia_url}",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color(0xFF1565C0)
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            // BOTONES
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Button(
                    onClick = onMarcarEnAtencion,
                    modifier = Modifier.weight(1f)
                ) {
                    Text("En atención")
                }

                OutlinedButton(
                    onClick = onCerrar,
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Cerrar")
                }
            }
        }
    }
}

// Formato fecha Firebase
private fun formatFecha(fecha: FechaDto): String {
    val millis = fecha.seconds * 1000
    val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
    return sdf.format(Date(millis))
}
