
package com.inacapsos.app.data.remote.dto

import com.google.gson.annotations.SerializedName

data class IncidenteDto(
    @SerializedName("tipo") val tipo: String,
    @SerializedName("descripcion") val descripcion: String,
    @SerializedName("fecha") val fecha: String,
    @SerializedName("ubicacion") val ubicacion: GeoPointDto,
    @SerializedName("usuarioId") val usuarioId: String
)

data class GeoPointDto(
    @SerializedName("latitude") val latitude: Double,
    @SerializedName("longitude") val longitude: Double
)
