package com.numad.aitranslator.dao

interface GenericResponse {
    data class Success(val data: Any? = null) : GenericResponse
    data class Failure(val error: String? = null) : GenericResponse
}
