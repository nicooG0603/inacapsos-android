package com.inacapsos.app.data.remote

import com.inacapsos.app.data.remote.dto.LoginRequestDto
import com.inacapsos.app.data.remote.dto.LoginResponseDto
import com.inacapsos.app.data.remote.dto.ReportDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

interface InacapApi {

    @POST("usuarios/login")
    suspend fun login(@Body request: LoginRequestDto): LoginResponseDto

    @GET("reportes")
    suspend fun getReportes(
        @Header("Authorization") token: String,
        @Query("usuarioId") usuarioId: String? = null
    ): List<ReportDto>
}
