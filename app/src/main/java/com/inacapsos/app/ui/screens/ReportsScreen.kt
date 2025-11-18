package com.inacapsos.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.inacapsos.app.core.AppSession
import com.inacapsos.app.data.remote.dto.ReportDto
import com.inacapsos.app.data.repository.InacapRepositoryImpl
import kotlinx.coroutines.launch

@Composable
fun ReportsScreen() {
    val inacapRed = Color(0xFFCC0000)
    val repo = remember { InacapRepositoryImpl() }

    var reports by remember { mutableStateOf<List<ReportDto>>(emptyList()) }
    var loading by remember { mutableStateOf(true) }

    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        scope.launch {
            loading = false
            // cuando haya API: reports = repo.getReportes(...)
        }
    }

    Surface(modifier = Modifier.fillMaxSize()) {
        Column {

            // 🔴 HEADER
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(inacapRed)
                    .padding(16.dp)
            ) {
                Column {
                    Text(
                        "Mis reportes",
                        style = MaterialTheme.typography.headlineSmall,
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        "Estado de los reportes enviados",
                        color = Color.White.copy(alpha = 0.85f)
                    )
                }
            }

            // 🩶 FONDO GRIS CLARO
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.surfaceVariant)
                    .padding(16.dp)
            ) {
                if (loading) {
                    Text("Cargando...")
                } else if (reports.isEmpty()) {
                    Text("No tienes reportes registrados.")
                } else {
                    LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        items(reports) {
                            ReportCardItem(it)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ReportCardItem(report: ReportDto) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
        colors = CardDefaults.cardColors(MaterialTheme.colorScheme.surface)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Text(report.titulo, fontWeight = FontWeight.SemiBold)
            Spacer(modifier = Modifier.height(4.dp))
            Text(report.descripcion, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.75f))
        }
    }
}