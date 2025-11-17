package com.inacapsos.app.ui.screens

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.preference.PreferenceManager
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView

@Composable
fun MapScreen() {
    val context = LocalContext.current
    // Es importante configurar el "user agent" para evitar ser bloqueado por los servidores de mapas.
    // Se recomienda hacer esto en la clase Application de tu app.
    Configuration.getInstance().load(
        context,
        PreferenceManager.getDefaultSharedPreferences(context)
    )
    Configuration.getInstance().userAgentValue = "com.inacapsos.app"

    AndroidView(
        modifier = Modifier.fillMaxSize(),
        factory = { 
            MapView(it).apply {
                setTileSource(TileSourceFactory.MAPNIK)
                controller.setZoom(12.0)
                controller.setCenter(GeoPoint(-33.4489, -70.6693)) // Coordenadas de Santiago
                setMultiTouchControls(true)
            }
        }
    )
}
