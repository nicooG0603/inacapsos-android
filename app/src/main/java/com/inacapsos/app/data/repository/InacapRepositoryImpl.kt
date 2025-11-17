package com.inacapsos.app.data.repository

import com.inacapsos.app.data.remote.ApiClient
import com.inacapsos.app.data.remote.dto.LoginRequestDto
import com.inacapsos.app.data.remote.dto.LoginResponseDto
import com.inacapsos.app.data.remote.dto.ReportDto

class InacapRepositoryImpl : InacapRepository {

    private val api = ApiClient.api

    override suspend fun login(email: String, password: String): LoginResponseDto {
        return api.login(LoginRequestDto(email = email, contrasena = password))
    }

    override suspend fun getReportes(token: String, usuarioId: String?): List<ReportDto> {
        val authHeader = if (token.startsWith("Bearer")) token else "Bearer $token"
        return api.getReportes(token = authHeader, usuarioId = usuarioId)
    }
}
