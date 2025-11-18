package com.inacapsos.app.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.preference.PreferenceManager
import com.inacapsos.app.core.AppSession
import com.inacapsos.app.data.remote.dto.FechaDto
import com.inacapsos.app.data.remote.dto.GeoPointDto
import com.inacapsos.app.data.remote.dto.IncidenteDto
import com.inacapsos.app.data.repository.InacapRepositoryImpl
import kotlinx.coroutines.launch
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import java.util.Date

@Composable
fun MapScreen() {
    val context = LocalContext.current
    val repository = remember { InacapRepositoryImpl() }
    val scope = rememberCoroutineScope()

    var showDialog by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }
    var error by remember { mutableStateOf<String?>(null) }
    var incidentes by remember { mutableStateOf<List<IncidenteDto>>(emptyList()) }

    val incidentTypes = listOf(
        IncidentType("Emergencia Seguridad", Icons.Default.Add),
        IncidentType("Emergencia Ambulancia", Icons.Default.Add),
        IncidentType("Emergencia Incendio", Icons.Default.Add),
        IncidentType("Robo a Persona", Icons.Default.Add),
        IncidentType("Robo de Vehículo", Icons.Default.Add),
        IncidentType("Robo a Casa", Icons.Default.Add)
    )

    LaunchedEffect(Unit) {
        scope.launch {
            isLoading = true
            try {
                incidentes = repository.getIncidentes()
            } catch (e: Exception) {
                error = e.message ?: "No se pudieron cargar los incidentes."
            } finally {
                isLoading = false
            }
        }
    }

    Configuration.getInstance().load(
        context,
        PreferenceManager.getDefaultSharedPreferences(context)
    )
    Configuration.getInstance().userAgentValue = "com.inacapsos.app"

    Box(modifier = Modifier.fillMaxSize()) {
        AndroidView(
            modifier = Modifier.fillMaxSize(),
            factory = { 
                MapView(it).apply {
                    setTileSource(TileSourceFactory.MAPNIK)
                    controller.setZoom(12.0)
                    controller.setCenter(GeoPoint(-33.4489, -70.6693)) // Coordenadas de Santiago
                    setMultiTouchControls(true)
                }
            },
            update = {
                mapView ->
                mapView.overlays.clear()
                incidentes.forEach { incidente ->
                    val marker = Marker(mapView)
                    marker.position = GeoPoint(incidente.ubicacion.latitude, incidente.ubicacion.longitude)
                    marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
                    marker.title = incidente.tipo
                    marker.snippet = incidente.descripcion
                    mapView.overlays.add(marker)
                }
                mapView.invalidate() // Redraw the map
            }
        )

        FloatingActionButton(
            onClick = { showDialog = true },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
        ) {
            Icon(Icons.Filled.Add, contentDescription = "Reportar Incidente")
        }

        if (showDialog) {
            AlertDialog(
                onDismissRequest = { showDialog = false },
                title = { Text("Reportar Incidente") },
                text = {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(3),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(incidentTypes) { incident ->
                            Card(
                                modifier = Modifier.clickable { 
                                    scope.launch {
                                        isLoading = true
                                        try {
                                            val now = Date()
                                            val fechaDto = FechaDto(
                                                seconds = now.time / 1000,
                                                nanoseconds = (now.time % 1000) * 1_000_000L
                                            )
                                            repository.reportIncident(
                                                incident = IncidenteDto(
                                                    tipo = incident.name,
                                                    descripcion = "Descripción de ejemplo",
                                                    fecha = fechaDto,
                                                    ubicacion = GeoPointDto(latitude = -33.4489, longitude = -70.6693),
                                                    usuario_id = AppSession.userId ?: "",
                                                    estado = "activo",
                                                    evidencia_url = ""
                                                )
                                            )
                                            incidentes = repository.getIncidentes()
                                            showDialog = false
                                        } catch (e: Exception) {
                                            error = e.message ?: "Ocurrió un error inesperado"
                                        } finally {
                                            isLoading = false
                                        }
                                    }
                                }
                            ) {
                                Column(
                                    modifier = Modifier.padding(8.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Icon(incident.icon, contentDescription = null, modifier = Modifier.size(24.dp))
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(text = incident.name, style = MaterialTheme.typography.bodySmall)
                                }
                            }
                        }
                    }
                },
                confirmButton = { },
                dismissButton = {
                    TextButton(onClick = { showDialog = false }) {
                        Text("Cancelar")
                    }
                }
            )
        }

        if (isLoading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        }

        error?.let {
            AlertDialog(
                onDismissRequest = { error = null },
                title = { Text("Error") },
                text = { Text(it) },
                confirmButton = {
                    Button(onClick = { error = null }) {
                        Text("Aceptar")
                    }
                }
            )
        }
    }
}

data class IncidentType(val name: String, val icon: ImageVector)
