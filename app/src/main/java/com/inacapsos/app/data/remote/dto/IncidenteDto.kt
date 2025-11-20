package com.inacapsos.app.data.remote.dto

import com.google.gson.annotations.SerializedName

data class IncidenteDto(
    @SerializedName("tipo") val tipo: String,
    @SerializedName("descripcion") val descripcion: String,
    @SerializedName("fecha") val fecha: FechaDto,
    @SerializedName("ubicacion") val ubicacion: GeoPointDto,
    @SerializedName("usuario_id") val usuario_id: UsuarioIdDto,
    @SerializedName("estado") val estado: String,
    @SerializedName("evidencia_url") val evidencia_url: String
)

data class GeoPointDto(
    @SerializedName("latitude") val latitude: Double,
    @SerializedName("longitude") val longitude: Double
)

data class FechaDto(
    @SerializedName("_seconds") val seconds: Long,
    @SerializedName("_nanoseconds") val nanoseconds: Long
)

data class UsuarioIdDto(
    @SerializedName("id") val id: String
)
