package com.inacapsos.app.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.inacapsos.app.core.AppSession
import com.inacapsos.app.data.remote.dto.ReportDto
import com.inacapsos.app.data.repository.InacapRepositoryImpl
import kotlinx.coroutines.launch

@Composable
fun ReportsScreen() {
    val repository = remember { InacapRepositoryImpl() }
    val scope = rememberCoroutineScope()

    var reports by remember { mutableStateOf<List<ReportDto>>(emptyList()) }
    var isLoading by remember { mutableStateOf(false) }
    var error by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        val token = AppSession.token
        val userId = AppSession.userId
        if (!token.isNullOrBlank()) {
            isLoading = true
            error = null
            scope.launch {
                try {
                    reports = repository.getReportes(token = token, usuarioId = userId)
                } catch (e: Exception) {
                    error = e.message ?: "No se pudieron cargar los reportes."
                } finally {
                    isLoading = false
                }
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Mis reportes",
            style = MaterialTheme.typography.headlineSmall
        )
        Spacer(modifier = Modifier.height(8.dp))

        if (isLoading) {
            Text("Cargando...")
        } else if (error != null) {
            Text(
                text = error!!,
                color = MaterialTheme.colorScheme.error
            )
        } else if (reports.isEmpty()) {
            Text("No tienes reportes registrados.")
        } else {
            LazyColumn {
                items(reports) { report ->
                    Card(
                        modifier = Modifier
                            .padding(vertical = 4.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text(
                                text = report.titulo,
                                style = MaterialTheme.typography.titleMedium
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = report.descripcion,
                                style = MaterialTheme.typography.bodySmall
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Estado: ${report.estado}",
                                style = MaterialTheme.typography.labelMedium
                            )
                        }
                    }
                }
            }
        }
    }
}
