package com.inacapsos.app.data.remote.dto

data class ReportDto(
    val id: String,
    val titulo: String,
    val descripcion: String,
    val tipo: String,
    val fecha: String,
    val estado: String
)
