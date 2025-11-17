package com.inacapsos.app.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun SosScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Botón SOS",
            style = MaterialTheme.typography.headlineSmall
        )
        Text(
            text = "Aquí podrás enviar una alerta rápida al equipo de seguridad. Por ahora es una pantalla de ejemplo.",
            style = MaterialTheme.typography.bodyMedium
        )
        Button(onClick = { /* TODO: conectar con API SOS */ }) {
            Text("Enviar SOS")
        }
    }
}
