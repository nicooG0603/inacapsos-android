package com.inacapsos.app.data.repository

import com.inacapsos.app.data.remote.dto.LoginResponseDto
import com.inacapsos.app.data.remote.dto.ReportDto

interface InacapRepository {
    suspend fun login(email: String, password: String): LoginResponseDto
    suspend fun getReportes(token: String, usuarioId: String? = null): List<ReportDto>
}
